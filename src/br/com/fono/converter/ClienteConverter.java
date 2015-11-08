package br.com.fono.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.fono.dao.ClienteDAO;
import br.com.fono.model.Cliente;

@FacesConverter("br.com.fono.converter.ClienteConverter")
public class ClienteConverter implements Converter {
	private ClienteDAO clienteDAO = new ClienteDAO();
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if (value != null && value.length() > 0) {
			Cliente cliente = clienteDAO.findByPK(value);
			return cliente;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj != null && obj.toString().length() > 0) {
			Cliente cliente = clienteDAO.findByPK(obj.toString());
			return (cliente != null) ? cliente.getNome() : "Não cadastrado";
		}
		return "";
	}

}
