package br.com.gerenciador.repository;

import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class UsuarioRepositoryJPA implements UsuarioRepository{
	
	private EntityManager em;

	public UsuarioRepositoryJPA() {
		this.em = JPAUtil.getEntityManager();
	}
	public UsuarioRepositoryJPA(EntityManager entityManger) {
		this.em = entityManger;
	}
	
	
	@Transactional
	public void persist(Usuario usuario) {
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			this.em.persist(usuario);
			transaction.commit();
			
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new DatabaseAccessException(
				"ocorreu um erro ao cadastrar usuário", e);
		}
	}
	
	@Transactional
	public void update(Usuario usuario) {
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.merge(usuario);
			transaction.commit();
			
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new DatabaseAccessException(
				"ocorreu um erro ao atualizar usuário", e);
		}
	}
	
	public Usuario findByLogin(String login) {
		try {
			Query query = em.createQuery(
				"SELECT e FROM Usuario e WHERE e.login =:login", 
				Usuario.class
			);
			query.setParameter("login", login);
			return(Usuario) query.getSingleResult();
			
		} catch (NoResultException e) {
			throw new NoResultException(
				"nenhum resultado foi encontrado na consulta por login");
			
		} catch(PersistenceException e) {
			throw new DatabaseAccessException(
				"ocorreu um erro ao consultar login", e);
		}
	}
	
	public boolean existsByLogin(String login) {
		try {
			Query query = em.createQuery("""
					SELECT CASE WHEN COUNT(u) > 0 
					THEN TRUE ELSE FALSE END FROM Usuario u 
					WHERE u.login = :login
				"""
			);
			query.setParameter("login", login);
			
			return (boolean) query.getSingleResult();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException(
				"ocorreu um erro ao verificar existência de login", e);
		}
	}
}