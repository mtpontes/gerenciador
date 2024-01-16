package br.com.alura.gerenciador.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Banco {
	
	private static List<Empresa> lista = new ArrayList<>();
	private static List<Usuario> listaUsuarios = new ArrayList<>();
	private static Integer chaveSequencial = 1;
	
	static {
		Empresa empresa = new Empresa(Integer.valueOf(chaveSequencial++), "Empresa1");
		Empresa empresa2 = new Empresa(Integer.valueOf(chaveSequencial++), "Empresa2");
		Empresa empresa3 = new Empresa(Integer.valueOf(chaveSequencial++), "Empresa3");
		
		lista.addAll(Arrays.asList(empresa, empresa2, empresa3));

		Usuario u1 = new Usuario();
		u1.setLogin("nico");
		u1.setSenha("123456789");
		Usuario u2 = new Usuario();
		u2.setLogin("mateus");
		u2.setSenha("123456789");
		
		listaUsuarios.add(u1);
		listaUsuarios.add(u2);
	}

	public void adiciona(Empresa empresa) {
		empresa.setId(Banco.chaveSequencial++);
		Banco.lista.add(empresa);
	}
	
	public List<Empresa> getEmpresas(){
		return Banco.lista;
	}

	public void removeEmpresa(Integer id) {
		
		Iterator<Empresa> it = lista.iterator();
		
		while(it.hasNext()) {
			Empresa emp = it.next();
			
			if(emp.getId() == id) {
				it.remove();
			}
		}
	}

	public Empresa buscaEmpresaPelaId(Integer id) {
		for (Empresa empresa : lista) {
			if(empresa.getId() == id) {
				return empresa;
			}
		}
		return null;
	}

	public Usuario existeUsuario(String login, String senha) {
		for(Usuario usuario : listaUsuarios) {
			if(usuario.ehIgual(login, senha)) {
				return usuario;
			}
		}
		return null;
	}

}
