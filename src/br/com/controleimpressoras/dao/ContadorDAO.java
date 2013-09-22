package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.config.Util;
import br.com.controleimpressoras.model.Contador;
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

public class ContadorDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private static Logger log = Logger.getLogger(ContadorDAO.class);

	public void save(Contador contador) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setTimeZone(Util.getTimeZone());
		contador.setData(Util.trunc(contador.getData()));
		try {
			String id = contador.getCliente() + "-" + contador.getImpressora() + "-" + sdf.format(contador.getData());
			contador.setId(id);
			pm.makePersistent(contador);
		} finally {
			pm.close();
		}
	}
	
	public Map<String, Object> list(String cursor, int pageSize) {
		List<Contador> contadores = new ArrayList<Contador>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
		if (cursor != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
		}
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("Contador");
    	q.addSort("data", SortDirection.DESCENDING);
    	q.addSort("impressora", SortDirection.ASCENDING);
    	PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	Contador contador = new Contador();
	    	contador.setId(entity.getKey().toString().substring(10, entity.getKey().toString().length() - 2));
	    	contador.setCliente(entity.getProperty("cliente").toString());
	    	contador.setImpressora((String) entity.getProperty("impressora"));
	    	contador.setData((Date) entity.getProperty("data"));
	    	contador.setPagPreto((Long) entity.getProperty("pagPreto"));
	    	contador.setPagColor((Long) entity.getProperty("pagColor"));
	    	contadores.add(contador);
	    }
		
	    cursor = results.getCursor().toWebSafeString();
	    
	    
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("CURSOR", cursor);
	    result.put("RESULT", contadores);
		return result;
	}

	@SuppressWarnings({ "deprecation" })
	public List<Contador> list(String idCliente, Date dataInicial, Date dataFinal) {
		List<Contador> contadores = new ArrayList<Contador>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query("Contador");
		//query.addSort("data", SortDirection.ASCENDING);
		//query.addSort("pagPreto", SortDirection.ASCENDING);
		//query.addSort("pagColor", SortDirection.ASCENDING);
		if (idCliente != null) {
			query.addFilter("cliente", FilterOperator.EQUAL, idCliente);
		}
		query.addFilter("data", FilterOperator.GREATER_THAN_OR_EQUAL, dataInicial);
		query.addFilter("data", FilterOperator.LESS_THAN_OR_EQUAL, dataFinal);
    	PreparedQuery pq = datastore.prepare(query);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	Contador contador = new Contador();
	    	contador.setId(entity.getKey().toString().substring(10, entity.getKey().toString().length() - 2));
	    	contador.setCliente(entity.getProperty("cliente").toString());
	    	contador.setImpressora((String) entity.getProperty("impressora"));
	    	contador.setData((Date) entity.getProperty("data"));
	    	contador.setPagPreto(Long.valueOf(entity.getProperty("pagPreto").toString()));
	    	contador.setPagColor(Long.valueOf(entity.getProperty("pagColor").toString()));
	    	contadores.add(contador);
	    }
		return contadores;
	}

	@SuppressWarnings({ "deprecation" })
	public Set<String> getClientes(String idCliente, String idImpressora, Date dataInicial, Date dataFinal) {
		Set<String> clientes = new HashSet<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query("Contador");
		//query.addFilter("cliente", FilterOperator.NOT_EQUAL, idCliente);
		query.addFilter("impressora", FilterOperator.EQUAL, idImpressora);
		query.addFilter("data", FilterOperator.GREATER_THAN_OR_EQUAL, dataInicial);
		query.addFilter("data", FilterOperator.LESS_THAN_OR_EQUAL, dataFinal);
    	PreparedQuery pq = datastore.prepare(query);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	String cliente = entity.getProperty("cliente").toString();
	    	if (!cliente.equals(idCliente)) {
	    		clientes.add(cliente);
	    	}
	    }
		return clientes;
	}

	@SuppressWarnings({ "deprecation" })
	public Contador getLast(String idCliente, String idImpressora, Date maxDate) {
    	Contador contador = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(1);
		com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query("Contador");
		query.addSort("data", SortDirection.DESCENDING);
		query.addSort("pagPreto", SortDirection.DESCENDING);
		query.addSort("pagColor", SortDirection.DESCENDING);
		if (idCliente != null && idCliente.trim().length() > 0) {
			query.addFilter("cliente", FilterOperator.EQUAL, idCliente);
		}
		query.addFilter("impressora", FilterOperator.EQUAL, idImpressora);
		query.addFilter("data", FilterOperator.LESS_THAN_OR_EQUAL, maxDate);
    	PreparedQuery pq = datastore.prepare(query);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	contador = new Contador();
	    	contador.setId(entity.getKey().toString().substring(10, entity.getKey().toString().length() - 2));
	    	contador.setCliente(entity.getProperty("cliente").toString());
	    	contador.setImpressora((String) entity.getProperty("impressora"));
	    	contador.setData((Date) entity.getProperty("data"));
	    	contador.setPagPreto(Long.valueOf(entity.getProperty("pagPreto").toString()));
	    	contador.setPagColor(Long.valueOf(entity.getProperty("pagColor").toString()));
	    	break;
	    }
		return contador;
	}
	
	@SuppressWarnings("unchecked")
	public List<Contador> listCliente(String cliente) {
		List<Contador> contadores = new ArrayList<Contador>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Contador.class);
		if (cliente != null && cliente.trim().length() > 0) {
			q.setFilter("cliente == '" + cliente + "'");
		}
		try {
			List<Contador> result = (List<Contador>) q.execute();
			contadores.addAll(result);
		} finally {
			pm.close();
		}
		return contadores;
	}


	public void remove(Contador contador) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Contador c = pm.getObjectById(Contador.class, contador.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Contador findByPK(String id) {
		Contador contador = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			contador = pm.getObjectById(Contador.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return contador;
	}
}
