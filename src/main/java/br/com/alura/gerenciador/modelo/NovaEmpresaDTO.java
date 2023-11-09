package main.java.br.com.alura.gerenciador.modelo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record NovaEmpresaDTO(
		
		@NotBlank(message = "VALIDATION ERROR EMPRESA: Nome não pode ser nulo")
		@Pattern(regexp = "^[A-Za-z0-9\\s.'-]{1,100}$", message = "VALIDATION ERROR EMPRESA: Nome pode só pode ter letras, pontos, apóstrofos e hífens")
		String nome,
		
		@NotBlank(message = "VALIDATION ERROR: Data não pode ser nulo")
		@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[0-2])\\/\\d{4}$", message = "VALIDATION ERROR EMPRESA: Data não está no formado dd/MM/yyyy")
		String data,
		
		@NotNull(message = "VALIDATION ERROR EMPRESA: O ID do usuário não pode ser nulo")
		Long usuarioID) {
}
