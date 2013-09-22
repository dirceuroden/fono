package br.com.controleimpressoras.dao;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import br.com.controleimpressoras.config.PMF;
import br.com.controleimpressoras.model.ContadorMensal;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;

public class ContadorMensalDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private static Logger log = Logger.getLogger(ContadorMensalDAO.class);

	public void save(ContadorMensal contadorMensal) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		DecimalFormat df = new DecimalFormat("00");
		try {
			String id = contadorMensal.getCliente() + "-" + contadorMensal.getImpressora() + "-" + 
					df.format(contadorMensal.getMes()) + df.format(contadorMensal.getAno());
			contadorMensal.setId(id);
			pm.makePersistent(contadorMensal);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<ContadorMensal> listAll() {
		List<ContadorMensal> contadoresMensais = new ArrayList<ContadorMensal>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(ContadorMensal.class);
		q.setOrdering("ano desc, mes desc, impressora asc");
		try {
			List<ContadorMensal> result = (List<ContadorMensal>) q.execute();
			contadoresMensais.addAll(result);
		} finally {
			pm.close();
		}
		return contadoresMensais;
	}
	
	
	
	
	public Map<String, Object> list(String cursor, int pageSize) {
		List<ContadorMensal> contadoresMensais = new ArrayList<ContadorMensal>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
		if (cursor != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
		}
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("ContadorMensal");
    	q.addSort("ano", SortDirection.DESCENDING);
    	q.addSort("mes", SortDirection.DESCENDING);
    	q.addSort("impressora", SortDirection.ASCENDING);
    	PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    for (Entity entity : results) {
	    	ContadorMensal contadorMensal = new ContadorMensal();
	    	contadorMensal.setId(entity.getKey().toString().substring(16, entity.getKey().toString().length() - 2));
	    	contadorMensal.setCliente(entity.getProperty("cliente").toString());
	    	contadorMensal.setImpressora((String) entity.getProperty("impressora"));
	    	contadorMensal.setDia(Integer.valueOf(entity.getProperty("dia").toString()));
	    	contadorMensal.setMes(Integer.valueOf(entity.getProperty("mes").toString()));
	    	contadorMensal.setAno(Integer.valueOf(entity.getProperty("ano").toString()));
	    	contadorMensal.setPagPreto(Long.valueOf(entity.getProperty("pagPreto").toString()));
	    	contadorMensal.setPagColor(Long.valueOf(entity.getProperty("pagColor").toString()));
	    	contadoresMensais.add(contadorMensal);
	    }
		
	    cursor = results.getCursor().toWebSafeString();
	    
	    
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("CURSOR", cursor);
	    result.put("RESULT", contadoresMensais);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<ContadorMensal> listClienteMes(String idCliente, Integer mes, Integer ano) throws ParseException {
		List<ContadorMensal> contadoresMensais = new ArrayList<ContadorMensal>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(ContadorMensal.class);
		q.setFilter("cliente == '" + idCliente + "' && mes == " + mes + " && ano == " + ano);
		q.setOrdering("impressora asc");
		try {
			List<ContadorMensal> result = (List<ContadorMensal>) q.execute();
			contadoresMensais.addAll(result);
		} finally {
			pm.close();
		}
		return contadoresMensais;
	}
	
	public void remove(ContadorMensal contadorMensal) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ContadorMensal c = pm.getObjectById(ContadorMensal.class, contadorMensal.getId());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public ContadorMensal findByPK(String id) {
		ContadorMensal contadorMensal = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			contadorMensal = pm.getObjectById(ContadorMensal.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return contadorMensal;
	}
}
