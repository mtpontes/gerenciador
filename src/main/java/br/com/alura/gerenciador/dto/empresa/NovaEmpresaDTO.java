package br.com.alura.gerenciador.dto.empresa;

import br.com.alura.gerenciador.modelo.Usuario;
import jakarta.validation.Valid;

public record NovaEmpresaDTO(
		@Valid
		EmpresaBaseDTO base,
		
		Usuario usuario) {
}
