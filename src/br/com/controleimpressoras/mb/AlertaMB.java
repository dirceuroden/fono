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

import br.com.controleimpressoras.dao.AlertaDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.dao.LeituraDAO;
import br.com.controleimpressoras.model.Alerta;
import br.com.controleimpressoras.model.Impressora;
import br.com.controleimpressoras.model.Leitura;

@ManagedBean
@ViewScoped
public class AlertaMB implements Serializable {
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(AlertaMB.class);
	
	private LeituraDAO leituraDAO;
	
	private AlertaDAO alertaDAO;
	
	private ImpressoraDAO impressoraDAO;
	
	private Alerta alerta;
	
	private List<Leitura> leituras; 
	
	private String idSelecionado;
	
	private Impressora impressora;
	
	private List<Alerta> alertas;
	
	private List<Alerta> alertasTipo1;
	
	private List<Alerta> alertasTipo2;
	
	private String panel;
	
	private Integer alertaRows;
	
	private Integer alertaPage;
	
	private List<String> alertaCursorList;
	
	private Integer tipoAlerta;

	@PostConstruct
	public void init() {
		alertaRows = 20;
		alertaCursorList = new ArrayList<String>();
		alertaDAO = new AlertaDAO();
		leituraDAO = new LeituraDAO();
		impressoraDAO = new ImpressoraDAO();
		impressora = new Impressora();
		atualizar();
		panel = "painel";
	}
	
	@SuppressWarnings("unchecked")
	public void atualizar() {
		try {
			//Map<String, Object> result = alertaDAO.list(alertaCursorList.get(alertaPage), alertaRows);
			Map<String, Object> result = alertaDAO.list(null, 0);
			//String c = (String) result.get("CURSOR");
			//if (!alertaCursorList.contains(c)) {
			//	alertaCursorList.add(c);
			//}
			//alertas = (List<Alerta>) result.get("RESULT");
			alertas = new ArrayList<Alerta>();
			alertasTipo1 = (List<Alerta>) result.get("ALERTAS");
			alertasTipo2 = (List<Alerta>) result.get("FALHAS");
		} catch(Exception ex) {
			ex.printStackTrace();
			addMessage("Erro ao listar alertas: ", ex.getMessage());
		}
		panel = "alerta";
	}
	
	public void remover() {
		try {
			alertaDAO.remove(alerta);
		} catch(Exception ex) {
			addMessage("Erro ao remover alerta: ", ex.getMessage());
		}
		panel = "alerta";
	}
	
	public void listarInfo() {
		impressora = impressoraDAO.findByPK(idSelecionado); 
		leituras = leituraDAO.listImpressora(idSelecionado);
		panel = "info";
	}
	
	public void voltar() {
		panel = "alerta";
	}
	
	public void pagInicio() {
		alertaCursorList.clear();
		alertaCursorList.add(null);
		alertaPage = 0;
		atualizar();
	}
	
	public void pagAnterior() {
		alertaPage = (alertaPage.equals(0)) ? 0 : alertaPage - 1;
		atualizar();
	}
	
	public void pagProximo() {
		alertaPage = (alertaRows.equals(alertas.size())) ? alertaPage + 1 : alertaPage;
		atualizar();
	}

	public void mostrarAlertas() {
		tipoAlerta = 1;
		pagInicio();
		alertas = alertasTipo1;
	}
	
	public void mostrarFalhas() {
		tipoAlerta = 2;
		pagInicio();
		alertas = alertasTipo2;
	}
	
	public void atualizarPainel() {
		panel = "painel";
	}
	
	public Alerta getAlerta() {
		return alerta;
	}

	public void setAlerta(Alerta alerta) {
		this.alerta = alerta;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public List<Alerta> getAlertas() {
		return alertas;
	}

	public void setAlertas(List<Alerta> alertas) {
		this.alertas = alertas;
	}

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}

	public List<Leitura> getLeituras() {
		return leituras;
	}

	public Impressora getImpressora() {
		return impressora;
	}

	public Integer getAlertaRows() {
		return alertaRows;
	}

	public Integer getAlertaPage() {
		return alertaPage;
	}

	public Integer getAlertasSize() {
		return alertas.size();
	}
	
	public Integer getAlertasTipo1Size() {
		return alertasTipo1.size();
	}

	public Integer getAlertasTipo2Size() {
		return alertasTipo2.size();
	}

	public Integer getTipoAlerta() {
		return tipoAlerta;
	}

	public void setTipoAlerta(Integer tipoAlerta) {
		this.tipoAlerta = tipoAlerta;
	}

	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	private void addMessage(String summary, String detail) {
		//getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
