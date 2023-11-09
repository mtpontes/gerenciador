package main.java.br.com.alura.gerenciador.repository;

import jakarta.transaction.Transactional;
import main.java.br.com.alura.gerenciador.modelo.Usuario;

	
public interface UsuarioRepository {

	@Transactional
	public abstract void persist(Usuario usuario);
	
	@Transactional
	public abstract void update(Usuario usuario);
	
	public abstract Usuario findByLogin(String login);
	
	boolean existsByLogin(String login);
	
	boolean existsByID(Long id);
}

