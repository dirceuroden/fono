package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.Informacao;

public class InformacaoDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(Informacao informacao) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(informacao);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Informacao> list(Long idTemplateSnmp) {
		List<Informacao> informacoes = new ArrayList<Informacao>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Informacao.class);
		q.setFilter("template == " + idTemplateSnmp);
		q.setOrdering("tipoInformacao asc");
		try {
			List<Informacao> result = (List<Informacao>) q.execute();
			informacoes.addAll(result);
		} finally {
			pm.close();
		}
		return informacoes;
	}

	public void remove(Informacao informacao) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Informacao c = pm.getObjectById(Informacao.class, informacao.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Informacao findByPK(Long id) {
		Informacao informacao = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			informacao = pm.getObjectById(Informacao.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return informacao;
	}
}
