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

import org.apache.log4j.Logger;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.dao.ContadorDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.Contador;

@ManagedBean
@ViewScoped
public class ContadorMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ContadorMB.class);
	
	private ContadorDAO contadorDAO;
	
	private ClienteDAO clienteDAO;
	
	private Contador contador;
	
	private String idSelecionado;
	
	private List<Contador> contadores;
	
	private Map<String, Object> clienteSOM;
	
	private boolean edit;
	
	private Integer contadorRows;
	
	private Integer contadorPage;
	
	private List<String> contadorCursorList;
	
	@PostConstruct
	public void init() {
		contadorRows = 20;
		contadorCursorList = new ArrayList<String>();
		contadorDAO = new ContadorDAO();
		clienteDAO = new ClienteDAO();
		clienteSOM = new TreeMap<String, Object>();
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getId());
		}
		pagInicio();
	}
	
	public void incluir(){
		contador = new Contador();
		edit = true;
	}
	
	public void editar() {
		try {
			contador = contadorDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar contador: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			contadorDAO.save(contador);
		} catch(Exception ex) {
			addMessage("Erro ao salvar contador: ", ex.getMessage());
		}
		edit = false;
	}
	
	@SuppressWarnings("unchecked")
	public void atualizar() {
		try {
			Map<String, Object> result = contadorDAO.list(contadorCursorList.get(contadorPage), contadorRows);
			String c = (String) result.get("CURSOR");
			if (!contadorCursorList.contains(c)) {
				contadorCursorList.add(c);
			}
			contadores = (List<Contador>) result.get("RESULT");
		} catch(Exception ex) {
			ex.printStackTrace();
			addMessage("Erro ao listar contadores: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			contadorDAO.remove(contador);
		} catch(Exception ex) {
			addMessage("Erro ao remover contador: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void cancelar() {
		erase("formContadorEdit");
		edit = false;
	}
	
	public void pagInicio() {
		contadorCursorList.clear();
		contadorCursorList.add(null);
		contadorPage = 0;
		atualizar();
	}
	
	public void pagAnterior() {
		contadorPage = (contadorPage.equals(0)) ? 0 : contadorPage - 1;
		atualizar();
	}
	
	public void pagProximo() {
		contadorPage = (contadorRows.equals(contadores.size())) ? contadorPage + 1 : contadorPage;
		atualizar();
	}

	public Contador getContador() {
		return contador;
	}

	public void setContador(Contador contador) {
		this.contador = contador;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Contador> getContadores() {
		return contadores;
	}

	public void setContadores(List<Contador> contadores) {
		this.contadores = contadores;
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

	public Integer getContadorRows() {
		return contadorRows;
	}

	public Integer getContadorPage() {
		return contadorPage;
	}

	public List<String> getContadorCursorList() {
		return contadorCursorList;
	}

	public Integer getContadoresSize() {
		return contadores.size();
	}
	
	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
