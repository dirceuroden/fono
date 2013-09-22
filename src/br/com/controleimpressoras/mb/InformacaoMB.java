package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.InformacaoDAO;
import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.Informacao;
import br.com.controleimpressoras.model.TipoInformacao;

@ManagedBean
@ViewScoped
public class InformacaoMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(InformacaoMB.class);
	
	private TipoInformacaoDAO tipoInformacaoDAO;
	
	private InformacaoDAO informacaoDAO;
	
	private Informacao informacao;
	
	private Long idSelecionado;
	
	private Long idTemplateSnmpSelecionado;
	
	private List<Informacao> informacoes;
	
	private Map<String, Object> tipoInformacaoSOM;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		informacaoDAO = new InformacaoDAO();
		tipoInformacaoDAO = new TipoInformacaoDAO();
		tipoInformacaoSOM = new TreeMap<String, Object>();
		for (TipoInformacao t : tipoInformacaoDAO.listAll()) {
			tipoInformacaoSOM.put(t.getDescricao(), t.getId());
		}
		atualizar();
	}
	
	public void incluir(){
		informacao = new Informacao();
		edit = true;
	}
	
	public void editar() {
		try {
			informacao = informacaoDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar informacao: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			informacao.setTemplate(idTemplateSnmpSelecionado);
			informacaoDAO.save(informacao);
		} catch(Exception ex) {
			addMessage("Erro ao salvar informacao: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			informacoes = informacaoDAO.list(idTemplateSnmpSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao listar informacoes: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			informacaoDAO.remove(informacao);
		} catch(Exception ex) {
			addMessage("Erro ao remover informacao: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formInformacaoEdit");
		edit = false;
	}

	public Informacao getInformacao() {
		return informacao;
	}

	public void setInformacao(Informacao informacao) {
		this.informacao = informacao;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Informacao> getInformacoes() {
		return informacoes;
	}

	public void setInformacoes(List<Informacao> informacoes) {
		this.informacoes = informacoes;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Long getIdTemplateSnmpSelecionado() {
		return idTemplateSnmpSelecionado;
	}

	public void setIdTemplateSnmpSelecionado(Long idTemplateSnmpSelecionado) {
		this.idTemplateSnmpSelecionado = idTemplateSnmpSelecionado;
	}

	public Map<String, Object> getTipoInformacaoSOM() {
		return tipoInformacaoSOM;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
