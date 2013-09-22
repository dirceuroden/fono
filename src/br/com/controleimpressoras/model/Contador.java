package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Contador implements Serializable {
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
	private Long pagPreto;

	@Persistent
	private Long pagColor;

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

	public Long getPagPreto() {
		return pagPreto;
	}

	public void setPagPreto(Long pagPreto) {
		this.pagPreto = pagPreto;
	}

	public Long getPagColor() {
		return pagColor;
	}

	public void setPagColor(Long pagColor) {
		this.pagColor = pagColor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contador other = (Contador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
