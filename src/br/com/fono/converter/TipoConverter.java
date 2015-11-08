package br.com.fono.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("br.com.fono.converter.TipoConverter")
public class TipoConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		String value = "";
		if (obj.toString().equalsIgnoreCase("A")) {
			value = "Admissional";
		} else if (obj.toString().equalsIgnoreCase("P")) {
			value = "Periódico";
		} else if (obj.toString().equalsIgnoreCase("D")) {
			value = "Demissional";
		}
		return value;
	}

}
