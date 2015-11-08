package br.com.fono.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.fono.dao.ClienteDAO;
import br.com.fono.model.Cliente;

@FacesValidator("br.com.controleimpressoras.validator.ClienteValidator")
public class ClienteValidator implements Serializable, Validator {
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		ClienteDAO clienteDAO = new ClienteDAO();
		Cliente cliente = clienteDAO.findByPK(value.toString());
		if (cliente == null) {
			FacesMessage msg = new FacesMessage("Erro.", "Cliente não cadastrado.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
