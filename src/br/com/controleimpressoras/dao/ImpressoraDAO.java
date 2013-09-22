package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.SortDirection;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.Alerta;
import br.com.controleimpressoras.model.Impressora;

public class ImpressoraDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(Impressora impressora) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(impressora);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Impressora> listAll() {
		List<Impressora> impressoras = new ArrayList<Impressora>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Impressora.class);
		q.setOrdering("id asc");
		try {
			List<Impressora> result = (List<Impressora>) q.execute();
			impressoras.addAll(result);
		} finally {
			pm.close();
		}
		return impressoras;
	}

	@SuppressWarnings("unchecked")
	public List<Impressora> list(String idCliente) {
		List<Impressora> impressoras = new ArrayList<Impressora>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Impressora.class);
		q.setFilter("cliente == '" + idCliente + "'");
		q.setOrdering("cliente asc");
		q.setOrdering("template asc");
		try {
			List<Impressora> result = (List<Impressora>) q.execute();
			impressoras.addAll(result);
		} finally {
			pm.close();
		}
		return impressoras;
	}

	@SuppressWarnings("unchecked")
	public List<Impressora> list(String idCliente, Long idTemplate) {
		List<Impressora> impressoras = new ArrayList<Impressora>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Impressora.class);
		if ((idCliente == null || idCliente.trim().length() == 0) && 
				(idTemplate == null || idTemplate.equals(0L))) {
			
		} else if (idCliente == null || idCliente.trim().length() == 0) {
			q.setFilter("template == " + idTemplate);
		} else if (idTemplate == null || idTemplate.equals(0L)) {
			q.setFilter("cliente == '" + idCliente + "'");
		} else {
			q.setFilter("cliente == '" + idCliente + "' && template == " + idTemplate);
		}
		try {
			List<Impressora> result = (List<Impressora>) q.execute();
			impressoras.addAll(result);
		} finally {
			pm.close();
		}
		return impressoras;
	}
	
	@SuppressWarnings("deprecation")
	public Map<String, Object> list(String cursor, int pageSize, String idCliente) {
		List<Impressora> impressoras = new ArrayList<Impressora>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
		if (cursor != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
		}
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("Impressora");
	    if (idCliente != null && idCliente.trim().length() > 0) {
	    	q.addFilter("cliente", FilterOperator.EQUAL, idCliente);
	    }
	    q.addSort("cliente", SortDirection.ASCENDING);
    	q.addSort("template", SortDirection.ASCENDING);
    	PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	Impressora impressora = new Impressora();
	    	impressora.setId(entity.getKey().toString().substring(12, entity.getKey().toString().length() - 2));
	    	impressora.setTemplate(Long.valueOf(entity.getProperty("template").toString()));
	    	impressora.setCliente((String) entity.getProperty("cliente"));
	    	impressora.setIp((String) entity.getProperty("ip"));
	    	impressora.setDescricao((String) entity.getProperty("descricao"));
	    	impressoras.add(impressora);
	    }
		
	    cursor = results.getCursor().toWebSafeString();
	    
	    
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("CURSOR", cursor);
	    result.put("RESULT", impressoras);
		return result;
	}


	public void remove(Impressora impressora) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Impressora c = pm.getObjectById(Impressora.class, impressora.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Impressora findByPK(String nroSerie) {
		Impressora impressora = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			impressora = pm.getObjectById(Impressora.class, nroSerie);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return impressora;
	}
}
