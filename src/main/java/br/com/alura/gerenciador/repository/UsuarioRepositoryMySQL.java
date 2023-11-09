package main.java.br.com.alura.gerenciador.repository;

import java.math.BigInteger;

import org.hibernate.exception.JDBCConnectionException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import main.java.br.com.alura.gerenciador.modelo.Usuario;

public class UsuarioRepositoryMySQL implements UsuarioRepository{
	
	private EntityManager em;
	public UsuarioRepositoryMySQL(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	
	@Transactional
	public void persist(Usuario usuario) {
		try {
			em.getTransaction().begin();
			this.em.persist(usuario);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			em.getTransaction().rollback();
			System.out.println("Erro ao cadastrar Usuario - " + e.getMessage());
		}
	}
	
	@Transactional
	public void update(Usuario usuario) {
		try {
			em.getTransaction().begin();
			em.merge(usuario);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			System.out.println("Erro ao fazer update em Usuario - " + e.getMessage());
		}
	}
	
	public Usuario findByLogin(String login) {
		try {
			Query query = em.createNativeQuery("SELECT * FROM usuarios WHERE login =:login", Usuario.class);
			query.setParameter("login", login);
			return(Usuario) query.getSingleResult();
			
		} catch (NoResultException e) {
			System.out.println("Nenhum resultado foi encontrado na consulta por login");
			return null;
		} catch(PersistenceException e) {
			Throwable cause =  e.getCause();
			
			if(cause instanceof JDBCConnectionException) {
				System.out.println("FALHA NA CONEXÃO! - Uma Excessão JDBCOnnection foi lançada ao consultar Usuario");
			} else {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public boolean existsByLogin(String login) {
	    try {
	        Query query = em.createNativeQuery("SELECT EXISTS(SELECT * FROM usuarios WHERE login =:login)");
	        query.setParameter("login", login);
	        
	        BigInteger resposta = (BigInteger) query.getSingleResult(); 
	        
	        return !resposta.equals(BigInteger.ZERO);
	    } catch (Exception e) {
	        System.out.println("Erro ao verificar a existência de login de Usuario - " + e.getMessage());
	        return false;
	    }
	}

	public boolean existsByID(Long id) {
	    try {
	        Query query = em.createNativeQuery("SELECT EXISTS(SELECT * FROM usuarios WHERE id =:id)");
	        query.setParameter("id", id);
	        
	        BigInteger resposta = (BigInteger) query.getSingleResult(); 
	        
	        return !resposta.equals(BigInteger.ZERO);
	    } catch (Exception e) {
	        System.out.println("Erro ao verificar a existência do ID de Usuario - " + e.getMessage());
	        return false;
	    }
	}
}
