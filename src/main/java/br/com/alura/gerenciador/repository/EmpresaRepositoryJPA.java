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
		try {
			transaction.begin();
			em.persist(empresa);
			transaction.commit();
			
		} catch (PersistenceException e) {
			transaction.rollback();
			System.out.println("Ocorreu um erro ao persistir a entidade Empresa - " + e.getMessage());
			e.printStackTrace();
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
			System.out.println("Erro ao atualizar a entidade Empresa - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Empresa findEmpresaById(Long id) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.id =:id", Empresa.class);
			query.setParameter("id", id);
			return (Empresa) query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			System.out.println("Nenhum resultado foi encontrado na consulta - " + e.getMessage());
			return null;
		}
	}

	public List<Empresa> findEmpresas(){
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.ativo = 1", Empresa.class);

			System.out.println("VOU RETORNAR A LISTA");
			return query.getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			System.out.println("Erro ao consultar uma lista de Empresas - " + e.getMessage());
			return null;
		}
	}

	public List<Empresa> findEmpresasByUsuarioId(Long id) {
		try {
			Query query = em.createQuery("SELECT e FROM Empresa e WHERE e.usuario.id =:id", Empresa.class);
			query.setParameter("id", id);
			return query.getResultList();
			
		} catch (NoResultException e) {
			System.out.println("Nenhum resultado foi encontrado na consulta - " + e.getMessage() );
			e.printStackTrace();
			return null;
			
		} catch(PersistenceException e) {
			System.out.println("Erro ao consultar uma lista de Empresas - " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
