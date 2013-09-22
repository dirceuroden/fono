package br.com.controleimpressoras.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Impressora implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String id; // num serie

	@Persistent
	private Long template;

	@Persistent
	private String cliente;
	
	@Persistent
	private String ip;
	
	@Persistent
	private String descricao;
	
	@Persistent
	private Long pagPretoInicial;
	
	@Persistent
	private Long pagColorInicial;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getTemplate() {
		return template;
	}

	public void setTemplate(Long template) {
		this.template = template;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getPagPretoInicial() {
		return pagPretoInicial;
	}

	public void setPagPretoInicial(Long pagPretoInicial) {
		this.pagPretoInicial = pagPretoInicial;
	}

	public Long getPagColorInicial() {
		return pagColorInicial;
	}

	public void setPagColorInicial(Long pagColorInicial) {
		this.pagColorInicial = pagColorInicial;
	}
	
	
}
