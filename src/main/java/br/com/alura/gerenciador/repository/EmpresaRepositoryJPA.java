package br.com.alura.gerenciador.repository;

import java.util.List;

import br.com.alura.gerenciador.modelo.Empresa;
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
		transaction.begin();
		em.persist(empresa);
		transaction.commit();
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
			e.printStackTrace();
		}
	}
	
	public Empresa findEmpresaById(Long id) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.id =:id", Empresa.class);
			query.setParameter("id", id);
			return (Empresa) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	
	@Deprecated
	public List<Empresa> findEmpresas(){
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1", Empresa.class);
			return query.getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Empresa> findEmpresasPaged(Integer start, Integer end){
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1", Empresa.class);
			query.setFirstResult(start);
			query.setMaxResults(end);
			return query.getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	@Deprecated
	public List<Empresa> findEmpresasByUsuarioId(Long id) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.usuario.id =:id", Empresa.class);
			query.setParameter("id", id);
			return query.getResultList();
			
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
			
		} catch(PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Empresa> findEmpresasPagedByUsuarioIdAndAtivo(Long id, Integer start, Integer end, Boolean ativo) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = :ativo AND e.usuario.id =:id", Empresa.class);
			query.setFirstResult(start);
			query.setMaxResults(end);
			query.setParameter("id", id);
			query.setParameter("ativo", ativo);
			return query.getResultList();
			
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
			
		} catch(PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Deprecated
	public List<Empresa> searchEmpresasByNameLike(String nomeEmpresa){
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1 AND e.nome LIKE :nomeEmpresa", Empresa.class)
					.setParameter("nomeEmpresa", "%" + nomeEmpresa + "%");
			return query.getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Empresa> searchEmpresasPagedByNameLike(String nomeEmpresa, Integer start, Integer end){
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1 AND e.nome LIKE :nomeEmpresa", Empresa.class)
					.setParameter("nomeEmpresa", "%" + nomeEmpresa + "%");
			query.setFirstResult(start);
			query.setMaxResults(end);
			return query.getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public Long countEmpresasByAtivoTrue() {
		try {
			Query query = em.createQuery("SELECT COUNT(e) FROM Empresa e WHERE e.ativo = 1");
			return (Long) query.getSingleResult();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	public Long countEmpresasUsuarioByAtivo(Long id, Boolean ativo) {
		try {
			Query query = em.createQuery("SELECT COUNT(e) FROM Empresa e WHERE e.ativo = :ativo AND e.usuario.id = :id")
					.setParameter("id", id)
					.setParameter("ativo", ativo);
			return (Long) query.getSingleResult();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	public Long countEmpresasByParamSearch(String nomeEmpresa) {
		try {
			Query query = em.createQuery("SELECT COUNT(e) FROM Empresa e WHERE e.ativo =1 AND e.nome LIKE :nomeEmpresa")
					.setParameter("nomeEmpresa", "%" + nomeEmpresa + "%");
			return (Long) query.getSingleResult();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return 0L;
		}
	}
}
