package br.com.gerenciador.repository;

import java.util.List;

import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Empresa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class EmpresaRepositoryJPA implements EmpresaRepository {
	
	private EntityManager em;
	
	public EmpresaRepositoryJPA(EntityManager em) {
		this.em  = em;
	}
	
	
	@Transactional
	public void persist(Empresa empresa) {
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(empresa);
			transaction.commit();
			
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new DatabaseAccessException("ocorreu um erro ao cadastrar empresa", e);
		}
	}
	
	@Transactional
	public void update(Empresa empresa) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(empresa);
			transaction.commit();
			
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new DatabaseAccessException("ocorreu um erro ao atualizar empresa", e);
		}
	}
	
	public Empresa findById(Long id) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.id =:id", Empresa.class);
			query.setParameter("id", id);
			return (Empresa) query.getSingleResult();
			
		} catch (NoResultException e) {
			throw new NoResultException("registro de empresa não encontrado");
		}
	}
	
	public List<Empresa> findAllPaged(Integer start, Integer max) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1", Empresa.class);
			query.setFirstResult(start);
			query.setMaxResults(max);
			
			return query.getResultList();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException("ocorreu um erro ao consultar todas as empresas", e);
		}
	}
	
	public List<Empresa> findByUsuarioIdAndAtivoPaged(Long id, Integer start, Integer max, Boolean ativo) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = :ativo AND e.usuario.id =:id", Empresa.class);
			query.setFirstResult(start);
			query.setMaxResults(max);
			query.setParameter("id", id);
			query.setParameter("ativo", ativo);
			return query.getResultList();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException("ocorreu um erro ao consultar todas as empresas do usuário", e);
		}
	}
	
	public List<Empresa> findByNameLikePaged(String nomeEmpresa, Integer start, Integer max){
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1 AND e.nome LIKE :nomeEmpresa", Empresa.class)
					.setParameter("nomeEmpresa", "%" + nomeEmpresa + "%");
			query.setFirstResult(start);
			query.setMaxResults(max);
			return query.getResultList();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException("ocorreu um erro ao consultar empresas pelo nome", e);
		}
	}
	
	
	public Long countByAtivoTrue() {
		try {
			Query query = em.createQuery("SELECT COUNT(e) FROM Empresa e WHERE e.ativo = 1");
			return (Long) query.getSingleResult();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException("ocorreu um erro ao contar empresas ativas", e);
		}
	}
	
	public Long countByUsuarioAndAtivo(Long id, Boolean ativo) {
		try {
			Query query = em.createQuery("SELECT COUNT(e) FROM Empresa e WHERE e.ativo = :ativo AND e.usuario.id = :id")
					.setParameter("id", id)
					.setParameter("ativo", ativo);
			return (Long) query.getSingleResult();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException("ocorreu um erro ao contar empresas ativas do usuário", e);
		}
	}
	
	public Long countByParamSearch(String nomeEmpresa) {
		try {
			Query query = em.createQuery("SELECT COUNT(e) FROM Empresa e WHERE e.ativo =1 AND e.nome LIKE :nomeEmpresa")
					.setParameter("nomeEmpresa", "%" + nomeEmpresa + "%");
			return (Long) query.getSingleResult();
			
		} catch (PersistenceException e) {
			throw new DatabaseAccessException("ocorreu um erro ao contar empresas pelo parâmetro consultado" ,e);
		}
	}
}
