package br.com.alura.gerenciador.dto.empresa;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AlteraEmpresaDTO(
		@NotNull(message = "VALIDATION ERROR EMPRESA: ID não pode ser nulo")
		Long id,
		
		@Valid
		EmpresaBaseDTO base
		) {
	
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
}