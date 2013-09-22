package br.com.controleimpressoras.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.controleimpressoras.dao.TemplateSnmpDAO;
import br.com.controleimpressoras.model.TemplateSnmp;

@FacesConverter("br.com.controleimpressoras.converter.TemplateSnmpConverter")
public class TemplateConverter implements Converter {
	private TemplateSnmpDAO templateSnmpDAO = new TemplateSnmpDAO();
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj != null && obj.toString().length() > 0) {
			TemplateSnmp templateSnmp = templateSnmpDAO.findByPK(Long.valueOf(obj.toString()));
			return (templateSnmp != null) ? templateSnmp.getDescricao() : "Não cadastrado";
		}
		return "";
	}

}
