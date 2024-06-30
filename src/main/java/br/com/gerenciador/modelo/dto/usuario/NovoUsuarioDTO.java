package br.com.gerenciador.modelo.dto.usuario;

import br.com.gerenciador.validation.patterns.ValidationPatternsUsuario;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NovoUsuarioDTO(
	
	@NotBlank(message = "Nome não pode ser nulo")
	@Pattern(regexp = ValidationPatternsUsuario.NOME_USUARIO_REGEX_PATTERN, message = ValidationPatternsUsuario.NOME_USUARIO_ERROR_MESSAGE)
	String nome,
	
	@NotBlank(message = "Login não pode ser nulo")
	@Size(min = 3, message = "Login precisa ter no mínimo 3 caracteres")
	String login,
	
	@NotBlank(message = "Senha não pode ser nulo")
	@Size(min = 8, message = "Senha precisa ter no mínimo 8 caracteres")
	String senha,
	
	@NotBlank(message = "Confirma não pode ser nulo")
	String confirma) {

	@AssertTrue(message = "Confirma deve ser igual a senha")
	boolean isSenhaConfirmada() {
		return this.senha.equals(confirma);
	}
}