package br.com.controleimpressoras.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("br.com.controleimpressoras.converter.TipoTipoInformacaoConverter")
public class TipoTipoInformacaoConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		String value = "";
		if (obj.toString().equalsIgnoreCase("P")) {
			value = "Contador Preto";
		} else if (obj.toString().equalsIgnoreCase("C")) {
			value = "Contador Color";
		} else if (obj.toString().equalsIgnoreCase("N")) {
			value = "Nro. Série";
		} else if (obj.toString().equalsIgnoreCase("S")) {
			value = "Status";
		}
		return value;
	}

}
