package br.com.alura.gerenciador.service;

import java.util.List;

import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.dto.empresa.request.AlteraEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.request.NovaEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.repository.EmpresaRepository;
import br.com.alura.gerenciador.repository.EmpresaRepositoryJPA;
import br.com.alura.gerenciador.util.LocalDateUtil;
import br.com.alura.gerenciador.validation.FormValidationException;
import br.com.alura.gerenciador.validation.ValidatorUtil;
import jakarta.persistence.EntityManager;

public class EmpresaService {

	private EmpresaRepository repository;
	private ValidatorUtil validator = new ValidatorUtil();

	
	public EmpresaService(EntityManager em) {
		this.repository = new EmpresaRepositoryJPA(em);
	}
	
	
	public void cadastraEmpresa(NovaEmpresaDTO dto) {
		ValidatorUtil.valida(dto);
		repository.persist(new Empresa(dto));
	}
	
	public void alteraDadosEmpresa(AlteraEmpresaDTO dto, Usuario usuario) {
		validator.validaJson(dto);
		Empresa empresa = repository.findById(dto.id());
		
		if(empresa.getUsuario().getId() != usuario.getId()) {
			throw new FormValidationException("usuario sem autorizacao");
		}
		
		empresa.alteraDados(dto.base().nome(), LocalDateUtil.formatStringToLocalDate(dto.base().data()));
		repository.update(empresa);
	}
	
	public void arquivaEmpresa(Long empresaId, Long usuarioId) {
		Empresa empresa = repository.findById(empresaId);
				
		if(empresa.getUsuario().getId().equals(usuarioId)) {
			empresa = empresa.removeOrRestoreEmpresa();
			repository.update(empresa);
		} else {
			throw new FormValidationException("usuario sem autorização");
		}
	}
	
	
	
	@Deprecated
	public List<EmpresaBaseDTO> pesquisaEmpresas(String nomeDaEmpresa) {
		return repository.searchEmpresasByNameLike(nomeDaEmpresa).stream().map(EmpresaBaseDTO::new).toList();
	}
	public List<EmpresaBaseDTO> getEmpresasByNamePaged(String nomeEmpresa, Integer start, Integer max) {
		if(nomeEmpresa == null || nomeEmpresa.trim().isEmpty()) {
			return null;
		}
		return repository.findByNameLikePaged(nomeEmpresa, start, max).stream().map(EmpresaBaseDTO::new).toList();
	}
	
	
	@Deprecated
	public List<EmpresaBaseDTO> consultaEmpresas() {
		return repository.findEmpresas().stream().map(EmpresaBaseDTO::new).toList();
	}
	public List<EmpresaBaseDTO> getEmpresasPaged(Integer start, Integer max){
		return repository.findAllPaged(start, max).stream().map(EmpresaBaseDTO::new).toList();
	}
	
	
	@Deprecated
	public List<ListaEmpresasUsuarioDTO> consultaEmpresasUsuario(Long id) {
		return repository.findEmpresasByUsuarioId(id).stream().map(ListaEmpresasUsuarioDTO::new).toList();
	}
	public List<ListaEmpresasUsuarioDTO> getEmpresasAtivoUsuarioPaged(Long id, Integer start, Integer max, Boolean ativo) {
		return repository.findByUsuarioIdAndAtivoPaged(id, start, max, ativo).stream().map(ListaEmpresasUsuarioDTO::new).toList();
	}
	
	
	public Long getCountEmpresas() {
		return repository.countByAtivoTrue();
	}
	public Long getCountEmpresasUsuarioAtivo(Long id, Boolean ativo) {
		return repository.countByUsuarioAndAtivo(id, ativo);
	}
	public Long getCountEmpresasSearch(String nomeEmpresa) {
		if(nomeEmpresa == null || nomeEmpresa.trim().isEmpty()) {
			return 0L;
		}
		return repository.countByParamSearch(nomeEmpresa);
	}
}
