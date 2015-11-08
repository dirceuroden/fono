package br.com.fono.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.fono.config.PMF;
import br.com.fono.model.Exame;

public class ExameDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(Exame exame) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(exame);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Exame> listAll() {
		List<Exame> exames = new ArrayList<Exame>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Exame.class);
		q.setOrdering("dataExame desc");
		try {
			List<Exame> result = (List<Exame>) q.execute();
			exames.addAll(result);
		} finally {
			pm.close();
		}
		return exames;
	}

	@SuppressWarnings("unchecked")
	public List<Exame> listByDataValidade(Date dataInicial, Date dataFinal) {
		List<Exame> exames = new ArrayList<Exame>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Exame.class);
		q.setOrdering("dataExame desc");
		try {
			List<Exame> result = (List<Exame>) q.execute();
			for (Exame e : result) {
				if ((e.getDataValidade().after(dataInicial) || e.getDataValidade().equals(dataInicial)) && 
						(e.getDataValidade().before(dataFinal) || e.getDataValidade().equals(dataFinal))) {
					exames.add(e);
				}
			}
		} finally {
			pm.close();
		}
		return exames;
	}

	public void remove(Exame exame) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Exame c = pm.getObjectById(Exame.class, exame.getCnpj());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Exame findByPK(Long id) {
		Exame exame = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			exame = pm.getObjectById(Exame.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return exame;
	}
}
