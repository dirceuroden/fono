package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.ConfigImpressoraDAO;
import br.com.controleimpressoras.model.ConfigImpressora;

@ManagedBean
@ViewScoped
public class ConfigImpressoraMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(ConfigImpressoraMB.class);
	
	private ConfigImpressoraDAO configImpressoraDAO;
	
	private ConfigImpressora configImpressora;
	
	private Long idSelecionado;
	
	private List<ConfigImpressora> configImpressoras;
	
	private boolean edit;
	
	@PostConstruct
	public void init() {
		atualizar();
		configImpressoraDAO = new ConfigImpressoraDAO();
	}
	
	public void incluir(){
		configImpressora = new ConfigImpressora();
		edit = true;
	}
	
	public void editar() {
		try {
			configImpressora = configImpressoraDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar configuração de impressora: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			configImpressoraDAO.save(configImpressora);
		} catch(Exception ex) {
			addMessage("Erro ao salvar configuração de impressora: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void atualizar() {
		try {
			configImpressoras = configImpressoraDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar configuração de impressora: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			configImpressoraDAO.remove(configImpressora);
		} catch(Exception ex) {
			addMessage("Erro ao remover configuração de impressora: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formConfigImpressora");
		edit = false;
	}

	public ConfigImpressora getConfigImpressora() {
		return configImpressora;
	}

	public void setConfigImpressora(ConfigImpressora configImpressora) {
		this.configImpressora = configImpressora;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<ConfigImpressora> getConfigImpressoras() {
		return configImpressoras;
	}

	public void setConfigImpressoras(List<ConfigImpressora> configImpressoras) {
		this.configImpressoras = configImpressoras;
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
