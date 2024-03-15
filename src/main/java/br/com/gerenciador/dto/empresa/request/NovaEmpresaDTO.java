package br.com.gerenciador.dto.empresa.request;

import br.com.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.modelo.Usuario;
import jakarta.validation.Valid;

public record NovaEmpresaDTO(
		@Valid
		EmpresaBaseDTO base,
		
		Usuario usuario) {
}
