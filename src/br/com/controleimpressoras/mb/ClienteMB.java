package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.model.Cliente;

@ManagedBean
@ViewScoped
public class ClienteMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(ClienteMB.class);
	
	private ClienteDAO clienteDAO;
	
	private Cliente cliente;
	
	private String idSelecionado;
	
	private List<Cliente> clientes;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		clienteDAO = new ClienteDAO();
		atualizar();
	}
	
	public void incluir(){
		cliente = new Cliente();
		edit = true;
	}
	
	public void editar() {
		try {
			cliente = clienteDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar cliente: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			clienteDAO.save(cliente);
		} catch(Exception ex) {
			addMessage("Erro ao salvar cliente: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			clientes = clienteDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar clientes: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			clienteDAO.remove(cliente);
		} catch(Exception ex) {
			addMessage("Erro ao remover cliente: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formClienteEdit");
		edit = false;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
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
