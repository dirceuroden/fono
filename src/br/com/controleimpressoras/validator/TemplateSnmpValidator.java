package br.com.controleimpressoras.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.controleimpressoras.dao.TemplateSnmpDAO;
import br.com.controleimpressoras.model.TemplateSnmp;

@FacesValidator("br.com.controleimpressoras.validator.TemplateSnmpValidator")
public class TemplateSnmpValidator implements Serializable, Validator {
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		TemplateSnmpDAO templateSnmpDAO = new TemplateSnmpDAO();
		TemplateSnmp templateSnmp = templateSnmpDAO.findByPK((Long)value);
		if (templateSnmp == null) {
			FacesMessage msg = new FacesMessage("Erro.", "Template não cadastrado.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
