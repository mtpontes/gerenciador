package br.com.gerenciador.validation.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class LoginDisponivelValidatorTest {

	@InjectMocks
	private LoginDisponivelValidator loginDisponivelValidator;
	@Mock
	private ConstraintValidatorContext context;
	@Mock
	private UsuarioRepository repository;
	@Mock
	private EntityManager em;
	@Mock
	private Query query;
	
	private static final String LOGIN = "umLogin";
	private static final Boolean VALID = false;
	private static final Boolean INVALID = true;
	
	
	@Test
	@DisplayName("Deveria retornar true quando login é válido")
	void isValidTest01() {
		//arrange
		BDDMockito.given(repository.existsByLogin(LOGIN)).willReturn(VALID);
		// act/assert
		Assertions.assertTrue(() -> loginDisponivelValidator.isValid(LOGIN, context));
	}
	@Test
	@DisplayName("Deveria retornar false quando login é inválido")
	void isValidTest02() {
		//arrange
		BDDMockito.given(repository.existsByLogin(LOGIN)).willReturn(INVALID);
		// act/asert
		Assertions.assertFalse(() -> loginDisponivelValidator.isValid(LOGIN, context));
	}
}
