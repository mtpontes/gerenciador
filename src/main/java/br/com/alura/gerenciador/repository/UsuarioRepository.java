package br.com.alura.gerenciador.repository;

import br.com.alura.gerenciador.modelo.Usuario;
import jakarta.transaction.Transactional;

	
public interface UsuarioRepository {

	@Transactional
	public abstract void persist(Usuario usuario);
	
	@Transactional
	public abstract void update(Usuario usuario);
	
	public abstract Usuario findByLogin(String login);
	
	boolean existsByLogin(String login);
}

