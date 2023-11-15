package main.java.br.com.alura.gerenciador.service;

import jakarta.persistence.EntityManager;
import main.java.br.com.alura.gerenciador.dto.NovoUsuarioDTO;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepository;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepositoryMySQL;
import main.java.br.com.alura.gerenciador.validation.ValidatorUtil;

public class UsuarioService {

	private UsuarioRepository repository;
	
	public UsuarioService(EntityManager em) {
		this.repository = new UsuarioRepositoryMySQL(em);
	}
	
	
	public void cadastraUsuario(String nome, String login, String senha, String confirma) {
		System.out.println("UsuarioService - cadastraUsuario!");
		
		NovoUsuarioDTO dto = new NovoUsuarioDTO(nome, login, senha, confirma);
		ValidatorUtil.valida(dto);
		
		repository.persist(new Usuario(dto));
	}
	
	public Usuario getUsuarioPorLogin(String login) {
		return repository.findByLogin(login);
	}
}
