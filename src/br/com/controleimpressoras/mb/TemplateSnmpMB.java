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
import br.com.controleimpressoras.dao.TemplateSnmpDAO;
import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.TemplateSnmp;
import br.com.controleimpressoras.model.TipoInformacao;

@ManagedBean
@ViewScoped
public class TemplateSnmpMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(TemplateSnmpMB.class);
	
	private TemplateSnmpDAO templateSnmpDAO;
	
	private TemplateSnmp templateSnmp;
	
	private Long idSelecionado;
	
	private List<TemplateSnmp> templatesSnmp;
	
	private String panel;
	
	@PostConstruct
	public void init() {
		templateSnmpDAO = new TemplateSnmpDAO();
		atualizar();
	}
	
	public void incluir(){
		templateSnmp = new TemplateSnmp();
		InformacaoMB informacaoMB = (InformacaoMB) getBean("informacaoMB");
		if (informacaoMB != null) {
			informacaoMB.setIdSelecionado(null);
			informacaoMB.getInformacoes().clear();
		}
		panel = "detTemplate";
	}
	
	public void editar() {
		try {
			templateSnmp = templateSnmpDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar template SNMP: ", ex.getMessage());
		}
		InformacaoMB informacaoMB = (InformacaoMB) getBean("informacaoMB");
		if (informacaoMB != null) {
			informacaoMB.atualizar();
		}
		panel = "detTemplate";
	}

	public void salvar() {
		try {
			templateSnmpDAO.save(templateSnmp);
		} catch(Exception ex) {
			addMessage("Erro ao salvar template SNMP: ", ex.getMessage());
		}
		panel = "consulta";
	}
	
	public void atualizar() {
		try {
			templatesSnmp = templateSnmpDAO.listAll();
		} catch(Exception ex) {
			addMessage("Erro ao listar template SNMP: ", ex.getMessage());
		}
		panel = "consulta";
	}
	
	public void remover() {
		try {
			templateSnmpDAO.remove(templateSnmp);
		} catch(Exception ex) {
			addMessage("Erro ao remover template SNMP: ", ex.getMessage());
		}
		panel = "consulta";
	}
	
	public void cancelar() {
		erase("formTemplateSnmpEdit");
		panel = "consulta";
	}

	public TemplateSnmp getTemplateSnmp() {
		return templateSnmp;
	}

	public void setTemplateSnmp(TemplateSnmp templateSnmp) {
		this.templateSnmp = templateSnmp;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<TemplateSnmp> getTemplatesSnmp() {
		return templatesSnmp;
	}

	public void setTemplatesSnmp(List<TemplateSnmp> templatesSnmp) {
		this.templatesSnmp = templatesSnmp;
	}

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
