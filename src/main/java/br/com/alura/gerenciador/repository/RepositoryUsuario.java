package main.java.br.com.alura.gerenciador.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import main.java.br.com.alura.gerenciador.modelo.Usuario;

public class RepositoryUsuario implements UsuarioRepository{
	
	private EntityManager em;
	
	public RepositoryUsuario(EntityManager entityManager) {
		this.em = entityManager;
	}

	
	@Transactional
	public void persist(Usuario produto) {
		em.getTransaction().begin();
		this.em.persist(produto);
		em.getTransaction().commit();
	}

	public Usuario findByLogin(String login) {
		Query query = em.createNativeQuery("SELECT * FROM usuarios WHERE login =:login", Usuario.class);
		query.setParameter("login", login);
		try {
			return(Usuario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	@Override
	public void update(String login) {
		em.getTransaction().begin();
		em.getTransaction().commit();
	}

}
