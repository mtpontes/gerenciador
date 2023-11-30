package br.com.alura.gerenciador.validation.patterns;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidationPatternsUsuarioTest {

	private ValidationPatternsUsuario usuarioPatterns;
	
	private static final List<String> nomesValidos = List.of(
			"João",
			"O'Connor",
			"José Álvares",
			"AaÀàÖø",
			"Mary-Jane");
	
    private static final List<String> nomesInvalidos = List.of(
				"Nome@ComCaracteresEspeciais",
				"Nome!ComCaracteresEspeciais",
				"Nome#ComCaracteresEspeciais",
				"Nome$ComCaracteresEspeciais",
				"Nome%ComCaracteresEspeciais",
				"Nome&ComCaracteresEspeciais",
				"Nome*ComCaracteresEspeciais",
				"Nome()ComCaracteresEspeciais",
				"Nome=ComCaracteresEspeciais",
				"Nome+ComCaracteresEspeciais"
	);
	
	
	@Test
	void regexNomeTest() {
		//arrange
		Pattern patternNome = Pattern.compile(usuarioPatterns.NOME_USUARIO_REGEX_PATTERN);
		
		// act/assert
		nomesValidos.forEach(nomeValido -> {
			boolean matchValid = patternNome.matcher(nomeValido).matches();
			Assertions.assertTrue(matchValid);
		});
		
		nomesInvalidos.forEach(nomeInvalido -> {
			boolean matchInvalid = patternNome.matcher(nomeInvalido).matches();
			Assertions.assertFalse(matchInvalid);
		});
	}
}
