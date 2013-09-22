package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@PersistenceCapable
public class Leitura implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String id;
	
	@Persistent
	private String cliente;
	
	@Persistent
	private String impressora;
	
	@Persistent
	private Date data;
	
	@Persistent
	private Long tipoInformacao;
	
	@Persistent
	private String valor;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getImpressora() {
		return impressora;
	}

	public void setImpressora(String impressora) {
		this.impressora = impressora;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getTipoInformacao() {
		return tipoInformacao;
	}

	public void setTipoInformacao(Long tipoInformacao) {
		this.tipoInformacao = tipoInformacao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
