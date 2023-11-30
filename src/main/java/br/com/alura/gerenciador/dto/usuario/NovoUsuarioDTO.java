package br.com.alura.gerenciador.dto.usuario;

import br.com.alura.gerenciador.validation.annotation.LoginDisponivelAnnotation;
import br.com.alura.gerenciador.validation.patterns.ValidationPatternsUsuario;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NovoUsuarioDTO(
		@NotBlank(message = "VALIDATION ERROR USUARIO: Nome não pode ser nulo")
		@Pattern(regexp = ValidationPatternsUsuario.NOME_USUARIO_REGEX_PATTERN, message = ValidationPatternsUsuario.NOME_USUARIO_ERROR_MESSAGE)
		String nome,
		
		@NotBlank(message = "VALIDATION ERROR USUARIO: Login não pode ser nulo")
		@Size(min = 3, message = "VALIDATION ERROR: Login precisa ter no mínimo 3 caracteres")
		@LoginDisponivelAnnotation
		String login,
		
		@NotBlank(message = "VALIDATION ERROR: Senha não pode ser nulo")
		@Size(min = 8, message = "VALIDATION ERROR USUARIO: Senha precisa ter no mínimo 8 caracteres")
		String senha,
		
		@NotBlank(message = "VALIDATION ERROR USUARIO: Confirma não pode ser nulo")
		String confirma) {
	
		@AssertTrue(message = "VALIDATION ERROR USUARIO: Confirma deve ser igual a senha")
		boolean isSenhaConfirmada() {
			return this.senha.equals(confirma);
		}
}
