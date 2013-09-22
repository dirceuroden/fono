package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.TemplateSnmp;

public class TemplateSnmpDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(TemplateSnmp templateSnmp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(templateSnmp);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TemplateSnmp> listAll() {
		List<TemplateSnmp> templatesSnmp = new ArrayList<TemplateSnmp>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(TemplateSnmp.class);
		q.setOrdering("descricao asc");
		try {
			List<TemplateSnmp> result = (List<TemplateSnmp>) q.execute();
			templatesSnmp.addAll(result);
		} finally {
			pm.close();
		}
		return templatesSnmp;
	}

	public void remove(TemplateSnmp templateSnmp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			TemplateSnmp c = pm.getObjectById(TemplateSnmp.class, templateSnmp.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public TemplateSnmp findByPK(Long id) {
		TemplateSnmp templateSnmp = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			templateSnmp = pm.getObjectById(TemplateSnmp.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return templateSnmp;
	}
}
