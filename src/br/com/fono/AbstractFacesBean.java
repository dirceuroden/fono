
package br.com.fono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;




/**
 * @author Dirceu 
 * Esta classe fornece m�todos utilit�rios para os managed beans.
 */
@SuppressWarnings("deprecation")
public abstract class AbstractFacesBean{

    public AbstractFacesBean() {
    }
    
    /**
     * Retorna uma instancia da aplica��o atual
     */
    protected final Application getApplication() {
        return getFacesContext().getApplication();
    }

    /**
     * Retorna um mapa com os atributos do escopo de aplica��o
     */
    @SuppressWarnings("unchecked")
	protected final Map getApplicationMap() {
        return getExternalContext().getApplicationMap();
    }

    /**
     * Retorna uma instancia de ExternalContext da requisi��o atual
     */
    protected final ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    /**
     * Retorna uma instancia de FacesContext da requisi��o atual 
     */
    protected final FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Retorna uma instancia de Lifecycle da requisi��o atual
     */
    protected final Lifecycle getLifecycle() {
        String lifecycleId = getExternalContext().getInitParameter("javax.faces.LIFECYCLE_ID");
        if (lifecycleId == null || lifecycleId.length() == 0) {
            lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        return lifecycleFactory.getLifecycle(lifecycleId);
    }

    /**
     * Retorna um mapa com os atributos do escopo de requisicao
     */
    @SuppressWarnings("unchecked")
	protected final Map getRequestMap() {
        return getExternalContext().getRequestMap();
    }

    /**
     * Retorna um mapa com os atributos da sess�o do usu�rio atual.
     * Se n�o houver uma sess�o, ser� criada uma.
     */
    @SuppressWarnings("unchecked")
	protected final Map getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    /**
     * Retorna um bean de qualquer escopo com o nome especificado.
     * Se n�o existir uma inst�ncia do bean, ent�o uma nova insrancia
     * ser� criada. 
     * Se n�o exixtir nenhum bean com este nome, retorna null
     */
    protected final Object getBean(String name) {
        return getApplication().getVariableResolver().resolveVariable(getFacesContext(), name);
    }

    /**
     * Substitui um bean em qualquer escopo pelo bean especificado.
     * Se o bean especificado n�o existir ser� instanciado
     * um novo bean com escopo de requisi��o.
     */
    protected final void setBean(String name, Object value) {
        setValue("#{" + name + "}", value); 
    }

    /**
     * 
     * <p>The attribute name under which saved data will be stored on the
     * view root component.</p>
     */
    private static final String DATA_KEY = "com.sun.rave.web.ui.appbase.DATA";



    /**
     * <p>Return the data object stored (typically when the component tree
     * was previously rendered) under the specified key, if any; otherwise,
     * return <code>null</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE:</strong> Data objects will become
     * available only after the <em>Restore View</em> phase of the request
     * processing lifecycle has been completed.  A common place to reinitialize
     * state information, then, would be in the <code>preprocess()</code>
     * event handler of a page bean.</p>
     *
     * @param key Key under which to retrieve the requested data
     * @throws ClassNotFoundException 
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
	public final Object retrieveData(String key) {

        FacesContext context = getFacesContext();
        if (context == null) {
            return null;
        }
        UIViewRoot view = context.getViewRoot();
        if (view == null) {
            return null;
        }
        Map map = (Map) view.getAttributes().get(DATA_KEY);
        Object value = null;
        if (map != null) {
            //value = map.get(key);
            value = map.remove(key);
        }
        try {
			return (value == null) ? null : ((SerializeObject)value).deserialize();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

    }


    /**
     * <p>Save the specified data object (which <strong>MUST</strong> be
     * <code>Serializable</code>) under the specified key, such that it can
     * be retrieved (via <code>getData()</code>) on a s subsequent request
     * immediately after the component tree has been restored.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE:</strong> In order to successfully save
     * data objects, this method must be called before the <em>Render Response</em>
     * phase of the request processing lifecycle is executed.  A common scenario
     * is to save state information in the <code>prerender()</code> event handler
     * of a page bean.</p>
     *
     * @param key Key under which to store the requested data
     * @param data Data object to be stored
     * @throws IOException 
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
	public final void saveData(String key, Object data){
    	
    	SerializeObject so = null;
		try {
			so = new SerializeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Map map = (Map)
           getFacesContext().getViewRoot().getAttributes().get(DATA_KEY);
        if (map == null) {
            map = new HashMap();
            getFacesContext().getViewRoot().getAttributes().put(DATA_KEY, map);
        }
        map.put(key, so);

    }
    
    /**
     * Remove um dado guardado
     */
	@SuppressWarnings("unchecked")
	public void removeData(String key) {
		Map map = (Map) getFacesContext().getViewRoot().getAttributes().get(DATA_KEY);
		if (map != null) {
			map.remove(key);
		}
	}


    // ------------------------------------------------------ Value Manipulation


    /**
     * <p>Evaluate the specified value binding expression, and return
     * the value that it points at.</p>
     *
     * @param expr Value binding expression (including delimiters)
     */
    protected final Object getValue(String expr) {

        ValueBinding vb = getApplication().createValueBinding(expr);
        return (vb.getValue(getFacesContext()));

    }


    /**
     * <p>Evaluate the specified value binding expression, and update
     * the value that it points at.</p>
     *
     * @param expr Value binding expression (including delimiters) that
     *  must point at a writeable property
     * @param value New value for the property pointed at by <code>expr</code>
     */
    protected final void setValue(String expr, Object value) {

        ValueBinding vb = getApplication().createValueBinding(expr);
        vb.setValue(getFacesContext(), value);

    }


    // -------------------------------------------------- Component Manipulation


    /**
     * <p>Erase previously submitted values for all input components on this
     * page.  This method <strong>MUST</strong> be called if you have bound
     * input components to database columns, and then arbitrarily navigate
     * the underlying <code>RowSet</code> to a different row in an event
     * handler method.</p>
     */
    protected final void erase() {

        erase(getFacesContext().getViewRoot());

    }


    /**
     * <p>Private helper method for <code>erase()</code> that recursively
     * descends the component tree and performs the required processing.</p>
     *
     * @param component The component to be erased
     */
    @SuppressWarnings("unchecked")
	protected final void erase(UIComponent component) {

		// Erase the component itself (if needed)
		if (component instanceof EditableValueHolder) {
			EditableValueHolder edit = (EditableValueHolder) component;
			if (edit instanceof HtmlInputText) {
				if (!(((HtmlInputText) edit).isReadonly() || ((HtmlInputText) edit).isDisabled())) {
					edit.setValue(null);
				}
			} else if (edit instanceof HtmlInputTextarea) {
				if (!(((HtmlInputTextarea) edit).isReadonly() || ((HtmlInputTextarea) edit).isDisabled())) {
					edit.setValue(null);
				}
			} else {
				edit.setValue(null);
			}
			edit.setValid(true);
		}
        // Process the facets and children of this component
        Iterator kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            erase((UIComponent) kids.next());
        }

    }
    
