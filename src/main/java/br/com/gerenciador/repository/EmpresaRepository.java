package br.com.gerenciador.repository;

import java.util.List;

import br.com.gerenciador.modelo.Empresa;
import jakarta.transaction.Transactional;

public interface EmpresaRepository {
	
	@Transactional
	public void persist(Empresa empresa);
	@Transactional
	public void update(Empresa empresa);

	public Empresa findById(Long id);
	
	public List<Empresa> findAllPaged(Integer startIndex, Integer endIndex);

	public List<Empresa> findByUsuarioIdAndAtivoPaged(Long id, Integer start, Integer end, Boolean ativo);

	public List<Empresa> findByNameLikePaged(String nomeEmpresa, Integer start, Integer end);
	
	public Long countByAtivoTrue();
	public Long countByUsuarioAndAtivo(Long userId, Boolean empresaAtivo);
	public Long countByParamSearch(String nomeEmpresa);
}