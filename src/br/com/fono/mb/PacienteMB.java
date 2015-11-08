package br.com.fono.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
		edit = true;
	}
	
	public void editar() {
		try {
			paciente = pacienteDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar paciente: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			pacienteDAO.save(paciente);
		} catch(Exception ex) {
			addMessage("Erro ao salvar paciente: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			pacientes = pacienteDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar pacientes: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			pacienteDAO.remove(paciente);
		} catch(Exception ex) {
			addMessage("Erro ao remover paciente: ", ex.getMessage());
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

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
	
}
