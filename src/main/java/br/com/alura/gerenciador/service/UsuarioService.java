package br.com.alura.gerenciador.service;

import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.repository.UsuarioRepository;
import br.com.alura.gerenciador.repository.UsuarioRepositoryJPA;
import br.com.alura.gerenciador.validation.ValidatorUtil;
import jakarta.persistence.EntityManager;

public class UsuarioService {

	private UsuarioRepository repository;
	
	public UsuarioService(EntityManager em) {
		this.repository = new UsuarioRepositoryJPA(em);
	}
	
	
	public void cadastraUsuario(NovoUsuarioDTO dto) {
		ValidatorUtil.valida(dto);
		repository.persist(new Usuario(dto));
	}
	
	public Usuario buscaUsuarioPorLogin(String login) {
		return repository.findByLogin(login);
	}
	
	public boolean verificaSeLoginExiste(String login) {
		return repository.existsByLogin(login);
	}
}
