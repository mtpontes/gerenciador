package br.com.gerenciador.modelo.dto.empresa.request;

import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import jakarta.validation.Valid;

public record NovaEmpresaDTO(
	@Valid
	EmpresaBaseDTO base,
		
	Usuario usuario
) {}