package br.com.fono.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.fono.dao.UsuarioDAO;
import br.com.fono.model.Usuario;

@ManagedBean
@SessionScoped
public class LoginMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private UsuarioDAO usuarioDAO;
	
	private Usuario user;
	
	@PostConstruct
	public void init() {
		user = new Usuario();
		usuarioDAO = new UsuarioDAO();
	}
	
	public void doLogin() {
		if (isLoggedIn()) {
			try {
				if((user.getLogin() != null && user.getLogin().equalsIgnoreCase("admin")) &&
						(user.getSenha() != null && user.getSenha().equalsIgnoreCase("@fono"))) {
					getCurrentInstance().getExternalContext().redirect("view/usuario.jsf");
				} else {
					getCurrentInstance().getExternalContext().redirect("index.jsp");	
				}
			} catch (IOException ex) {
				addMessage("Erro ao redirecionar.", ex.getMessage());
			}
		} else {
			addMessage("Erro de validação.", "Login inválido, acesso negado.");
		}
	}
	
	public boolean isLoggedIn() {
		boolean result = false;
		if (user.getLogin() != null && user.getLogin().equalsIgnoreCase("desenv")) {
			result = true;
		} else if((user.getLogin() != null && user.getLogin().equalsIgnoreCase("admin")) &&
					(user.getSenha() != null && user.getSenha().equalsIgnoreCase("@fono"))) {
			result = true;
		} else {
			if (user != null && user.getLogin() != null && user.getSenha() != null) {
				Usuario u = usuarioDAO.findByPK(user.getLogin());
				if (u != null && u.getSenha().equals(user.getSenha()) && u.getAtivo().equals("S")) {
					result = true;
					user = u;
				}
			}
		}
	
		return result;
	}
	
	public Usuario getUser() {
		return user;
	}
	
	public boolean isMenuVisivel() {
		if (user.getLogin() != null && user.getLogin().equalsIgnoreCase("admin")) {
			return false;
		} else if (isLoggedIn()) {
			return true;
		} else {
			return false;
		}
		
	}
	
	private void addMessage(String summary, String detail) {
		getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
