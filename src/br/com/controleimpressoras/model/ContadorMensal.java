package br.com.controleimpressoras.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import br.com.controleimpressoras.config.Util;

@PersistenceCapable
public class ContadorMensal implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String id;

	@Persistent
	private String cliente;

	@Persistent
	private String impressora;

	@Persistent
	private Integer dia;
	
	@Persistent
	private Integer mes;
	
	@Persistent
	private Integer ano;
	
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

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
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
	
	public Date getData() {
		if (dia != null && mes != null && ano != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setTimeZone(Util.getTimeZone());
			DecimalFormat df = new DecimalFormat("00");
			String strData = df.format(dia) + "/" + df.format(mes) + "/" + df.format(ano);
			try {
				return sdf.parse(strData);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public void setData(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setTimeZone(Util.getTimeZone());
		String[] strData = sdf.format(data).split("-");
		dia = Integer.valueOf(strData[0]);
		mes = Integer.valueOf(strData[1]);
		ano = Integer.valueOf(strData[2]);
	}
}
