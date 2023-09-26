package main.java.br.com.alura.gerenciador.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import main.java.br.com.alura.gerenciador.modelo.Empresa;

public interface EmpresaRepository {
	
	@Transactional
	public void persist(Empresa empresa);
	
	public List<Empresa> findEmpresas();

	public Empresa findEmpresaById(Long id);
	
	@Transactional
	public void update(Empresa empresa);

	public List<Empresa> findEmpresasDoUsuario(Long id);
	
}