package main.java.br.com.alura.gerenciador.repository;

import java.util.List;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.transaction.Transactional;

import main.java.br.com.alura.gerenciador.modelo.Empresa;

public interface EmpresaRepository {
	
	@Transactional
	@PostPersist
	public void persist(Empresa empresa);
	
	public Empresa findEmpresaById(Long id);

	@Transactional
	@PostUpdate
	public void update(Empresa empresa);
	
	public List<Empresa> findEmpresas();

	public List<Empresa> findEmpresasOfUsuarioById(Long id);
	
	public List<Empresa> findEmpresasOfUsuarioByIdAndAtivoTrue(Long id);
	
}