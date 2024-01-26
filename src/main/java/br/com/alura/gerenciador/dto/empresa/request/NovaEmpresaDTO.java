package br.com.alura.gerenciador.dto.empresa.request;

import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import jakarta.validation.Valid;

public record NovaEmpresaDTO(
		@Valid
		EmpresaBaseDTO base,
		
		Usuario usuario) {
}
