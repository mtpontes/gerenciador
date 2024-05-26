package br.com.gerenciador.unit.validation.patterns;

import java.time.Year;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.validation.patterns.ValidationPatternsEmpresa;

@ExtendWith(MockitoExtension.class)
class ValidationPatternsEmpresaTest {

	private ValidationPatternsEmpresa empresaPatterns;
	private List<String> nomesValidos = List.of(
			"Um Nome Normal Com Espaço",
			"Nome.Composto-D'ExemploComPontoHífenApóstrofo",
			"Nome123ComNumeros",
			"NomeComAcentos AaÀàÖøÇçÃÕÑ"
			);
	private List<String> nomesInvalidos = List.of(
			"Nome@Inválido",
			"Nom!Inválido",
			"Nome#Inválido"
			);
	private static final String DATA_VALIDA = "01/01/"+ Year.now().minusYears(1);
	private List<String> datasInvalidas = List.of(
			"01-01-2023",
			"2101/01/23",
			"01/13/2023",
			"32/01/2023"
			);
	
	
	@Test
	void regexNomeTest() {
		//arange
		Pattern patternNome = Pattern.compile(empresaPatterns.NOME_EMPRESA_REGEX_PATTERN);

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

	@Test
	void regexDataTest() {
		//arrange
		Pattern patternData = Pattern.compile(empresaPatterns.DATA_EMPRESA_REGEX_PATTERN);

		// act/assert
		Assertions.assertTrue(patternData.matcher(DATA_VALIDA).matches());
		
		datasInvalidas.forEach(dataInvalida -> {
			boolean matchInvalid = patternData.matcher(dataInvalida).matches();
			Assertions.assertFalse(matchInvalid);
		});
	}
}
