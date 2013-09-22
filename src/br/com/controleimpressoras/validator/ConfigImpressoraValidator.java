package br.com.controleimpressoras.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.controleimpressoras.dao.ConfigImpressoraDAO;
import br.com.controleimpressoras.model.ConfigImpressora;

@FacesValidator("br.com.controleimpressoras.validator.ConfigImpressoraValidator")
public class ConfigImpressoraValidator implements Serializable, Validator {
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		ConfigImpressoraDAO configImpressoraDAO = new ConfigImpressoraDAO();
		ConfigImpressora configImpressora = configImpressoraDAO.findByPK((Long)value);
		if (configImpressora == null) {
			FacesMessage msg = new FacesMessage("Erro.", "Configuração de impressora não cadastrada.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
