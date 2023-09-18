package main.java.br.com.alura.gerenciador.repository;

import javax.transaction.Transactional;

import main.java.br.com.alura.gerenciador.modelo.Usuario;

public interface UsuarioRepository {

	@Transactional
	public abstract void persist(Usuario produto);
	
	public abstract Usuario findByLogin(String login);

	@Transactional
	public abstract void update(String login);
}
