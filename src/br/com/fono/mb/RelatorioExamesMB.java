package br.com.fono.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import br.com.fono.AbstractFacesBean;
import br.com.fono.dao.ClienteDAO;
import br.com.fono.dao.ExameDAO;
import br.com.fono.dao.PacienteDAO;
import br.com.fono.model.Cliente;
import br.com.fono.model.Exame;
import br.com.fono.model.Paciente;

@ManagedBean
@ViewScoped
public class RelatorioExamesMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RelatorioExamesMB.class);
	
	private ExameDAO exameDAO;
	
	private PacienteDAO pacienteDAO;
	
	private ClienteDAO clienteDAO;
	
	private Exame exame;
	
	private Long idSelecionado;
	
	private Date dataInicial;
	
	private Date dataFinal;
	
	private List<Exame> exames;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		exameDAO = new ExameDAO();
		pacienteDAO = new PacienteDAO();
		clienteDAO = new ClienteDAO();
		atualizar();
	}
	
	public void editar() {
		try {
			exame = exameDAO.findByPK(idSelecionado);
			
			Paciente p = pacienteDAO.findByPK(exame.getCpf());
			Cliente c = clienteDAO.findByPK(exame.getCnpj());
			exame.setPaciente(p);
			exame.setCliente(c);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			addMessage("Erro ao editar exame: ", ex.getMessage());
		}
		edit = true;
	}

	public void atualizar() {
		try {
			exames = exameDAO.listByDataValidade(dataInicial, dataFinal);
		} catch(Exception ex) {
			addMessage("Erro ao listar exames: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formExameEdit");
		edit = false;
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
	
	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
	
}