    /**
     * Limpa os valores dos componentes
     */
    protected final void erase(String componentId) {
    	erase(findComponent(componentId));
    }
    
    /**
     * Retorna a instancia de um componente
     */
    protected final UIComponent findComponent(String clienteId) {
    	return getFacesContext().getViewRoot().findComponent(clienteId);
    }

    /**
     * Inclui a mensagem no arquivo de log do Conteiner
     */
    protected final void log(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            getExternalContext().log(message);
        } else {
            System.out.println(message);
        }
    }

    /**
     * Inclui a mensagem e a excess�o no arquivo de log do Conteiner
     */
    protected final void log(String message, Throwable throwable) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            getExternalContext().log(message, throwable);
        } else {
            System.out.println(message);
            throwable.printStackTrace(System.out);
        }
    }

    /**
     * Inclui uma mensagem SEVERITY_INFO resumida na fila global 
     */
    protected final void info(String summary) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_INFO resumida na fila do componente 
     */
    protected final void info(UIComponent component, String summary) {
        getFacesContext().addMessage(component.getClientId(getFacesContext()),
        		new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_WARN resumida na fila global 
     */
    protected final void warn(String summary) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_WARN resumida na fila do componente
     */
    protected final void warn(UIComponent component, String summary) {
        getFacesContext().addMessage(component.getClientId(getFacesContext()),
        		new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_ERROR resumida na fila global 
     */
    protected final void error(String summary) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_ERROR resumida na fila do componente
     */
    protected final void error(UIComponent component, String summary) {
        getFacesContext().addMessage(component.getClientId(getFacesContext()),
        		new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_FATAL resumida na fila global 
     */
    protected final void fatal(String summary) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, null));
    }

    /**
     * Inclui uma mensagem SEVERITY_FATAL resumida na fila do componente
     */
    protected final void fatal(UIComponent component, String summary) {
        getFacesContext().addMessage(component.getClientId(getFacesContext()),
        		new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, null));
    }
    
}
