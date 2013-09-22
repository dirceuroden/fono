package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.ConfigImpressora;

public class ConfigImpressoraDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(ConfigImpressora configImpressora) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(configImpressora);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConfigImpressora> listAll() {
		List<ConfigImpressora> configImpressoras = new ArrayList<ConfigImpressora>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(ConfigImpressora.class);
		q.setOrdering("modelo asc");
		try {
			List<ConfigImpressora> result = (List<ConfigImpressora>) q.execute();
			configImpressoras.addAll(result);
		} finally {
			pm.close();
		}
		return configImpressoras;
	}

	public void remove(ConfigImpressora configImpressora) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ConfigImpressora c = pm.getObjectById(ConfigImpressora.class, configImpressora.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public ConfigImpressora findByPK(Long id) {
		ConfigImpressora configImpressora = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			configImpressora = pm.getObjectById(ConfigImpressora.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return configImpressora;
	}
}
