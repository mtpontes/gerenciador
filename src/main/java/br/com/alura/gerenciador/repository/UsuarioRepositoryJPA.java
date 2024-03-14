package br.com.alura.gerenciador.repository;

import br.com.alura.gerenciador.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class UsuarioRepositoryJPA implements UsuarioRepository{
	
	private EntityManager em;
	
	public UsuarioRepositoryJPA(EntityManager entityManager) {
		this.em = entityManager;
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
			throw new PersistenceException("ocorreu um erro no servidor ao cadastrar usuário", e);
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
			throw new PersistenceException("ocorreu um erro no servidor ao atualizar usuário", e);
		}
	}
	
	public Usuario findByLogin(String login) {
		try {
			Query query = em.createQuery("SELECT e FROM Usuario e WHERE e.login =:login", Usuario.class);
			query.setParameter("login", login);
			return(Usuario) query.getSingleResult();
			
		} catch (NoResultException e) {
			throw new NoResultException("nenhum resultado foi encontrado na consulta por login");
			
		} catch(PersistenceException e) {
			throw new PersistenceException("ocorreu um erro no servidor ao consultar login", e);
		}
	}
	
	public boolean existsByLogin(String login) {
		try {
			Query query = em.createQuery("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Usuario u WHERE u.login = :login");
			query.setParameter("login", login);
			
			return (boolean) query.getSingleResult();
			
		} catch (PersistenceException e) {
			throw new PersistenceException("ocorreu um erro interno no servidor", e);
		}
        		
	}

}
