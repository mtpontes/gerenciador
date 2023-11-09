package main.java.br.com.alura.gerenciador.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import main.java.br.com.alura.gerenciador.modelo.Empresa;

public class EmpresaRepositoryMySQL implements EmpresaRepository {
	
	private EntityManager em;
	public EmpresaRepositoryMySQL(EntityManager em) {
		this.em  = em;
	}
	
	@Transactional
	public void persist(Empresa empresa) {
		try {
			em.getTransaction().begin();
			em.persist(empresa);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			em.getTransaction().rollback();
			System.out.println("Ocorreu um erro ao persistir a entidade Empresa - " + e.getMessage());
		}
	}
	
	@Transactional
	public void update(Empresa empresa) {
		try {
			em.getTransaction().begin();
			em.merge(empresa);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			em.getTransaction().rollback();
			System.out.println("Erro ao atualizar a entidade Empresa - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public List<Empresa> findEmpresas(){
		try {
			Query query = em.createNativeQuery("SELECT * FROM empresas WHERE ativo = 1", Empresa.class);

			return query.getResultList();
		} catch (PersistenceException e) {
			System.out.println("Erro ao consultar uma lista de Empresas - " + e.getMessage());
			return null;
		}
	}

	public Empresa findEmpresaById(Long id) {
		try {
			Query query = em.createNativeQuery("SELECT * FROM empresas WHERE id =:id", Empresa.class);
			query.setParameter("id", id);

			return (Empresa) query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Erro ao consultar Empresa pelo ID - " + e.getMessage());
			return null;
		}
	}

	public List<Empresa> findEmpresasOfUsuarioById(Long id) {
		try {
			Query query = em.createNativeQuery("SELECT * FROM empresas WHERE usuario_id =:id", Empresa.class);
			query.setParameter("id", id);
			
			return query.getResultList();
		} catch (PersistenceException e) {
			System.out.println("Erro ao consultar Empresas relacionadas a Usuario - " + e.getMessage() );
			return null;
		}
	}

	public List<Empresa> findEmpresasOfUsuarioByIdAndAtivoTrue(Long id) {
		try {
			Query query =  em.createNativeQuery("SELECT * FROM empresas WHERE usuario_id =:id AND ativo =1", Empresa.class);
			query.setParameter("id", id);
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao consultar Empresas ativas relacionadas a Usuario - " + e.getMessage());
			return null;
		}
	}
}
