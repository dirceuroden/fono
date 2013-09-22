package br.com.controleimpressoras.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.model.Impressora;

@FacesConverter("br.com.controleimpressoras.converter.ImpressoraConverter")
public class ImpressoraConverter implements Converter {
	private ImpressoraDAO impressoraDAO = new ImpressoraDAO();
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj != null && obj.toString().length() > 0) {
			Impressora impressora = impressoraDAO.findByPK(obj.toString());
			return (impressora != null) ? impressora.getDescricao() : "Não cadastrada";
		}
		return "";
	}

}
