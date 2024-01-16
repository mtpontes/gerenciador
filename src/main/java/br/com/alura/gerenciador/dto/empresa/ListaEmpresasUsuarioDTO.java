package br.com.alura.gerenciador.dto.empresa;

import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.util.LocalDateUtil;

public record ListaEmpresasUsuarioDTO(
		Long id, 
		EmpresaBaseDTO base,
		Boolean ativo) {

	
	
	public ListaEmpresasUsuarioDTO(EmpresaBaseDTO dto) {
		this(null, new EmpresaBaseDTO(dto.getNome(), (dto.data())), null);
	}
	public ListaEmpresasUsuarioDTO(Empresa empresa) {
		this(empresa.getId(), new EmpresaBaseDTO(empresa.getNome(), LocalDateUtil.formatLocalDateToString(empresa.getDataAbertura())), empresa.getAtivo());
	}

	public Long getId() {
		return this.id;
	}
	public EmpresaBaseDTO getBase() {
		return this.base;
	}
	public String getNome() {
		return this.base.nome();
	}
	public String getData() {
		return this.base.data();
	}
	public Boolean getAtivo() {
		return this.ativo;
	}
}
