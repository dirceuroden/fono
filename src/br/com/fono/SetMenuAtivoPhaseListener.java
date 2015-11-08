package br.com.fono;

import java.util.Map;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SetMenuAtivoPhaseListener implements PhaseListener {
 	private static final long serialVersionUID = 1L;

	public void afterPhase(final PhaseEvent event) {}
    
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
    public void beforePhase(PhaseEvent event) { 
    	final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Map<String, String> map = facesContext.getExternalContext().getRequestParameterMap();
    	
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ParametroMB parametroMB = (ParametroMB) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "parametroMB");
        
        String active = map.get("active");
        
        if (active != null && active.length() > 0) {
        	parametroMB.setMenuAtivo(map.get("active"));
        }
    }
}
