package main.java.br.com.alura.gerenciador.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.modelo.UsuarioDto;

public class UsuarioRepositoryMySQL implements UsuarioRepository{
	
	private EntityManager em;
	public UsuarioRepositoryMySQL(EntityManager entityManager) {
		this.em = entityManager;
	}

	
	
	@Transactional
	public void persist(Usuario usuario) {
		em.getTransaction().begin();
		this.em.persist(usuario);
		em.getTransaction().commit();
	}
	
	@Transactional
	public void update(Usuario usuario) {
		em.merge(usuario);
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
}
