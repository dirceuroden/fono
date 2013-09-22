package br.com.controleimpressoras.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/teste")
public class Teste implements Serializable {
	private static final long serialVersionUID = 1L;

	@GET
	@Path("/ola/{nome}")
	@Produces("text/plain")
	public String ola(@PathParam("nome") String nome) {
		return "Ola " + nome;
	}
	
	@POST
	@Path("/ola2/{nome}")
	@Produces("text/plain")
	public String ola2(@PathParam("nome") String nome) {
		return "Ola " + nome;
	}

	@GET
	@Path("/pessoa/{nome}")
	@Produces("text/xml")
	public List<Pessoa> pessoa(@PathParam("nome") String nome) {
		
		Pessoa p1 = new Pessoa();
		p1.setNome(nome);
		p1.setIdade(5);
		Pessoa p2 = new Pessoa();
		p2.setNome(nome);
		p2.setIdade(10);
		
		
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(p1);
		lista.add(p2);
		
		return lista;
	}

	@POST
	@Produces("text/plain")
	@Consumes("text/xml")
	public String envia(List<Pessoa> pessoas) {
		
		return "Quantidade = " + pessoas.size();
	}


}
