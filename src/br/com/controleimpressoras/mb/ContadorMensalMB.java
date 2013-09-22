package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.dao.ContadorMensalDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.ContadorMensal;
import br.com.controleimpressoras.model.Impressora;

@ManagedBean
@ViewScoped
public class ContadorMensalMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(ContadorMensalMB.class);
	
	private ContadorMensalDAO contadorMensalDAO;
	
	private ClienteDAO clienteDAO;
	
	private ContadorMensal contadorMensal;
	
	private String idSelecionado;
	
	private List<ContadorMensal> contadoresMensais;
	
	private Map<String, Object> clienteSOM;
	
	private boolean edit;
	
	private Integer contadorMensalRows;
	
	private Integer contadorMensalPage;
	
	private List<String> contadorMensalCursorList;
	
	@PostConstruct
	public void init() {
		contadorMensalRows = 20;
		contadorMensalCursorList = new ArrayList<String>();
		contadorMensalDAO = new ContadorMensalDAO();
		clienteDAO = new ClienteDAO();
		clienteSOM = new TreeMap<String, Object>();
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getId());
		}
		pagInicio();
	}
	
	public void incluir(){
		contadorMensal = new ContadorMensal();
		edit = true;
	}
	
	public void editar() {
		try {
			contadorMensal = contadorMensalDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar contador mensal: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			contadorMensalDAO.save(contadorMensal);
		} catch(Exception ex) {
			addMessage("Erro ao salvar contador mensal: ", ex.getMessage());
		}
		edit = false;
	}
	
	@SuppressWarnings("unchecked")
	public void atualizar() {
		try {
			Map<String, Object> result = contadorMensalDAO.list(contadorMensalCursorList.get(contadorMensalPage), contadorMensalRows);
			String c = (String) result.get("CURSOR");
			if (!contadorMensalCursorList.contains(c)) {
				contadorMensalCursorList.add(c);
			}
			contadoresMensais = (List<ContadorMensal>) result.get("RESULT");
		} catch(Exception ex) {
			ex.printStackTrace();
			addMessage("Erro ao listar contadores mensais: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			contadorMensalDAO.remove(contadorMensal);
		} catch(Exception ex) {
			addMessage("Erro ao remover contador mensal: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formContadorMensalEdit");
		edit = false;
	}
	
	public void pagInicio() {
		contadorMensalCursorList.clear();
		contadorMensalCursorList.add(null);
		contadorMensalPage = 0;
		atualizar();
	}
	
	public void pagAnterior() {
		contadorMensalPage = (contadorMensalPage.equals(0)) ? 0 : contadorMensalPage - 1;
		atualizar();
	}
	
	public void pagProximo() {
		contadorMensalPage = (contadorMensalRows.equals(contadoresMensais.size())) ? contadorMensalPage + 1 : contadorMensalPage;
		atualizar();
	}

	public ContadorMensal getContadorMensal() {
		return contadorMensal;
	}

	public void setContadorMensal(ContadorMensal contadorMensal) {
		this.contadorMensal = contadorMensal;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<ContadorMensal> getContadoresMensais() {
		return contadoresMensais;
	}

	public void setContadoresMensais(List<ContadorMensal> contadoresMensais) {
		this.contadoresMensais = contadoresMensais;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Map<String, Object> getClienteSOM() {
		return clienteSOM;
	}

	public Integer getContadorMensalRows() {
		return contadorMensalRows;
	}

	public Integer getContadorMensalPage() {
		return contadorMensalPage;
	}

	public List<String> getContadorMensalCursorList() {
		return contadorMensalCursorList;
	}

	public Integer getContadoresMensaisSize() {
		return contadoresMensais.size();
	}
	
	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
