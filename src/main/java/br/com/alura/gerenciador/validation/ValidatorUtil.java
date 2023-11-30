package br.com.alura.gerenciador.validation;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidatorUtil {

	private static ValidatorFactory factoryValidator = Validation.buildDefaultValidatorFactory();
	
	public static Validator getValidator() {
		return factoryValidator.getValidator();
	}
	
	public static void valida(Record record) {
		Validator validator = getValidator();
		Set<ConstraintViolation<Record>> violations = validator.validate(record);
		
		if(!violations.isEmpty()) {
			violations.forEach(msg -> System.out.println(msg.getMessage()));
			throw new FormValidationException("Erro na validação!");
		}
	}
}
