package br.com.fono.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.fono.AbstractFacesBean;
import br.com.fono.dao.ClienteDAO;
import br.com.fono.model.Cliente;

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
	
	private boolean novo;
	
	@PostConstruct
	public void init() {
		clienteDAO = new ClienteDAO();
		atualizar();
	}
	
	public void incluir(){
		cliente = new Cliente();
		novo = true;
		edit = true;
	}
	
	public void editar() {
		try {
			cliente = clienteDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			error("Erro ao editar cliente: " + ex.getMessage());
		}
		novo = false;
		edit = true;
	}

	public void salvar() {
		try {
			if (novo) {
				Cliente c = clienteDAO.findByPK(cliente.getCnpj());
				if (c != null) {
					error("Erro ao salvar paciente: CNPJ já está cadastrado.");
					edit = true;
				} else {
					clienteDAO.save(cliente);
					novo = false;
					edit = false;
				}
			} else {
				clienteDAO.save(cliente);
				novo = false;
				edit = false;
			}
		} catch(Exception ex) {
			error("Erro ao salvar cliente: " + ex.getMessage());
		}
	}
	
	public void atualizar() {
		try {
			clientes = clienteDAO.listAll();
		} catch(Exception ex) {
			error("Erro ao listar clientes: " + ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			clienteDAO.remove(cliente);
		} catch(Exception ex) {
			error("Erro ao remover cliente: " + ex.getMessage());
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
}
