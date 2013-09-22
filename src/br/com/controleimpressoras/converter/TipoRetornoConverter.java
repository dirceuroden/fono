package br.com.controleimpressoras.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("br.com.controleimpressoras.converter.TipoRetornoConverter")
public class TipoRetornoConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		String value = "";
		if (obj.toString().equalsIgnoreCase("T")) {
			value = "Texto";
		} else if (obj.toString().equalsIgnoreCase("N")) {
			value = "Número";
		}
		return value;
	}

}
