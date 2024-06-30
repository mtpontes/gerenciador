package br.com.gerenciador.unit.validation.patterns;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.validation.patterns.ValidationPatternsUsuario;

@ExtendWith(MockitoExtension.class)
class ValidationPatternsUsuarioTest {

	private final String NOME_USUARIO_REGEX_PATTERN = ValidationPatternsUsuario.NOME_USUARIO_REGEX_PATTERN;
	
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
		Pattern patternNome = Pattern.compile(NOME_USUARIO_REGEX_PATTERN);
		
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
