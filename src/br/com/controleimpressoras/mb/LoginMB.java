package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.controleimpressoras.model.User;

@ManagedBean
@SessionScoped
public class LoginMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private User user;
	
	@PostConstruct
	public void init() {
		user = new User();
	}
	
	public void doLogin() {
		if (isLoggedIn()) {
			try {
				getCurrentInstance().getExternalContext().redirect("index.jsp");
			} catch (IOException ex) {
				addMessage("Erro ao redirecionar.", ex.getMessage());
			}
		} else {
			addMessage("Erro de validação.", "Login inválido, acesso negado.");
		}
	}
	
	public boolean isLoggedIn() {
		return (user.getLogin() != null && user.getLogin().equalsIgnoreCase("frohlich")) &&
				(user.getSenha() != null && user.getSenha().equalsIgnoreCase("cereais"));
	}
	
	public User getUser() {
		return user;
	}
	
	private void addMessage(String summary, String detail) {
		getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
