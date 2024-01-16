package br.com.alura.gerenciador.dto.empresa;

import br.com.alura.gerenciador.modelo.Usuario;
import jakarta.validation.Valid;

public record NovaEmpresaDTO(
		@Valid
		EmpresaBaseDTO base,
		
		Usuario usuario) {
	
	EmpresaBaseDTO getBase() {
		return this.base;
	}
	
	String getNome() {
		return this.base.nome();
	}
	String getData() {
		return this.base.data();
	}
}
