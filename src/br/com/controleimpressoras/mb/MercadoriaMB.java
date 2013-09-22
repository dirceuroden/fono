package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.Mercadoria;

@ManagedBean
@ViewScoped
public class MercadoriaMB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MercadoriaMB.class);
	
	private Mercadoria mercadoria;
	
	private Long idSelecionado;
	
	private List<Mercadoria> mercadorias;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		fillMercadorias();
	}
	
	public List<Mercadoria> getMercadorias() {
		return mercadorias;
	}
	
	public Mercadoria getMercadoria() {
		return mercadoria;
	}
	
	public void setMercadoria(Mercadoria mercadoria) {
		this.mercadoria = mercadoria;
	}
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
		
		log.debug("setIdSelecionado = " + idSelecionado);
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	public List<Mercadoria> getListMeracdorias() {
        return mercadorias;
	}
	
	public boolean getEdit() {
		return edit;
	}
	
	@SuppressWarnings("unchecked")
	private void fillMercadorias() {
		mercadorias = new ArrayList<Mercadoria>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Mercadoria.class);
		q.setOrdering("nome asc");
		try {
			List<Mercadoria> result = (List<Mercadoria>) q.execute();
			mercadorias.addAll(result);
			log.debug("Carregou a lista de mercadorias ("+mercadorias.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de mercadorias.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.mercadoria"), ex.getMessage());
		} finally {
			pm.close();
		}
	}
	
	public void incluir(){
		mercadoria = new Mercadoria();
		log.debug("Pronto pra incluir");
		edit = true;
	}
	
	public void editar() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			mercadoria = pm.getObjectById(Mercadoria.class, idSelecionado);
			log.debug("Pronto pra editar" + idSelecionado);
		} catch(Exception ex) {
			log.error("Erro ao editar mercadoria.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.mercadoria"), ex.getMessage());
		} finally {
			pm.close();
		}
		edit = true;
	}

	public void salvar() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(mercadoria);
			log.debug("Salvou mercadoria "+mercadoria.getId());
		} catch(Exception ex) {
			log.error("Erro ao salvar mercadoria.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.mercadoria"), ex.getMessage());
		} finally {
			pm.close();
		}
		edit = false;
	}
	
	public void atualizar() {
		fillMercadorias();
		edit = false;
	}
	
	public void remover() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Mercadoria m = pm.getObjectById(Mercadoria.class, mercadoria.getId());
			pm.deletePersistent(m);
			log.debug("Removeu mercadoria "+mercadoria.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover mercadoria.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.mercadoria"), ex.getMessage());
		} finally {
			pm.close();
		}
		edit = false;
	}
	
	public void cancelar() {
		edit = false;
	}
	
	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
