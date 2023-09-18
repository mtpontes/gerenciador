package main.java.br.com.alura.gerenciador.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import main.java.br.com.alura.gerenciador.modelo.Usuario;

public class UsuarioRepository {

	private EntityManager em;
	
	public UsuarioRepository(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	@Transactional
	public void cadastrar(Usuario produto) {
		this.em.persist(produto);
	}
	
	
	public Usuario buscarPorLogin(String login) {
		Query query = em.createNativeQuery("SELECT * FROM usuarios WHERE login =:login", Usuario.class);
		query.setParameter("login", login);
		try {
			return(Usuario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
