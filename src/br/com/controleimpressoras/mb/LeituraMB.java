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
import br.com.controleimpressoras.dao.LeituraDAO;
import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.Leitura;
import br.com.controleimpressoras.model.TipoInformacao;

@ManagedBean
@ViewScoped
public class LeituraMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LeituraMB.class);
	
	private LeituraDAO leituraDAO;
	
	private ClienteDAO clienteDAO;
	
	private TipoInformacaoDAO tipoInformacaoDAO;
	
	private Leitura leitura;
	
	private String idSelecionado;
	
	private List<Leitura> leituras;
	
	private Map<String, Object> clienteSOM;
	
	private Map<String, Object> tipoInformacaoSOM;
	
	private boolean edit;
	
	private Integer leituraRows;
	
	private Integer leituraPage;
	
	private List<String> leituraCursorList;
	
	@PostConstruct
	public void init() {
		leituraRows = 20;
		leituraCursorList = new ArrayList<String>();
		leituraDAO = new LeituraDAO();
		tipoInformacaoDAO = new TipoInformacaoDAO();
		clienteDAO = new ClienteDAO();
		clienteSOM = new TreeMap<String, Object>();
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getId());
		}
		tipoInformacaoDAO = new TipoInformacaoDAO();
		tipoInformacaoSOM = new TreeMap<String, Object>();
		for (TipoInformacao t : tipoInformacaoDAO.listAll()) {
			tipoInformacaoSOM.put(t.getDescricao(), t.getId());
		}
		pagInicio();
	}
	
	public void incluir(){
		leitura = new Leitura();
		edit = true;
	}
	
	public void editar() {
		try {
			leitura = leituraDAO.findByPK(idSelecionado);
		} catch(Exception ex) {
			addMessage("Erro ao editar leitura: ", ex.getMessage());
		}
		edit = true;
	}

	public void salvar() {
		try {
			leituraDAO.save(leitura);
		} catch(Exception ex) {
			addMessage("Erro ao salvar leitura: ", ex.getMessage());
		}
		edit = false;
	}
	
	@SuppressWarnings("unchecked")
	public void atualizar() {
		try {
			Map<String, Object> result = leituraDAO.list(leituraCursorList.get(leituraPage), leituraRows);
			String c = (String) result.get("CURSOR");
			if (!leituraCursorList.contains(c)) {
				leituraCursorList.add(c);
			}
			leituras = (List<Leitura>) result.get("RESULT");
		} catch(Exception ex) {
			addMessage("Erro ao listar leituras: ", ex.getMessage());
		}
		edit = false;
	}
	
	public void remover() {
		try {
			leituraDAO.remove(leitura);
		} catch(Exception ex) {
			addMessage("Erro ao remover leitura: ", ex.getMessage());
		}
		pagInicio();
		edit = false;
	}
	
	public void cancelar() {
		erase("formLeituraEdit");
		edit = false;
	}

	public void pagInicio() {
		leituraCursorList.clear();
		leituraCursorList.add(null);
		leituraPage = 0;
		atualizar();
	}
	
	public void pagAnterior() {
		leituraPage = (leituraPage.equals(0)) ? 0 : leituraPage - 1;
		atualizar();
	}
	
	public void pagProximo() {
		leituraPage = (leituraRows.equals(leituras.size())) ? leituraPage + 1 : leituraPage;
		atualizar();
	}
	
	public Leitura getLeitura() {
		return leitura;
	}

	public void setLeitura(Leitura leitura) {
		this.leitura = leitura;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Leitura> getLeituras() {
		return leituras;
	}

	public void setLeituras(List<Leitura> leituras) {
		this.leituras = leituras;
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

	public Map<String, Object> getTipoInformacaoSOM() {
		return tipoInformacaoSOM;
	}

	public Integer getLeituraRows() {
		return leituraRows;
	}
	
	public int getLeiturasSize() {
		return leituras.size();
	}

	public Integer getLeituraPage() {
		return leituraPage;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
