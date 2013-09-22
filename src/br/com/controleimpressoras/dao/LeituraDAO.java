package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.config.Util;
import br.com.controleimpressoras.model.Leitura;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;

public class LeituraDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(LeituraDAO.class);

	public void save(Leitura leitura) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		sdf.setTimeZone(Util.getTimeZone());
		try {
			String id = leitura.getCliente() + "-" + leitura.getImpressora() + "-" + leitura.getTipoInformacao();
			leitura.setId(id);
			pm.makePersistent(leitura);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Leitura> listAll() {
		List<Leitura> leituras = new ArrayList<Leitura>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Leitura.class);
		q.setOrdering("data desc");
		try {
			List<Leitura> result = (List<Leitura>) q.execute();
			leituras.addAll(result);
		} finally {
			pm.close();
		}
		return leituras;
	}
	
	public Map<String, Object> list(String cursor, int pageSize) {
		List<Leitura> leituras = new ArrayList<Leitura>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
		if (cursor != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
		}
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("Leitura");
	    q.addSort("data", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	Leitura leitura = new Leitura();
	    	leitura.setId(entity.getKey().toString().substring(9, entity.getKey().toString().length() - 2));
	    	leitura.setCliente(entity.getProperty("cliente").toString());
	    	leitura.setImpressora(entity.getProperty("impressora").toString());
	    	leitura.setData((Date) entity.getProperty("data"));
	    	leitura.setTipoInformacao(Long.valueOf(entity.getProperty("tipoInformacao").toString()));
	    	leitura.setValor(entity.getProperty("valor").toString());
	    	leituras.add(leitura);
	    }
		
	    cursor = results.getCursor().toWebSafeString();
	    
	    
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("CURSOR", cursor);
	    result.put("RESULT", leituras);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Leitura> listImpressora(String idImpressora) {
		List<Leitura> leituras = new ArrayList<Leitura>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Leitura.class);
		q.setFilter("impressora == '" + idImpressora + "'");
		q.setOrdering("tipoInformacao asc");
		try {
			List<Leitura> result = (List<Leitura>) q.execute();
			leituras.addAll(result);
		} finally {
			pm.close();
		}
		return leituras;
	}
	
	@SuppressWarnings("unchecked")
	public List<Leitura> listTipoInformacao(String cliente, Long tipoInformacao) {
		List<Leitura> leituras = new ArrayList<Leitura>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Leitura.class);
		if (cliente == null || cliente.trim().length() == 0) {
			q.setFilter("tipoInformacao == " + tipoInformacao);	
		} else {
			q.setFilter("cliente == '" + cliente + "' && tipoInformacao == " + tipoInformacao);
		}
		try {
			List<Leitura> result = (List<Leitura>) q.execute();
			leituras.addAll(result);
		} finally {
			pm.close();
		}
		return leituras;
	}

	public void remove(Leitura leitura) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Leitura c = pm.getObjectById(Leitura.class, leitura.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Leitura findByPK(String id) {
		Leitura leitura = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			leitura = pm.getObjectById(Leitura.class, id);
		} catch (JDOObjectNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			pm.close();
		}
		return leitura;
	}
}
