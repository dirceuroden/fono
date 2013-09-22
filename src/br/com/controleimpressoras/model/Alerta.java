package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@PersistenceCapable
public class Alerta implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String id; //impressora
	
	@Persistent
	private Integer tipo; // 1 = Alerta de status
	                      // 2 = Falha de comunicação
	                      // 9 = Exclui alerta
	
	@Persistent
	private String cliente;
	
	@Persistent
	private String descricao;
	
	@Persistent
	private Date data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
}
