package br.com.controleimpressoras.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.model.Impressora;

@FacesValidator("br.com.controleimpressoras.validator.ImpressoraValidator")
public class ImpressoraValidator implements Serializable, Validator {
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		ImpressoraDAO impressoraDAO = new ImpressoraDAO();
		Impressora impressora = impressoraDAO.findByPK(value.toString());
		if (impressora == null) {
			FacesMessage msg = new FacesMessage("Erro.", "Impressora não cadastrada.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
