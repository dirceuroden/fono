package br.com.fono.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

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
	
	private SimpleDateFormat sdf;
	
	@PostConstruct
	public void init() {
		exameDAO = new ExameDAO();
		pacienteDAO = new PacienteDAO();
		clienteDAO = new ClienteDAO();
		sdf = new SimpleDateFormat("dd/MM/yyyy");
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
	
	public void download() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"CPF Paciente\";\"Nome paciente\";\"Sexo\";\"Cliente\";\"Cidade\";\"Metodo\";\"Tipo\";\"Fone Paciente\";\"E-mail Paciente\";\"Data Exame\";\"Data Validade\";\"Valor\"\n");
		
		for (Exame e : exames) {
			Paciente p = pacienteDAO.findByPK(e.getCpf());
			Cliente c = clienteDAO.findByPK(e.getCnpj());
			
			sb.append("\"").append(e.getCpf()).append("\";");
			sb.append("\"").append(p.getNome()).append("\";");
			sb.append("\"").append(p.getSexo()).append("\";");
			sb.append("\"").append(c.getNome()).append("\";");
			sb.append("\"").append(c.getCidade()).append("\";");
			sb.append("\"").append(e.getMetodo()).append("\";");
			sb.append("\"").append(e.getTipo()).append("\";");
			sb.append("\"").append(p.getFone()).append("\";");
			sb.append("\"").append(p.getEmail()).append("\";");
			sb.append("\"").append(sdf.format(e.getDataExame())).append("\";");
			sb.append("\"").append(sdf.format(e.getDataValidade())).append("\";");
			sb.append("\"").append(e.getValor()).append("\"\n");
		}
		
		try {
	        String filename = "dados.csv";

	        FacesContext fc = getFacesContext();
	        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

	        response.reset();
	        response.setContentType("text/comma-separated-values");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

	        OutputStream output = response.getOutputStream();
	        output.write(sb.toString().getBytes());
	        output.flush();
	        output.close();

	        fc.responseComplete();
	    } catch (IOException e) {
	    	e.printStackTrace();
			addMessage("Erro ao gerar arquivo: ", e.getMessage());
	    }
	}
	
	public boolean getExibeBotaoDownload() {
		return !exames.isEmpty();
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
