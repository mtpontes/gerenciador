package br.com.alura.gerenciador.repository;

import java.util.List;

import br.com.alura.gerenciador.modelo.Empresa;
import jakarta.transaction.Transactional;

public interface EmpresaRepository {
	
	@Transactional
	public void persist(Empresa empresa);
	
	@Transactional
	public void update(Empresa empresa);

	public Empresa findEmpresaById(Long id);
	
	public List<Empresa> findEmpresas();

	public List<Empresa> findEmpresasByUsuarioId(Long id);
	
}