package br.com.gerenciador.unit.validation.annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.util.LocalDateUtil;
import br.com.gerenciador.validation.annotation.DataNaoFuturaValidation;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class DataNaoFuturaValidationTest {
	
	@InjectMocks
	private DataNaoFuturaValidation validation;
	@Mock
	private ConstraintValidatorContext context;

	private static final String DATA_VALIDA = LocalDateUtil.formatLocalDateToString(LocalDate.now());
	private static final String DATA_INVALIDA_FUTURO = LocalDateUtil.formatLocalDateToString(LocalDate.now().plusYears(1));
	private List<String> DATAS_FORMATO_INVALIDO = List.of(
		"01-01-2023",
		"2101/01/23",
		"01/13/2023",
		"32/01/2023"
		);

	
	@Test
	void isValidTest01() {
		assertTrue(validation.isValid(DATA_VALIDA, context));
		assertFalse(validation.isValid(DATA_INVALIDA_FUTURO, context));
	}
	
	@Test
	@DisplayName("Retorna false para datas em formato inválido")
	void isValidTest02() {
		BDDMockito.given(context.buildConstraintViolationWithTemplate(Mockito.anyString()))
			.willReturn(BDDMockito.mock(ConstraintViolationBuilder.class));

		DATAS_FORMATO_INVALIDO.forEach(data -> {
			assertFalse(validation.isValid(data, context));
		});
	}
}
