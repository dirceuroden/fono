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
import br.com.controleimpressoras.model.HistoricoAlerta;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.sun.org.apache.bcel.internal.generic.IDIV;

public class HistoricoAlertaDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(HistoricoAlertaDAO.class);
	
	public void save(HistoricoAlerta historicoAlerta) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(historicoAlerta);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HistoricoAlerta> listAll() {
		List<HistoricoAlerta> historicoAlertas = new ArrayList<HistoricoAlerta>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(HistoricoAlerta.class);
		q.setOrdering("data desc");
		try {
			List<HistoricoAlerta> result = (List<HistoricoAlerta>) q.execute();
			historicoAlertas.addAll(result);
		} finally {
			pm.close();
		}
		return historicoAlertas;
	}
	
	@SuppressWarnings("deprecation")
	public List<HistoricoAlerta> list(String idImpressora, Date dataInicial, Date dataFinal) {
		List<HistoricoAlerta> historicoAlertas = new ArrayList<HistoricoAlerta>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query("HistoricoAlerta");
	    query.addSort("data", SortDirection.DESCENDING);
	    if (idImpressora != null && idImpressora.trim().length() > 0) {
	    	query.addFilter("impressora", FilterOperator.EQUAL, idImpressora);
	    }
		query.addFilter("data", FilterOperator.GREATER_THAN_OR_EQUAL, dataInicial);
		query.addFilter("data", FilterOperator.LESS_THAN_OR_EQUAL, dataFinal);
		PreparedQuery pq = datastore.prepare(query);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	HistoricoAlerta historicoAlerta = new HistoricoAlerta();
	    	historicoAlerta.setId(Long.valueOf(entity.getKey().toString().substring(16, entity.getKey().toString().length() - 1)));
	    	historicoAlerta.setCliente((String) entity.getProperty("cliente"));
	    	historicoAlerta.setImpressora((String) entity.getProperty("impressora"));
	    	historicoAlerta.setData((Date) entity.getProperty("data"));
	    	historicoAlerta.setStatus((String) entity.getProperty("status"));
	    	historicoAlertas.add(historicoAlerta);
	    }
		return historicoAlertas;
	}
	
	public void remove(HistoricoAlerta historicoAlerta) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			HistoricoAlerta c = pm.getObjectById(HistoricoAlerta.class, historicoAlerta.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public HistoricoAlerta findByPK(String id) {
		HistoricoAlerta historicoAlerta = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			historicoAlerta = pm.getObjectById(HistoricoAlerta.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return historicoAlerta;
	}
}
