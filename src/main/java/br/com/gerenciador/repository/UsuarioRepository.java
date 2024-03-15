package br.com.gerenciador.repository;

import br.com.gerenciador.modelo.Usuario;
import jakarta.transaction.Transactional;

	
public interface UsuarioRepository {

	@Transactional
	public abstract void persist(Usuario usuario);
	
	@Transactional
	public abstract void update(Usuario usuario);
	
	public abstract Usuario findByLogin(String login);
	
	boolean existsByLogin(String login);
}

