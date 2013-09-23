package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AgentSnmpData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String impressora;
	private String ip;
	private String template;
	private String serialOid;
	private String alertas;
	private List<Informacao> informacoes;
	private List<Informacao> statusInfo;

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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getSerialOid() {
		return serialOid;
	}

	public void setSerialOid(String serialOid) {
		this.serialOid = serialOid;
	}

	public String getAlertas() {
		return alertas;
	}

	public void setAlertas(String alertas) {
		this.alertas = alertas;
	}

	public List<Informacao> getInformacoes() {
		return informacoes;
	}

	public void setInformacoes(List<Informacao> informacoes) {
		this.informacoes = informacoes;
	}

	public List<Informacao> getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(List<Informacao> statusInfo) {
		this.statusInfo = statusInfo;
	}
}
