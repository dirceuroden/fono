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

import org.apache.log4j.Logger;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.Alerta;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;

public class AlertaDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LeituraDAO.class);
	
	public void save(Alerta alerta) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(alerta);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Alerta> listAll() {
		List<Alerta> alertas = new ArrayList<Alerta>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Alerta.class);
		q.setOrdering("data desc");
		try {
			List<Alerta> result = (List<Alerta>) q.execute();
			alertas.addAll(result);
		} finally {
			pm.close();
		}
		return alertas;
	}
	
	public Map<String, Object> list(String cursor, int pageSize) {
		List<Alerta> alertas = new ArrayList<Alerta>();
		List<Alerta> falhas = new ArrayList<Alerta>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults(); //withLimit(pageSize);
		if (cursor != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
		}
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("Alerta");
	    q.addSort("data", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	Alerta alerta = new Alerta();
	    	alerta.setId(entity.getKey().toString().substring(8, entity.getKey().toString().length() - 2));
	    	alerta.setTipo(Integer.valueOf(entity.getProperty("tipo").toString()));
	    	alerta.setCliente((String) entity.getProperty("cliente"));
	    	alerta.setDescricao((String) entity.getProperty("descricao"));
	    	alerta.setData((Date) entity.getProperty("data"));
	    	if (alerta.getTipo() == 1) {
	    		alertas.add(alerta);
	    	} else if (alerta.getTipo() == 2) {
	    		falhas.add(alerta);
	    	}
	    }
		
	    cursor = results.getCursor().toWebSafeString();
	    
	    
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("CURSOR", cursor);
	    result.put("ALERTAS", alertas);
	    result.put("FALHAS", falhas);
		return result;
	}
	
	public void remove(Alerta alerta) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Alerta c = pm.getObjectById(Alerta.class, alerta.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Alerta findByPK(String id) {
		Alerta alerta = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			alerta = pm.getObjectById(Alerta.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return alerta;
	}
}
