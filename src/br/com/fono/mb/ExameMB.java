package br.com.fono.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.fono.AbstractFacesBean;
import br.com.fono.config.Util;
import br.com.fono.dao.ClienteDAO;
import br.com.fono.dao.ExameDAO;
import br.com.fono.dao.PacienteDAO;
import br.com.fono.model.Cliente;
import br.com.fono.model.Exame;
import br.com.fono.model.Paciente;

@ManagedBean
@ViewScoped
public class ExameMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(ExameMB.class);
	
	private ExameDAO exameDAO;
	
	private ClienteDAO clienteDAO;
	
	private PacienteDAO pacienteDAO;
	
	private Exame exame;
	
	private Long idSelecionado;
	
	private List<Exame> exames;
	
	private Map<String, String> clienteSOM;
	
	private Map<String, String> pacienteSOM;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		exameDAO = new ExameDAO();
		clienteDAO = new ClienteDAO();
		pacienteDAO = new PacienteDAO();

		clienteSOM = new TreeMap<String, String>();
		clienteSOM.put("", null);
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getCnpj());
		}
		
		pacienteSOM = new TreeMap<String, String>();
		pacienteSOM.put("", null);
		for (Paciente p : pacienteDAO.listAll()) {
			pacienteSOM.put(p.getNome(), p.getCpf());
		}

		atualizar();
	}
	
	public void incluir(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(Util.getDataAtual());
		cal.add(Calendar.YEAR, 1);
		
		exame = new Exame();
		exame.setDataExame(Util.getDataAtual());
		edit = true;
	}
	
	public void editar() {
		try {
			exame = exameDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar exame: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			exameDAO.save(exame);
		} catch(Exception ex) {
			addMessage("Erro ao salvar exame: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			exames = exameDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar exames: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			exameDAO.remove(exame);
		} catch(Exception ex) {
			addMessage("Erro ao remover exame: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formExameEdit");
		edit = false;
	}

	public void atualizaCliente() {
		if (idSelecionado != null && exame.getIdExame() == null) {
			if (exame != null && exame.getCpf() != null) {
				Paciente p = pacienteDAO.findByPK(exame.getCpf());
				exame.setCnpj(p.getCnpj());
			}
		}
	}
	
	public void atualizaValidade() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(Util.getDataAtual());
		
		if (exame.getTipo().equals("A") || 
				exame.getTipo().equals("S")) {
			cal.add(Calendar.MONTH, 6);
			exame.setDataValidade(cal.getTime());
		} else if (exame.getTipo().equals("P") ||
				exame.getTipo().equals("T") ||
				exame.getTipo().equals("R")) {
			cal.add(Calendar.YEAR, 1);
			exame.setDataValidade(cal.getTime());
		} else {
			exame.setDataValidade(null);
		}
	}
	
	public Exame getExame() {
		return exame;
	}

	public void setExame(Exame exame) {
		this.exame = exame;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Exame> getExames() {
		return exames;
	}

	public void setExames(List<Exame> exames) {
		this.exames = exames;
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

	public Map<String, String> getPacienteSOM() {
		return pacienteSOM;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
	
}
