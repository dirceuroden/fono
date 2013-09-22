package br.com.controleimpressoras.model;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ConfigImpressora implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String modelo;
	
	@Persistent
	private String fabricante;
	
	@Persistent
	private String versaoSoftware;

	@Persistent
	private String linkContador;

	@Persistent
	private String linkSerie;

	@Persistent
	private Integer linhaPreto;
	
	@Persistent
	private Integer colunaPreto;
	
	@Persistent
	private Integer linhaColor;
	
	@Persistent
	private Integer colunaColor;
	
	@Persistent
	private String linhaSerie;
	
	@Persistent
	private String colunaSerie;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getVersaoSoftware() {
		return versaoSoftware;
	}

	public void setVersaoSoftware(String versaoSoftware) {
		this.versaoSoftware = versaoSoftware;
	}

	public Integer getLinhaPreto() {
		return linhaPreto;
	}

	public void setLinhaPreto(Integer linhaPreto) {
		this.linhaPreto = linhaPreto;
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
