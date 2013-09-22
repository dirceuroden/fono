package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Leituras implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Leitura> list = new ArrayList<Leitura>();
	
	public List<Leitura> getList() {
		return list;
	}

	public void setList(List<Leitura> list) {
		this.list = list;
	}
}
