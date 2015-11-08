package br.com.fono.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.fono.config.PMF;
import br.com.fono.model.Paciente;

public class PacienteDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(Paciente paciente) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(paciente);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Paciente> listAll() {
		List<Paciente> pacientes = new ArrayList<Paciente>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Paciente.class);
		q.setOrdering("nome asc");
		try {
			List<Paciente> result = (List<Paciente>) q.execute();
			pacientes.addAll(result);
		} finally {
			pm.close();
		}
		return pacientes;
	}

	public void remove(Paciente paciente) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Paciente c = pm.getObjectById(Paciente.class, paciente.getCnpj());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Paciente findByPK(String id) {
		Paciente paciente = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			paciente = pm.getObjectById(Paciente.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return paciente;
	}
}
