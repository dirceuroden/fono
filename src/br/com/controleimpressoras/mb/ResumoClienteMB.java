package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.config.Util;
import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.dao.ContadorDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.Contador;
import br.com.controleimpressoras.model.Impressora;

@ManagedBean
@ViewScoped
public class ResumoClienteMB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LeituraMB.class);

	private ContadorDAO contadorDAO;
	
	private ImpressoraDAO impressoraDAO;

	private List<Result> result;

	private int pagPreto;

	private int pagColor;

	private String panel;

	private Consulta consulta;

	private LocCliente locCliente;

	@PostConstruct
	public void init() {
		impressoraDAO = new ImpressoraDAO();
		contadorDAO = new ContadorDAO();
		consulta = new Consulta();
		locCliente = new LocCliente();
		result = new ArrayList<Result>();
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
		
		
		Map<String, Long> anteriorPretoMap = new HashMap<String, Long>();
		Map<String, Long> anteriorColorMap = new HashMap<String, Long>();
		Map<String, Long> somaPretoMap = new HashMap<String, Long>();
		Map<String, Long> somaColorMap = new HashMap<String, Long>();
		Map<String, Date> dataMap = new HashMap<String, Date>();
		
		
		Calendar cal = Calendar.getInstance(Util.getTimeZone());
		cal.setTime(consulta.getDataInicial());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		List<Contador> all = new ArrayList<Contador>();
		all.addAll(contadorDAO.list(consulta.getIdCliente(), consulta.getDataInicial(), consulta.getDataFinal()));
		
		Set<String> distinctImpressoras = new HashSet<String>();
		for (Contador c : all) {
			distinctImpressoras.add(c.getImpressora());
		}
		
		for (String idImpressora : distinctImpressoras) {
			Set<String> clientes = contadorDAO.getClientes(consulta.getIdCliente(), idImpressora, consulta.getDataInicial(), consulta.getDataFinal());
			for (String idCliente : clientes) {
				all.addAll(contadorDAO.list(idCliente, consulta.getDataInicial(), consulta.getDataFinal()));
			}
		}
		
		Collections.sort(all, new Comparator<Contador>() {
			@Override
			public int compare(Contador c1, Contador c2) {
				if (c1.getData().before(c2.getData())) {
					return -1;
				} else if (c1.getData().after(c2.getData())) {
					return 1;
				} else {
					if (c1.getPagPreto() < c2.getPagPreto()) {
						return -1;
					} else if (c1.getPagPreto() > c2.getPagPreto()) {
						return 1;
					} else {
						if (c1.getPagColor() < c2.getPagColor()) {
							return -1;
						} else if (c1.getPagColor() > c2.getPagColor()) {
							return 1;
						} else {
							return 0;
						}
					}
				}
			}
		});
		
		for (Contador c : all) {
			if (consulta.getIdCliente().equals(c.getCliente())) {
				long anteriorPreto = 0L;
				if (anteriorPretoMap.containsKey(c.getImpressora())) {
					anteriorPreto = anteriorPretoMap.get(c.getImpressora());
				} else {
					Calendar calendar = Calendar.getInstance(Util.getTimeZone());
					calendar.setTime(c.getData());
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					Contador cont = contadorDAO.getLast(null, c.getImpressora(), calendar.getTime());
					if (cont == null) {
						Impressora imp = impressoraDAO.findByPK(c.getImpressora());
						if (imp != null) {
							anteriorPreto = (imp.getPagPretoInicial() == null) ? 0L : imp.getPagPretoInicial();
						}
					} else {
						anteriorPreto = cont.getPagPreto();
					}
				}
				long somaPreto = (somaPretoMap.containsKey(c.getImpressora())) ? somaPretoMap.get(c.getImpressora()) : 0L;
				somaPretoMap.put(c.getImpressora(), somaPreto + (((c.getPagPreto() == null) ? 0L : c.getPagPreto()) - anteriorPreto));
			
				
				long anteriorColor = 0L;
				if (anteriorColorMap.containsKey(c.getImpressora())) {
					anteriorColor = anteriorColorMap.get(c.getImpressora());
				} else {
					Calendar calendar = Calendar.getInstance(Util.getTimeZone());
					calendar.setTime(c.getData());
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					Contador cont = contadorDAO.getLast(null, c.getImpressora(), calendar.getTime());
					if (cont == null) {
						Impressora imp = impressoraDAO.findByPK(c.getImpressora());
						if (imp != null) {
							anteriorColor = (imp.getPagColorInicial() == null) ? 0L : imp.getPagColorInicial();
						}
					} else {
						anteriorColor = cont.getPagColor();
					}
				}
				long somaColor = (somaColorMap.containsKey(c.getImpressora())) ? somaColorMap.get(c.getImpressora()) : 0L;
				somaColorMap.put(c.getImpressora(), somaColor + (((c.getPagColor() == null) ? 0L : c.getPagColor()) - anteriorColor));
			}
			
			anteriorPretoMap.put(c.getImpressora(), (c.getPagPreto() == null) ? 0L : c.getPagPreto());
			anteriorColorMap.put(c.getImpressora(), (c.getPagColor() == null) ? 0L : c.getPagColor());
			
			dataMap.put(c.getImpressora(), c.getData());
		}
		
		pagPreto = 0;
		pagColor = 0;
		
		for (String idImpressora : somaPretoMap.keySet()) {
			Impressora impressora = impressoraDAO.findByPK(idImpressora);
			
			Result r = new Result();
			r.setCliente(consulta.getIdCliente());
			r.setImpressora(idImpressora);
			r.setTemplate((impressora == null) ? null : impressora.getTemplate());
			r.setDescricao((impressora == null) ? null : impressora.getDescricao());
			r.setData(dataMap.get(idImpressora));
			r.setPagPreto(somaPretoMap.get(idImpressora));
			r.setPagColor(somaColorMap.get(idImpressora));
			
			pagPreto += (r.getPagPreto() == null) ? 0 : r.getPagPreto();
			pagColor += (r.getPagColor() == null) ? 0 : r.getPagColor();
			
			result.add(r);
		}
		
		consulta.clear();
		locCliente.clear();
		
		
		panel = "lista";
	}
