package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.dao.LeituraDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.Impressora;
import br.com.controleimpressoras.model.Leitura;

@ManagedBean
@ViewScoped
public class StatusMB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StatusMB.class);

	private LeituraDAO leituraDAO;
	
	private ImpressoraDAO impressoraDAO;
	
	private Impressora impressora;
	
	private List<Leitura> leituras; 
	
	private List<Impressora> impressoras;
	
	private String idSelecionado;

	private String panel;

	private LocCliente locCliente;
	
	private Integer impressoraRows;
	
	private Integer impressoraPage;
	
	private List<String> impressoraCursorList;

	@PostConstruct
	public void init() {
		impressoraRows = 20;
		impressoraCursorList = new ArrayList<String>();
		leituraDAO = new LeituraDAO();
		impressoraDAO = new ImpressoraDAO();
		locCliente = new LocCliente();
		leituras = new ArrayList<Leitura>();
		impressoras = new ArrayList<Impressora>();
		impressora = new Impressora();
		pagInicio();
		panel = "locCliente";
	}

	public void consultar() {
		panel = "locCliente";
	}

	public void localizarCliente() {
		panel = "locCliente";
	}

	public void listarInfo() {
		impressora = impressoraDAO.findByPK(idSelecionado); 
		leituras = leituraDAO.listImpressora(idSelecionado);
		panel = "info";
	}
	
	public void voltar() {
		panel = "lista";
	}
	
	public void pagInicio() {
		impressoraCursorList.clear();
		impressoraCursorList.add(null);
		impressoraPage = 0;
		locCliente.selecionar();
	}
	
	public void pagAnterior() {
		impressoraPage = (impressoraPage.equals(0)) ? 0 : impressoraPage - 1;
		locCliente.selecionar();
	}
	
	public void pagProximo() {
		impressoraPage = (impressoraRows.equals(impressoras.size())) ? impressoraPage + 1 : impressoraPage;
		locCliente.selecionar();
	}

	public String getPanel() {
		return panel;
	}

	public LocCliente getLocCliente() {
		return locCliente;
	}

	public List<Impressora> getImpressoras() {
		return impressoras;
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

	public Impressora getImpressora() {
		return impressora;
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

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels",
				getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}

	private void addMessage(String summary, String detail) {
		// getCurrentInstance().addMessage(null, new FacesMessage(summary,
		// summary.concat("<br/>").concat(detail)));
	}

	public class LocCliente implements Serializable {
		private static final long serialVersionUID = 1L;

		private ClienteDAO clienteDAO;

		private String texto;

		private String idSelecionado;

		private List<Cliente> result;

		public LocCliente() {
			clienteDAO = new ClienteDAO();
			result = new ArrayList<Cliente>();
		}

		public void clear() {
			texto = null;
			result.clear();
		}

		@SuppressWarnings("unchecked")
		public void selecionar() {
			impressoras.clear();
			
			//impressoras = impressoraDAO.list(locCliente.getIdSelecionado());
			
			
			try {
				Map<String, Object> result = impressoraDAO.list(impressoraCursorList.get(impressoraPage), impressoraRows, locCliente.getIdSelecionado());
				String c = (String) result.get("CURSOR");
				if (!impressoraCursorList.contains(c)) {
					impressoraCursorList.add(c);
				}
				impressoras = (List<Impressora>) result.get("RESULT");
			} catch(Exception ex) {
				ex.printStackTrace();
				addMessage("Erro ao listar impressoras: ", ex.getMessage());
			}
			
			panel = "lista";
		}

		public void listar() {
			try {
				List<Cliente> all = clienteDAO.listAll();
				result.clear();
				for (Cliente c : all) {
					if (c.getNome().contains(texto)) {
						result.add(c);
					}
				}
			} catch (Exception ex) {
				addMessage("Erro ao listar clientes: ", ex.getMessage());
			}
		}

		public String getIdSelecionado() {
			return idSelecionado;
		}

		public void setIdSelecionado(String idSelecionado) {
			this.idSelecionado = idSelecionado;
		}

		public List<Cliente> getResult() {
			return result;
		}

		public void setResult(List<Cliente> result) {
			this.result = result;
		}

		public String getTexto() {
			return texto;
		}

		public void setTexto(String texto) {
			this.texto = texto;
		}
	}

}
