package br.com.fono.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.fono.AbstractFacesBean;
import br.com.fono.dao.ClienteDAO;
import br.com.fono.dao.PacienteDAO;
import br.com.fono.model.Cliente;
import br.com.fono.model.Paciente;

@ManagedBean
@ViewScoped
public class PacienteMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(PacienteMB.class);
	
	private PacienteDAO pacienteDAO;
	
	private ClienteDAO clienteDAO;
	
	private Paciente paciente;
	
	private String idSelecionado;
	
	private List<Paciente> pacientes;
	
	private Map<String, String> clienteSOM;
	
	private boolean edit;
	
	private boolean novo;
	
	@PostConstruct
	public void init() {
		pacienteDAO = new PacienteDAO();
		clienteDAO = new ClienteDAO();
		clienteSOM = new TreeMap<String, String>();
		clienteSOM.put("", null);
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getCnpj());
		}
		
		atualizar();
	}
	
	public void incluir(){
		paciente = new Paciente();
		novo = true;
		edit = true;
	}
	
	public void editar() {
		try {
			paciente = pacienteDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			error("Erro ao editar paciente: " + ex.getMessage());
		}
		novo = false;
		edit = true;
	}

	public void salvar() {
		try {
			if (novo) {
				Paciente p = pacienteDAO.findByPK(paciente.getCpf());
				if (p != null) {
					error("Erro ao salvar paciente: CPF já está cadastrado.");
					edit = true;
				} else {
					pacienteDAO.save(paciente);
					novo = false;
					edit = false;
				}
			} else {
				pacienteDAO.save(paciente);
				novo = false;
				edit = false;
			}
		} catch(Exception ex) {
			error("Erro ao salvar paciente: " + ex.getMessage());
		}
	}
	
	public void atualizar() {
		try {
			pacientes = pacienteDAO.listAll();
		} catch(Exception ex) {
			error("Erro ao listar pacientes: " + ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			pacienteDAO.remove(paciente);
		} catch(Exception ex) {
			error("Erro ao remover paciente: " + ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formPacienteEdit");
		edit = false;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	
	public Map<String, String> getClienteSOM() {
		return clienteSOM;
	}

}