/*

	public void listar() {
		result.clear();
		
		Calendar cal = Calendar.getInstance(Util.getTimeZone());
		cal.setTime(consulta.getDataInicial());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		log.info(cal.getTime());
		log.info(consulta.getDataFinal());
		
		List<Contador> all = contadorDAO.list(consulta.getIdCliente(), consulta.getDataInicial(), consulta.getDataFinal());
		
		
		Set<String> distinctImpressoras = new HashSet<String>();
		for (Contador c : all) {
			distinctImpressoras.add(c.getImpressora());
		}
		
		List<Contador> leituraAtual = new ArrayList<Contador>();
		List<Contador> leituraAnterior = new ArrayList<Contador>();
		
		log.info("Distinct="+distinctImpressoras.size());
		
		for (String idImpressora : distinctImpressoras) {
			Contador contadorAtual = contadorDAO.getLast(consulta.getIdCliente(), idImpressora, consulta.getDataFinal());
			if (contadorAtual != null) {
				leituraAtual.add(contadorAtual);				
			}
			Contador contadorAnterior = contadorDAO.getLast(null, idImpressora, cal.getTime());
			if (contadorAnterior != null) {
				leituraAnterior.add(contadorAnterior);
			}
		}
		
		log.info("Anterior = " + leituraAnterior.size());
		log.info("Atual = " + leituraAtual.size());
		
		pagPreto = 0;
		pagColor = 0;
		if (leituraAtual != null) {
			for (Contador la : leituraAtual) {
				Impressora impressora = impressoraDAO.findByPK(la.getImpressora());
				
				Result r = new Result();
				r.setCliente(la.getCliente());
				r.setImpressora(la.getImpressora());
				r.setTemplate((impressora == null) ? null : impressora.getTemplate());
				r.setDescricao((impressora == null) ? null : impressora.getDescricao());
				r.setData(la.getData());
				r.setPagPreto(la.getPagPreto());
				r.setPagColor(la.getPagColor());
				
				for (Contador lb : leituraAnterior) {
					if (la.getImpressora().equalsIgnoreCase(lb.getImpressora())) {
						Long totalPreto = la.getPagPreto() - lb.getPagPreto();
						Long totalColor = la.getPagColor() - lb.getPagColor();
						
						r.setPagPreto(totalPreto);
						r.setPagColor(totalColor);
						
						break;
					}
				}
				
				pagPreto += (r.getPagPreto() == null) ? 0 : r.getPagPreto();
				pagColor += (r.getPagColor() == null) ? 0 : r.getPagColor();
				
				result.add(r);
			}
		}
		
		
		consulta.clear();
		locCliente.clear();
		
		
		panel = "lista";
	}
*/
	/*
	
	public void listar() {
		result.clear();
		
		Integer mesAnterior;
		Integer anoAnterior;

		if (consulta.getMes() > 1) {
			mesAnterior = consulta.getMes() - 1;
			anoAnterior = consulta.getAno();
		} else {
			mesAnterior = 12;
			anoAnterior = consulta.getAno() - 1;
		}

		List<ContadorMensal> leituraAnterior = null;
		List<ContadorMensal> leituraAtual = null;

		try {
			leituraAnterior = contadorMensalDAO.listClienteMes(
					consulta.getIdCliente(), mesAnterior, anoAnterior);
			leituraAtual = contadorMensalDAO.listClienteMes(consulta.getIdCliente(),
					consulta.getMes(), consulta.getAno());
		} catch (Exception ex) {
			addMessage("Erro ao executar consulta: ", ex.getMessage());
		}

		pagPreto = 0;
		pagColor = 0;
		if (leituraAtual != null) {
			for (ContadorMensal la : leituraAtual) {
				Impressora impressora = impressoraDAO.findByPK(la.getImpressora());
				
				Result r = new Result();
				r.setCliente(la.getCliente());
				r.setImpressora(la.getImpressora());
				r.setTemplate((impressora == null) ? null : impressora.getTemplate());
				r.setDescricao((impressora == null) ? null : impressora.getDescricao());
				r.setData(la.getData());
				r.setPagPreto(la.getPagPreto());
				r.setPagColor(la.getPagColor());
				
				for (ContadorMensal lb : leituraAnterior) {
					if (la.getImpressora().equalsIgnoreCase(lb.getImpressora())) {
						Long totalPreto = la.getPagPreto() - lb.getPagPreto();
						Long totalColor = la.getPagColor() - lb.getPagColor();
						
						r.setPagPreto(totalPreto);
						r.setPagColor(totalColor);
						
						break;
					}
				}
				
				pagPreto += (r.getPagPreto() == null) ? 0 : r.getPagPreto();
				pagColor += (r.getPagColor() == null) ? 0 : r.getPagColor();
				
				result.add(r);
			}
		}
		consulta.clear();
		locCliente.clear();
		
		
		panel = "lista";
	}
*/
	public Integer getPagPreto() {
		return pagPreto;
	}

	public Integer getPagColor() {
		return pagColor;
	}

	public String getPanel() {
		return panel;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public LocCliente getLocCliente() {
		return locCliente;
	}

	public List<Result> getResult() {
		return result;
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

		private String idCliente;
		private Date dataInicial;
		private Date dataFinal;

		public void clear() {
			idCliente = null;
			dataInicial = null;
			dataFinal = null;
		}

		public String getIdCliente() {
			return idCliente;
		}

		public void setIdCliente(String idCliente) {
			this.idCliente = idCliente;
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

		public void selecionar() {
			consulta.setIdCliente(idSelecionado);
			panel = "consulta";
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

	public class Result implements Serializable {
		private static final long serialVersionUID = 1L;

		private String cliente;
		private String impressora;
		private Long template;
		private String descricao;
		private Date data;
		private Long pagPreto;
		private Long pagColor;

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

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public Date getData() {
			return data;
		}

		public void setData(Date data) {
			this.data = data;
		}

		public Long getPagPreto() {
			return pagPreto;
		}

		public void setPagPreto(Long pagPreto) {
			this.pagPreto = pagPreto;
		}

		public Long getPagColor() {
			return pagColor;
		}

		public void setPagColor(Long pagColor) {
			this.pagColor = pagColor;
		}

	}
}
