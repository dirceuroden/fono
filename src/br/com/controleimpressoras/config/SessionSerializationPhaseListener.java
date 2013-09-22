package br.com.controleimpressoras.config;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.log4j.Logger;

/**
 * <code>PhaseListener</code> do JSF para forçar a serialização da sessão do App Engine.
 * 
 * @author YaW Tecnologia
 */
public class SessionSerializationPhaseListener implements PhaseListener {
 
	private static Logger log = Logger.getLogger(SessionSerializationPhaseListener.class);

	public SessionSerializationPhaseListener() { }
	
    private void serialSession() {
        log.debug("Serializando a session web.");
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Map sessionMap = facesContext.getExternalContext().getSessionMap();
        sessionMap.put("forceGaeSessionSerialization", System.currentTimeMillis());
    }
    
    public void afterPhase(final PhaseEvent event) {
    	if (event.getPhaseId().equals(PhaseId.RENDER_RESPONSE) 
    			|| event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)
    			|| (event.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES) 
    					&& event.getFacesContext().getResponseComplete())) {
    		serialSession();
    	}
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
    public void beforePhase(PhaseEvent event) { }

}
