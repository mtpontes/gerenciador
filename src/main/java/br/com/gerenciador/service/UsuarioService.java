package br.com.gerenciador.service;

import br.com.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.repository.UsuarioRepository;
import br.com.gerenciador.repository.UsuarioRepositoryJPA;
import br.com.gerenciador.validation.ValidatorUtil;
import jakarta.persistence.EntityManager;

public class UsuarioService {

	private UsuarioRepository repository;
	private ValidatorUtil validator = new ValidatorUtil();

	
	public UsuarioService(EntityManager em) {
		this.repository = new UsuarioRepositoryJPA(em);
	}
	
	public void cadastraUsuario(NovoUsuarioDTO dto) {
		validator.valida(dto);
		repository.persist(new Usuario(dto));
	}
	
	public Usuario buscaUsuarioPorLogin(String login) {
		return repository.findByLogin(login);
	}
	
	public boolean verificaSeLoginExiste(String login) {
		return repository.existsByLogin(login);
	}
}
