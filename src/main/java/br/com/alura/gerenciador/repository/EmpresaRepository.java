package main.java.br.com.alura.gerenciador.repository;

import java.util.List;

import main.java.br.com.alura.gerenciador.modelo.Empresa;

public interface EmpresaRepository {
	
	public void persist(Empresa empresa);
	
	public List<Empresa> findEmpresas();

	public Empresa findEmpresaById(Long id);

	public void update(Empresa empresa);
}
