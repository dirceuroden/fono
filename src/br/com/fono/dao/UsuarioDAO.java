package br.com.fono.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import br.com.fono.config.PMF;
import br.com.fono.model.Usuario;

public class UsuarioDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	public void save(Usuario usuario) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(usuario);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listAll() {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Usuario.class);
		q.setOrdering("login asc");
		try {
			List<Usuario> result = (List<Usuario>) q.execute();
			usuarios.addAll(result);
		} finally {
			pm.close();
		}
		return usuarios;
	}

	public void remove(Usuario usuario) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Usuario c = pm.getObjectById(Usuario.class, usuario.getLogin());
			pm.deletePersistent(c);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
	}
	
	public Usuario findByPK(String id) {
		if (id == null || id.length() == 0) return null;
		Usuario usuario = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			usuario = pm.getObjectById(Usuario.class, id);
		} catch (JDOObjectNotFoundException ex) {
		} finally {
			pm.close();
		}
		return usuario;
	}
}
