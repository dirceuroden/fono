package br.com.fono.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.fono.config.PMF;
import br.com.fono.model.Cliente;

public class ClienteDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(Cliente cliente) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(cliente);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> listAll() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Cliente.class);
		q.setOrdering("nome asc");
		try {
			List<Cliente> result = (List<Cliente>) q.execute();
			clientes.addAll(result);
		} finally {
			pm.close();
		}
		return clientes;
	}

	public void remove(Cliente cliente) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Cliente c = pm.getObjectById(Cliente.class, cliente.getCnpj());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Cliente findByPK(String id) {
		if (id == null || id.length() == 0) return null;
		Cliente cliente = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			cliente = pm.getObjectById(Cliente.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return cliente;
	}
}
