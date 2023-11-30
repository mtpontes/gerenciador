package br.com.alura.gerenciador.service;

import java.util.List;

import br.com.alura.gerenciador.dto.empresa.AlteraEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.dto.empresa.ListaEmpresasUsuarioDTO;
import br.com.alura.gerenciador.dto.empresa.NovaEmpresaDTO;
import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.repository.EmpresaRepository;
import br.com.alura.gerenciador.repository.EmpresaRepositoryJPA;
import br.com.alura.gerenciador.util.LocalDateUtil;
import br.com.alura.gerenciador.validation.ValidatorUtil;
import jakarta.persistence.EntityManager;

public class EmpresaService {

	private EmpresaRepository repository;
	
	public EmpresaService(EntityManager em) {
		this.repository = new EmpresaRepositoryJPA(em);
	}
	
	
	public List<EmpresaBaseDTO> consultaEmpresas() {
		return repository.findEmpresas().stream().map(EmpresaBaseDTO::new).toList();
	}
	public List<ListaEmpresasUsuarioDTO> consultaEmpresasUsuario(Long id) {
		return repository.findEmpresasByUsuarioId(id).stream().map(ListaEmpresasUsuarioDTO::new).toList();
	}
	
	public void alteraDadosEmpresa(AlteraEmpresaDTO dto) {
		System.out.println("Service - alteraDadosEmpresa!");

		ValidatorUtil.valida(dto);
		
		Empresa empresa = repository.findEmpresaById(dto.id());
		empresa.alteraDados(dto.base().nome(), LocalDateUtil.formatStringToLocalDate(dto.base().data()));
		repository.update(empresa);
	}
	
	public void cadastraEmpresa(NovaEmpresaDTO dto) {
		System.out.println("Service - cadastraEmpresa!");
		
		ValidatorUtil.valida(dto);
		repository.persist(new Empresa(dto));
	}
	
	public void atualizaEmpresa(Long id) {
		System.out.println("removeEmpresa!");
		
		Empresa empresa = repository.findEmpresaById(id);
				
		if(empresa != null) {
			empresa = empresa.removeOrRestoreEmpresa();
			repository.update(empresa);
		} 
	}
}
