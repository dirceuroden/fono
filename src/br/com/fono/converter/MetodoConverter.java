package br.com.fono.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("br.com.fono.converter.MetodoConverter")
public class MetodoConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		String value = "";
		if (obj.toString().equalsIgnoreCase("A")) {
			value = "Via Aérea";
		} else if (obj.toString().equalsIgnoreCase("O")) {
			value = "Via Óssea";
		}
		return value;
	}

}
