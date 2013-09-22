package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Alertas implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Alerta> list = new ArrayList<Alerta>();
	
	public List<Alerta> getList() {
		return list;
	}

	public void setList(List<Alerta> list) {
		this.list = list;
	}
}
