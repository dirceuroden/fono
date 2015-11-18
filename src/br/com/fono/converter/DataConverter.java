package br.com.fono.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

@FacesConverter("br.com.fono.converter.DataConverter")
public class DataConverter implements Converter {
	private static Logger log = Logger.getLogger(DataConverter.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if (value == null || value.length() == 0) return null;
		Date data = null;
		try {
			data = sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Informe a data no formato DD/MM/AAAA.", "Informe a data no formato DD/MM/AAAA.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}
		return data;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj == null) return null;
		return sdf.format((Date) obj);
	}

}
