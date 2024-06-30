package br.com.gerenciador.service;

import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.usuario.NovoUsuarioDTO;
import br.com.gerenciador.repository.UsuarioRepository;
import br.com.gerenciador.repository.UsuarioRepositoryJPA;
import br.com.gerenciador.validation.ValidatorUtil;

public class UsuarioService {

	private UsuarioRepository repository;
	private ValidatorUtil validator;

	
	public UsuarioService() {
		this.repository = new UsuarioRepositoryJPA();
		this.validator = new ValidatorUtil();
	}
	public UsuarioService(UsuarioRepository repository, ValidatorUtil validatorUtil) {
		this.repository = repository;
		this.validator = validatorUtil;
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