package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.dao.TemplateSnmpDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.Impressora;
import br.com.controleimpressoras.model.TemplateSnmp;

@ManagedBean
@ViewScoped
public class ImpressoraMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(ImpressoraMB.class);
	
	private ImpressoraDAO impressoraDAO;
	
	private TemplateSnmpDAO templateSnmpDAO;
	
	private ClienteDAO clienteDAO;
	
	private Impressora impressora;
	
	private String idSelecionado;
	
	private List<Impressora> impressoras;
	
	private String panel;
	
	private Map<String, Object> templateSOM;
	
	private Map<String, Object> clienteSOM;
	
	private Integer impressoraRows;
	
	private Integer impressoraPage;
	
	private List<String> impressoraCursorList;
	
	private Consulta consulta;
	
	@PostConstruct
	public void init() {
		consulta = new Consulta();
		impressoras = new ArrayList<Impressora>();
		impressoraRows = 20;
		impressoraCursorList = new ArrayList<String>();
		impressoraDAO = new ImpressoraDAO();
		templateSnmpDAO = new TemplateSnmpDAO();
		clienteDAO = new ClienteDAO();
		templateSOM = new TreeMap<String, Object>();
		clienteSOM = new TreeMap<String, Object>();
		for (TemplateSnmp t : templateSnmpDAO.listAll()) {
			templateSOM.put(t.getDescricao(), t.getId());
		}
		clienteSOM.put("", null);
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getId());
		}
		panel = "consultar";
	}
	
	public void incluir(){
		impressora = new Impressora();
		panel = "editar";
	}
	
	public void editar() {
		try {
			impressora = impressoraDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar impressora: ", ex.getMessage());
		}
		panel = "editar";
	}

	public void salvar() {
		try {
			impressoraDAO.save(impressora);
		} catch(Exception ex) {
			addMessage("Erro ao salvar impressora: ", ex.getMessage());
		}
		panel = "listar";
	}
	
	@SuppressWarnings("unchecked")
	public void atualizar() {
		try {
			if (consulta.getIdImpressora() != null && consulta.getIdImpressora().trim().length() > 0) {
				impressoras.clear();
				Impressora imp = impressoraDAO.findByPK(consulta.getIdImpressora());
				if (imp != null) {
					impressoras.add(imp);
				}
			} else {
				Map<String, Object> result = impressoraDAO.list(impressoraCursorList.get(impressoraPage), impressoraRows, consulta.getIdCliente());
				String c = (String) result.get("CURSOR");
				if (!impressoraCursorList.contains(c)) {
					impressoraCursorList.add(c);
				}
				impressoras = (List<Impressora>) result.get("RESULT");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			addMessage("Erro ao listar impressoras: ", ex.getMessage());
		}
		panel = "listar";
	}
	
	public void remover() {
		try {
			impressoraDAO.remove(impressora);
		} catch(Exception ex) {
			addMessage("Erro ao remover impressora: ", ex.getMessage());
		}
		pagInicio();
		panel = "listar";
	}
	
	public void cancelar() {
		erase("formLeituraEdit");
		panel = "listar";
	}
	
	public void listar() {
		pagInicio();
	}

	public void consultar() {
		panel = "consultar";
	}

	public void pagInicio() {
		impressoraCursorList.clear();
		impressoraCursorList.add(null);
		impressoraPage = 0;
		atualizar();
	}
	
	public void pagAnterior() {
		impressoraPage = (impressoraPage.equals(0)) ? 0 : impressoraPage - 1;
		atualizar();
	}
	
	public void pagProximo() {
		impressoraPage = (impressoraRows.equals(impressoras.size())) ? impressoraPage + 1 : impressoraPage;
		atualizar();
	}

	public Impressora getImpressora() {
		return impressora;
	}

	public void setImpressora(Impressora impressora) {
		this.impressora = impressora;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Impressora> getImpressoras() {
		return impressoras;
	}

	public void setImpressoras(List<Impressora> impressoras) {
		this.impressoras = impressoras;
	}

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}

	public Map<String, Object> getTemplateSOM() {
		return templateSOM;
	}

	public Map<String, Object> getClienteSOM() {
		return clienteSOM;
	}
	
	public Integer getImpressorasSize() {
		return impressoras.size();
	}
	
	public Integer getImpressoraRows() {
		return impressoraRows;
	}

	public Integer getImpressoraPage() {
		return impressoraPage;
	}
	
	public Consulta getConsulta() {
		return consulta;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
	
	public class Consulta implements Serializable {
		private static final long serialVersionUID = 1L;

		private String idImpressora;
		private String idCliente;

		public String getIdImpressora() {
			return idImpressora;
		}

		public void setIdImpressora(String idImpressora) {
			this.idImpressora = idImpressora;
		}

		public String getIdCliente() {
			return idCliente;
		}

		public void setIdCliente(String idCliente) {
			this.idCliente = idCliente;
		}
	}
}
