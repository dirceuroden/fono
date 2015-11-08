package br.com.fono.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.fono.AbstractFacesBean;
import br.com.fono.dao.UsuarioDAO;
import br.com.fono.model.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(UsuarioMB.class);
	
	private UsuarioDAO usuarioDAO;
	
	private Usuario usuario;
	
	private String idSelecionado;
	
	private List<Usuario> usuarios;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		atualizar();
	}
	
	public void incluir(){
		usuario = new Usuario();
		edit = true;
	}
	
	public void editar() {
		try {
			usuario = usuarioDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar usuario: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			usuarioDAO.save(usuario);
		} catch(Exception ex) {
			addMessage("Erro ao salvar usuario: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			usuarios = usuarioDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar usuarios: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			usuarioDAO.remove(usuario);
		} catch(Exception ex) {
			addMessage("Erro ao remover usuario: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formUsuarioEdit");
		edit = false;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
	
}
