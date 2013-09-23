package br.com.controleimpressoras.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.controleimpressoras.config.Util;
import br.com.controleimpressoras.dao.AlertaDAO;
import br.com.controleimpressoras.dao.ContadorDAO;
import br.com.controleimpressoras.dao.HistoricoAlertaDAO;
import br.com.controleimpressoras.dao.ImpressoraDAO;
import br.com.controleimpressoras.dao.InformacaoDAO;
import br.com.controleimpressoras.dao.LeituraDAO;
import br.com.controleimpressoras.dao.TemplateSnmpDAO;
import br.com.controleimpressoras.dao.TipoInformacaoDAO;
import br.com.controleimpressoras.model.AgentSnmpData;
import br.com.controleimpressoras.model.Alerta;
import br.com.controleimpressoras.model.Alertas;
import br.com.controleimpressoras.model.Contador;
import br.com.controleimpressoras.model.HistoricoAlerta;
import br.com.controleimpressoras.model.Impressora;
import br.com.controleimpressoras.model.Informacao;
import br.com.controleimpressoras.model.Leitura;
import br.com.controleimpressoras.model.Leituras;
import br.com.controleimpressoras.model.TemplateSnmp;
import br.com.controleimpressoras.model.TipoInformacao;

@Path("/agentdataws")
public class AgentDataWS implements Serializable {
	private static final long serialVersionUID = 1L;

	@GET
	@Path("/getdata/{cliente}")
	@Produces("text/xml")
	public List<AgentSnmpData> getAgentSnmpDataList(@PathParam("cliente") String cliente) {
		List<AgentSnmpData> agentSnmpDataList = new ArrayList<AgentSnmpData>();
		ImpressoraDAO impressoraDAO = new ImpressoraDAO();
		List<Impressora> impressoras = impressoraDAO.list(cliente); 
		InformacaoDAO informacaoDAO = new InformacaoDAO();
		TipoInformacaoDAO tipoInformacaoDAO = new TipoInformacaoDAO();
		TemplateSnmpDAO templateSnmpDAO = new TemplateSnmpDAO();
		
		if (impressoras != null) {
			for (Impressora imp : impressoras) {
				TemplateSnmp template = templateSnmpDAO.findByPK(imp.getTemplate());
				List<Informacao> informacoes = informacaoDAO.list(imp.getTemplate());
				List<Informacao> statusInfoList = new ArrayList<Informacao>();
				Informacao serialOidInfo = new Informacao();
				if (informacoes != null) {
					for (Informacao info : informacoes) {
						TipoInformacao tipoInfo = tipoInformacaoDAO.findByPK(info.getTipoInformacao());
						if (tipoInfo != null && tipoInfo.getTipo() != null) {
							if (tipoInfo.getTipo().equalsIgnoreCase("N")) {
								serialOidInfo = info;
							} else if (tipoInfo.getTipo().equalsIgnoreCase("S")) {
								statusInfoList.add(info);
							}
						}
					}
				}

				informacoes.remove(serialOidInfo);
				
				AgentSnmpData agentSnmpData = new AgentSnmpData();
				agentSnmpData.setImpressora(imp.getId());
				agentSnmpData.setIp(imp.getIp());
				agentSnmpData.setTemplate(template.getDescricao());
				agentSnmpData.setSerialOid(serialOidInfo.getOid());
				agentSnmpData.setAlertas(template.getAlertas());
				agentSnmpData.setInformacoes(informacoes);
				agentSnmpData.setStatusInfo(statusInfoList);
				agentSnmpDataList.add(agentSnmpData);
			}
		}
		return agentSnmpDataList;
	}
	
	@GET
	@Path("/getleituras")
	@Produces("text/xml")
	public List<Leitura> getleituras() {
		LeituraDAO leituraDAO = new LeituraDAO();
		return leituraDAO.listAll();
	}
	
	@POST
	@Path("/enviaLeituras")
	@Consumes("text/xml")
	@Produces("text/plain")
	public String enviaLeituras(Leituras leituras) {
		String status = "OK";
		LeituraDAO leituraDAO = new LeituraDAO();
		TipoInformacaoDAO tipoInformacaoDAO = new TipoInformacaoDAO();
		ContadorDAO contadorDAO = new ContadorDAO();
		
		Map<String, Long> contadorPreto = new HashMap<String, Long>();
		Map<String, Long> contadorColor = new HashMap<String, Long>();
		
		try {
			if (leituras != null) {
				for (Leitura l : leituras.getList()) {
					TipoInformacao ti = tipoInformacaoDAO.findByPK(l.getTipoInformacao());
					
					if (ti != null && ti.getTipo().equalsIgnoreCase("P")) {
						if (contadorPreto.containsKey(l.getImpressora())) {
							Long val = contadorPreto.get(l.getImpressora());
							Long total = val + Long.valueOf(l.getValor());
							contadorPreto.put(l.getImpressora(), total);
						} else {
							contadorPreto.put(l.getImpressora(), Long.valueOf(l.getValor()));
						}
					}
					
					if (ti != null && ti.getTipo().equalsIgnoreCase("C")) {
						if (contadorColor.containsKey(l.getImpressora())) {
							Long val = contadorColor.get(l.getImpressora());
							Long total = val + Long.valueOf(l.getValor());
							contadorColor.put(l.getImpressora(), total);
						} else {
							contadorColor.put(l.getImpressora(), Long.valueOf(l.getValor()));
						}
					}

					if (ti != null && (ti.getTipo().equalsIgnoreCase("P") || ti.getTipo().equalsIgnoreCase("C"))) {
						Contador contador = new Contador();
						contador.setCliente(l.getCliente());
						contador.setImpressora(l.getImpressora());
						contador.setData(Util.getDataAtual());
						contador.setPagPreto((contadorPreto.get(l.getImpressora()) == null) ? 0 : contadorPreto.get(l.getImpressora()));
						contador.setPagColor((contadorColor.get(l.getImpressora()) == null) ? 0 : contadorColor.get(l.getImpressora()));
						contadorDAO.save(contador);
					}
					l.setData(Util.getDataAtual());
					leituraDAO.save(l);
				}
			} else {
				status = "ERRO";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			status = "ERRO";
		}
		return "<status>" + status + "</status>"; 
	}
	
	@POST
	@Path("/enviaAlertas")
	@Consumes("text/xml")
	@Produces("text/plain")
	public String enviaAlertas(Alertas alertas) {
		String status = "OK";
		AlertaDAO alertaDAO = new AlertaDAO();
		HistoricoAlertaDAO historicoAlertaDAO = new HistoricoAlertaDAO();
		try {
			for (Alerta alerta : alertas.getList()) {
				if (alerta.getTipo() == 9) {
					alertaDAO.remove(alerta);
				} else {
					alerta.setData(Util.getDataAtual());
					alertaDAO.save(alerta);
					
					if (alerta.getTipo() == 1) {
						HistoricoAlerta ha = new HistoricoAlerta();
						ha.setCliente(alerta.getCliente());
						ha.setImpressora(ha.getImpressora());
						ha.setData(Util.trunc(Util.getDataAtual()));
						ha.setStatus(alerta.getDescricao());
						historicoAlertaDAO.save(ha);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			status = "ERRO";
		}
		return "<status>" + status + "</status>"; 
	}
	
	
}
