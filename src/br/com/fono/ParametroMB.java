package br.com.fono;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ParametroMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private String menuAtivo;

	public String getMenuAtivo() {
		return menuAtivo;
	}

	public void setMenuAtivo(String menuAtivo) {
		this.menuAtivo = menuAtivo;
	}
}
