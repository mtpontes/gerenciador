package main.java.br.com.alura.gerenciador.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import main.java.br.com.alura.gerenciador.validation.LoginDisponivelAnnotation;

public record NovoUsuarioDTO(
		@NotBlank(message = "VALIDATION ERROR: Nome não pode ser nulo")
		@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' _-]+$", message = "VALIDATION ERROR: Nome só permite letras, apóstrofo (\\') e hífen (-)")
		String nome,
		
		@NotBlank(message = "VALIDATION ERROR: Login não pode ser nulo")
		@Size(min = 3, message = "VALIDATION ERROR: Login precisa ter no mínimo 3 caracteres")
		@LoginDisponivelAnnotation(message = "VALIDATION ERROR: Login já existe no banco de dados")
		String login,
		
		@NotBlank(message = "VALIDATION ERROR: Senha não pode ser nulo")
		@Size(min = 8, message = "VALIDATION ERROR: Senha precisa ter no mínimo 8 caracteres")
		String senha,
		
		@NotBlank(message = "VALIDATION ERROR: Confirma não pode ser nulo")
		String confirma) {
	
		@AssertTrue(message = "VALIDATION ERROR: Confirma deve ser igual a senha")
		boolean isSenhaConfirmada() {
			return senha.equals(confirma);
		}
}
