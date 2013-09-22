package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.TipoInformacao;

@ManagedBean
@ViewScoped
public class TipoInformacaoMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(TipoInformacaoMB.class);
	
	private TipoInformacaoDAO tipoInformacaoDAO;
	
	private TipoInformacao tipoInformacao;
	
	private Long idSelecionado;
	
	private List<TipoInformacao> tiposInformacao;
	
	private Map<String, Object> tipoSOM;
	
	private Map<String, Object> tipoRetornoSOM;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		tipoInformacaoDAO = new TipoInformacaoDAO();
		tipoSOM = new TreeMap<String, Object>();
		tipoSOM.put("", null);
		tipoSOM.put("Contador Color", "C");
		tipoSOM.put("Contador Preto", "P");
		tipoSOM.put("Nro. Série", "N");
		tipoSOM.put("Status", "S");
		tipoRetornoSOM = new TreeMap<String, Object>();
		tipoRetornoSOM.put("Texto", "T");
		tipoRetornoSOM.put("Número", "N");
		atualizar();
	}
	
	public void incluir(){
		tipoInformacao = new TipoInformacao();
		edit = true;
	}
	
	public void editar() {
		try {
			tipoInformacao = tipoInformacaoDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar tipo de informação: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			tipoInformacaoDAO.save(tipoInformacao);
		} catch(Exception ex) {
			addMessage("Erro ao salvar tipo de informação: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			tiposInformacao = tipoInformacaoDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar tipos de informação: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			tipoInformacaoDAO.remove(tipoInformacao);
		} catch(Exception ex) {
			addMessage("Erro ao remover tipo de informação: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formTipoInformacao");
		edit = false;
	}

	public TipoInformacao getTipoInformacao() {
		return tipoInformacao;
	}

	public void setTipoInformacao(TipoInformacao tipoInformacao) {
		this.tipoInformacao = tipoInformacao;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<TipoInformacao> getTiposInformacao() {
		return tiposInformacao;
	}

	public void setTiposInformacao(List<TipoInformacao> tipoInformacaos) {
		this.tiposInformacao = tipoInformacaos;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Map<String, Object> getTipoSOM() {
		return tipoSOM;
	}

	public Map<String, Object> getTipoRetornoSOM() {
		return tipoRetornoSOM;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
