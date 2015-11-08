package br.com.fono.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.fono.dao.PacienteDAO;
import br.com.fono.model.Paciente;

@FacesConverter("br.com.fono.converter.PacienteConverter")
public class PacienteConverter implements Converter {
	private PacienteDAO pacienteDAO = new PacienteDAO();
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if (value != null && value.length() > 0) {
			Paciente paciente = pacienteDAO.findByPK(value);
			return paciente;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj != null && obj.toString().length() > 0) {
			Paciente paciente = pacienteDAO.findByPK(obj.toString());
			return (paciente != null) ? paciente.getNome() : "Não cadastrado";
		}
		return "";
	}

}
