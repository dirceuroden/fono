package br.com.controleimpressoras.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.AbstractFacesBean;
import br.com.controleimpressoras.config.Util;
import br.com.controleimpressoras.dao.ClienteDAO;
import br.com.controleimpressoras.dao.ContadorDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.dao.LeituraDAO;
import br.com.controleimpressoras.dao.TemplateSnmpDAO;
import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.Cliente;
import br.com.controleimpressoras.model.Contador;
import br.com.controleimpressoras.model.Impressora;
import br.com.controleimpressoras.model.Leitura;
import br.com.controleimpressoras.model.TemplateSnmp;
import br.com.controleimpressoras.model.TipoInformacao;

@ManagedBean
@ViewScoped
public class RelatorioLeiturasMB extends AbstractFacesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RelatorioLeiturasMB.class);
	
	private LeituraDAO leituraDAO;
	
	private TemplateSnmpDAO templateSnmpDAO;
	
	private TipoInformacaoDAO tipoInformacaoDAO;
	
	private ImpressoraDAO impressoraDAO;
	
	private ClienteDAO clienteDAO;
	
	private ContadorDAO contadorDAO;
	
	private List<Leitura> leituras;
	
	private List<Leitura> leiturasAll;
	
	private Map<String, Object> templateSnmpSOM;
	
	private Map<String, Object> tipoInformacaoSOM;
	
	private Map<String, Object> clienteSOM;
	
	private Map<String, Object> classificarSOM;
	
	private String panel;
	
	private Consulta consulta;
	
	private Integer leituraRows;
	
	private Integer leituraPage;
	
	@PostConstruct
	public void init() {
		leituraRows = 20;
		leituraPage = 0;
		leituras = new ArrayList<Leitura>();
		leiturasAll = new ArrayList<Leitura>();
		consulta = new Consulta();
		leituraDAO = new LeituraDAO();
		tipoInformacaoDAO = new TipoInformacaoDAO();
		impressoraDAO = new ImpressoraDAO();
		templateSnmpDAO = new TemplateSnmpDAO();
		clienteDAO = new ClienteDAO();
		contadorDAO = new ContadorDAO();

		templateSnmpSOM = new TreeMap<String, Object>();
		templateSnmpSOM.put("", null);
		for (TemplateSnmp t : templateSnmpDAO.listAll()) {
			templateSnmpSOM.put(t.getDescricao(), t.getId());
		}

		tipoInformacaoSOM = new TreeMap<String, Object>();
		tipoInformacaoSOM.put("Total de Páginas Impressas", 0L);
		for (TipoInformacao t : tipoInformacaoDAO.listAll()) {
			tipoInformacaoSOM.put(t.getDescricao(), t.getId());
		}

		clienteSOM = new TreeMap<String, Object>();
		clienteSOM.put("", null);
		for (Cliente c : clienteDAO.listAll()) {
			clienteSOM.put(c.getNome(), c.getId());
		}
		
		classificarSOM = new TreeMap<String, Object>();
		classificarSOM.put("Valor Crescente", "C");
		classificarSOM.put("Valor Decrescente", "D");
		
		panel = "consulta";
	}
	
	public void voltar() {
		panel = "consulta";
	}
	
	public void listar() {
		
		if (consulta.getIdTemplate() == null || consulta.getIdTemplate().equals(0L) && 
				(consulta.getIdCliente() == null || consulta.getIdCliente().trim().length() == 0)) {
			error("Você deve escolher um cliente ou um template.");
			panel = "consulta";
			return;
		}
		
		try {
			leiturasAll.clear();
			
			TipoInformacao ti = null;
			if (consulta.getIdTipoInformacao().equals(0L)) {
				ti = new TipoInformacao();
				ti.setTipoRetorno("N");
			} else {
				ti = tipoInformacaoDAO.findByPK(consulta.getIdTipoInformacao());
			}
			final TipoInformacao tipoInformacao = ti;
			
			if (consulta.getIdTipoInformacao().equals(0L)) {
				List<Impressora> listaImpressoras = impressoraDAO.list(consulta.getIdCliente(), consulta.getIdTemplate());
				for (Impressora imp : listaImpressoras) {
					Contador c = contadorDAO.getLast(consulta.getIdCliente(), imp.getId(), Util.getDataAtual());
					if (c != null) {
						Long pagPreto = (c.getPagPreto() == null) ? 0L : c.getPagPreto();
						Long pagColor = (c.getPagColor() == null) ? 0L : c.getPagColor();

						Leitura l = new Leitura();
						l.setCliente(c.getCliente());
						l.setData(c.getData());
						l.setImpressora(c.getImpressora());
						l.setTipoInformacao(0L);
						l.setValor(Long.valueOf(pagPreto + pagColor).toString());
						leiturasAll.add(l);
					}
				}
			} else {
				List<Leitura> all = leituraDAO.listTipoInformacao(consulta.getIdCliente(), consulta.getIdTipoInformacao());
				for (Leitura l : all) {
					Impressora imp = impressoraDAO.findByPK(l.getImpressora());
					if (consulta.getIdTemplate() == null || consulta.getIdTemplate().equals(0L)) {
						leiturasAll.add(l);
					} else if (imp != null && imp.getTemplate().equals(consulta.getIdTemplate())) {
						leiturasAll.add(l);
					}
				}
			}
			
			final String classificar = consulta.getClassificar();
			Collections.sort(leiturasAll, new Comparator<Leitura>() {
				@Override
				public int compare(Leitura l1, Leitura l2) {
					if (tipoInformacao.getTipoRetorno().equalsIgnoreCase("T")) {
						return (classificar.equals("C")) ? l1.getValor().compareTo(l2.getValor()) : l2.getValor().compareTo(l1.getValor());
					} else if (tipoInformacao.getTipoRetorno().equalsIgnoreCase("N")) {
						try {
							Long num1 = Long.valueOf(l1.getValor());
							Long num2 = Long.valueOf(l2.getValor());
							return (classificar.equals("C")) ? num1.compareTo(num2) : num2.compareTo(num1);
						} catch (Exception e) {
							e.printStackTrace();
							return 0;
						}
					}
					return 0;
				}
			});
		} catch(Exception ex) {
			ex.printStackTrace();
			error("Erro ao gerar relatório: " + ex.getMessage());
		}
		panel = "lista";
	}
	
	public void pagInicio() {
		leituraPage = 0;
	}
	
	public void pagAnterior() {
		leituraPage = (leituraPage.equals(0)) ? 0 : leituraPage - 1;
	}
	
	public void pagProximo() {
		leituraPage = (leituraRows.equals(leituras.size())) ? leituraPage + 1 : leituraPage;
	}

	public List<Leitura> getLeituras() {
		leituras.clear();
		int inicio = leituraPage * leituraRows;
		for (int i = inicio; i < inicio + leituraRows; i++) {
			if (i < leiturasAll.size()) {
				leituras.add(leiturasAll.get(i));
			}
		}
		return leituras;
	}

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}

	public Map<String, Object> getTemplateSnmpSOM() {
		return templateSnmpSOM;
	}

	public Map<String, Object> getTipoInformacaoSOM() {
		return tipoInformacaoSOM;
	}

	public Map<String, Object> getClienteSOM() {
		return clienteSOM;
	}

	public Map<String, Object> getClassificarSOM() {
		return classificarSOM;
	}

	public int getLeiturasSize() {
		return leituras.size();
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public Integer getLeituraRows() {
		return leituraRows;
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
	
	public class Consulta implements Serializable {
		private static final long serialVersionUID = 1L;

		private String idCliente;
		private Long idTemplate;
		private Long idTipoInformacao;
		private String classificar;

		public String getIdCliente() {
			return idCliente;
		}

		public void setIdCliente(String idCliente) {
			this.idCliente = idCliente;
		}

		public Long getIdTemplate() {
			return idTemplate;
		}

		public void setIdTemplate(Long idTemplate) {
			this.idTemplate = idTemplate;
		}

		public Long getIdTipoInformacao() {
			return idTipoInformacao;
		}

		public void setIdTipoInformacao(Long idTipoInformacao) {
			this.idTipoInformacao = idTipoInformacao;
		}

		public String getClassificar() {
			return classificar;
		}

		public void setClassificar(String classificar) {
			this.classificar = classificar;
		}
		
	}
	
}
