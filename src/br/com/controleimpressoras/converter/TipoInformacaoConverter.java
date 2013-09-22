package br.com.controleimpressoras.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.TipoInformacao;

@FacesConverter("br.com.controleimpressoras.converter.TipoInformacaoConverter")
public class TipoInformacaoConverter implements Converter {
	private TipoInformacaoDAO tipoInformacaoDAO = new TipoInformacaoDAO();
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj != null && obj.toString().length() > 0) {
			TipoInformacao ti = tipoInformacaoDAO.findByPK(Long.valueOf(obj.toString()));
			return (ti != null) ? ti.getDescricao() : "Não cadastrado";
		}
		return "";
	}

}
