package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.config.Util;
import br.com.controleimpressoras.dao.HistoricoAlertaDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.model.HistoricoAlerta;
import br.com.controleimpressoras.model.Impressora;

@ManagedBean
@ViewScoped
public class HistoricoAlertaMB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(HistoricoAlertaMB.class);

	private HistoricoAlertaDAO historicoAlertaDAO;
	
	private ImpressoraDAO impressoraDAO;

	private List<Result> result;

	private String panel;

	private Consulta consulta;
	
	private List<HistoricoAlerta> historicoAlertas;
	
	private List<HistoricoAlerta> historicoAlertasImpressora;
	
	private String idSelecionado;

	@PostConstruct
	public void init() {
		impressoraDAO = new ImpressoraDAO();
		historicoAlertaDAO = new HistoricoAlertaDAO();
		consulta = new Consulta();
		result = new ArrayList<Result>();
		historicoAlertas = new ArrayList<HistoricoAlerta>();
		historicoAlertasImpressora = new ArrayList<HistoricoAlerta>();
		panel = "consulta";
	}

	public void consultar() {
		panel = "consulta";
	}

	public void localizarCliente() {
		panel = "locCliente";
	}

	public void listar() {
		result.clear();
		
		//
		/*
		Calendar cal = Calendar.getInstance(Util.getTimeZone());
		cal.setTime(Util.getDataAtual());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		
				
		HistoricoAlerta ha1 = new HistoricoAlerta();
		ha1.setCliente("12345678998778");
		ha1.setImpressora("LI940HK787Y0");
		ha1.setData(Util.trunc(cal.getTime()));
		ha1.setStatus("Erro 1");
		
		HistoricoAlerta ha2 = new HistoricoAlerta();
		ha2.setCliente("12345678998778");
		ha2.setImpressora("LI940HK787Y0");
		ha2.setData(Util.trunc(cal.getTime()));
		ha2.setStatus("Erro 2");

		HistoricoAlerta ha3 = new HistoricoAlerta();
		ha3.setCliente("12345678998778");
		ha3.setImpressora("LI940HK787Y0");
		ha3.setData(Util.trunc(cal.getTime()));
		ha3.setStatus("Erro 3");

		HistoricoAlerta ha4 = new HistoricoAlerta();
		ha4.setCliente("12345678998778");
		ha4.setImpressora("TG39KPOY74E");
		ha4.setData(Util.trunc(cal.getTime()));
		ha4.setStatus("Erro 4");

		HistoricoAlerta ha5 = new HistoricoAlerta();
		ha5.setCliente("12345678998778");
		ha5.setImpressora("TG39KPOY74E");
		ha5.setData(Util.trunc(cal.getTime()));
		ha5.setStatus("Erro 5");

		historicoAlertaDAO.save(ha1);
		historicoAlertaDAO.save(ha2);
		historicoAlertaDAO.save(ha3);
		historicoAlertaDAO.save(ha4);
		historicoAlertaDAO.save(ha5);
	
		*/
		
		
		
		
		
		
		
		
		
		historicoAlertas = historicoAlertaDAO.list(consulta.getIdImpressora(), consulta.getDataInicial(), consulta.getDataFinal());
		
		
		Map<String, Integer> contadorAlerta = new HashMap<String, Integer>();
		
		for (HistoricoAlerta ha : historicoAlertas) {
			String key = ha.getCliente() + "#" + ha.getImpressora();
			if (contadorAlerta.containsKey(key)) {
				contadorAlerta.put(key, contadorAlerta.get(key) + 1);
			} else {
				contadorAlerta.put(key, 1);
			}
		}
		
		for (String key : contadorAlerta.keySet()) {
			String[] keyArray = key.split("#");
			String idCliente = keyArray[0];
			String idImpressora = keyArray[1];
			
			Impressora impressora = impressoraDAO.findByPK(idImpressora);
			
			Result r = new Result();
			r.setCliente(idCliente);
			r.setImpressora(idImpressora);
			r.setTemplate(impressora.getTemplate());
			r.setQuantidade(contadorAlerta.get(key));
			result.add(r);
		}
		
		Collections.sort(result, new Comparator<Result>() {
			@Override
			public int compare(Result r1, Result r2) {
				return r2.getQuantidade() - r1.getQuantidade();
			}
		});
		
		
		consulta.clear();
		panel = "lista";
	}
	
	public void listarHistoricoAlertasImpressora() {
		historicoAlertasImpressora.clear();
		for (HistoricoAlerta ha : historicoAlertas) {
			if (ha.getImpressora().equals(idSelecionado)) {
				historicoAlertasImpressora.add(ha);
			}
		}
		
		Collections.sort(historicoAlertasImpressora, new Comparator<HistoricoAlerta>() {
			@Override
			public int compare(HistoricoAlerta ha1, HistoricoAlerta ha2) {
				return ha2.getData().compareTo(ha1.getData());
			}
		});
		
		panel = "historico";
	}

	public void voltar() {
		panel = "lista";
	}
	
	public String getPanel() {
		return panel;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public List<Result> getResult() {
		return result;
	}
	
	public List<HistoricoAlerta> getHistoricoAlertasImpressora() {
		return historicoAlertasImpressora;
	}

	public String getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(String idSelecionado) {
		this.idSelecionado = idSelecionado;
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

	public class Consulta implements Serializable {
		private static final long serialVersionUID = 1L;

		private String idImpressora;
		private Date dataInicial;
		private Date dataFinal;

		public void clear() {
			idImpressora = null;
			dataInicial = null;
			dataFinal = null;
		}

		public String getIdImpressora() {
			return idImpressora;
		}

		public void setIdImpressora(String idImpressora) {
			this.idImpressora = idImpressora;
		}

		public Date getDataInicial() {
			return dataInicial;
		}

		public void setDataInicial(Date dataInicial) {
			this.dataInicial = Util.trunc(dataInicial);
		}

		public Date getDataFinal() {
			return dataFinal;
		}

		public void setDataFinal(Date dataFinal) {
			this.dataFinal = Util.trunc(dataFinal);
		}

	}

	public class Result implements Serializable {
		private static final long serialVersionUID = 1L;

		private String cliente;
		private String impressora;
		private Long template;
		private Integer quantidade;

		public String getCliente() {
			return cliente;
		}

		public void setCliente(String cliente) {
			this.cliente = cliente;
		}

		public String getImpressora() {
			return impressora;
		}

		public void setImpressora(String impressora) {
			this.impressora = impressora;
		}

		public Long getTemplate() {
			return template;
		}

		public void setTemplate(Long template) {
			this.template = template;
		}

		public Integer getQuantidade() {
			return quantidade;
		}

		public void setQuantidade(Integer quantidade) {
			this.quantidade = quantidade;
		}
	}
}
