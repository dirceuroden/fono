package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.TipoInformacao;

public class TipoInformacaoDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(TipoInformacao tipoInformacao) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(tipoInformacao);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoInformacao> listAll() {
		List<TipoInformacao> tiposInformacao = new ArrayList<TipoInformacao>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(TipoInformacao.class);
		q.setOrdering("descricao asc");
		try {
			List<TipoInformacao> result = (List<TipoInformacao>) q.execute();
			tiposInformacao.addAll(result);
		} finally {
			pm.close();
		}
		return tiposInformacao;
	}

	public void remove(TipoInformacao tipoInformacao) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			TipoInformacao c = pm.getObjectById(TipoInformacao.class, tipoInformacao.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public TipoInformacao findByPK(Long id) {
		TipoInformacao tipoInformacao = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			tipoInformacao = pm.getObjectById(TipoInformacao.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return tipoInformacao;
	}
}
