package br.com.controleimpressoras.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AgentData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String impressora;
	private String ip;
	private String linkContador;
	private String linkSerie;
	private Integer linhaPreto;
	private Integer colunaPreto;
	private Integer linhaColor;
	private Integer colunaColor;
	private String linhaSerie;
	private String colunaSerie;

	public String getImpressora() {
		return impressora;
	}

	public void setImpressora(String impressora) {
		this.impressora = impressora;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLinkContador() {
		return linkContador;
	}

	public void setLinkContador(String linkContador) {
		this.linkContador = linkContador;
	}

	public String getLinkSerie() {
		return linkSerie;
	}

	public void setLinkSerie(String linkSerie) {
		this.linkSerie = linkSerie;
	}

	public Integer getLinhaPreto() {
		return linhaPreto;
	}

	public void setLinhaPreto(Integer linhaPreto) {
		this.linhaPreto = linhaPreto;
	}

	public Integer getColunaPreto() {
		return colunaPreto;
	}

	public void setColunaPreto(Integer colunaPreto) {
		this.colunaPreto = colunaPreto;
	}

	public Integer getLinhaColor() {
		return linhaColor;
	}

	public void setLinhaColor(Integer linhaColor) {
		this.linhaColor = linhaColor;
	}

	public Integer getColunaColor() {
		return colunaColor;
	}

	public void setColunaColor(Integer colunaColor) {
		this.colunaColor = colunaColor;
	}

	public String getLinhaSerie() {
		return linhaSerie;
	}

	public void setLinhaSerie(String linhaSerie) {
		this.linhaSerie = linhaSerie;
	}

	public String getColunaSerie() {
		return colunaSerie;
	}

	public void setColunaSerie(String colunaSerie) {
		this.colunaSerie = colunaSerie;
	}

}
