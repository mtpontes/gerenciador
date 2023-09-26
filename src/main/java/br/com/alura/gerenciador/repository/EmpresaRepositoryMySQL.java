package main.java.br.com.alura.gerenciador.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import main.java.br.com.alura.gerenciador.modelo.Empresa;

public class EmpresaRepositoryMySQL implements EmpresaRepository {
	
	private EntityManager em;
	
	public EmpresaRepositoryMySQL(EntityManager em) {
		this.em  = em;
	}
	
	
	public void persist(Empresa empresa) {
		em.getTransaction().begin();
		em.persist(empresa);
		em.getTransaction().commit();
	}
	
	public List<Empresa> findEmpresas(){
		return em.createNativeQuery("SELECT * FROM empresas WHERE ativo = 1", Empresa.class).getResultList();
	}

	public Empresa findEmpresaById(Long id) {
		Query query = em.createNativeQuery("SELECT * FROM empresas WHERE id =:id", Empresa.class).setParameter("id", id);
		
		try {
			return (Empresa) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void update(Empresa empresa) {
		em.getTransaction().begin();
		Empresa empresaDB = findEmpresaById(empresa.getId());
		if (empresaDB != null) {
			empresaDB.alteraDados(empresa.getNome(), empresa.getDataAbertura());
			em.merge(empresaDB);
		}
		em.getTransaction().commit();
	}


	public List<Empresa> findEmpresasDoUsuario(Long id) {
		return em.createNativeQuery("SELECT * FROM empresas WHERE usuario_id =:id", Empresa.class)
				.setParameter("id", id).getResultList();
	}

}
