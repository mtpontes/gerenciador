package main.java.br.com.alura.gerenciador.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidatorUtil {

	private static ValidatorFactory factoryValidator = Validation.buildDefaultValidatorFactory();
	
	public static Validator getValidator() {
		return factoryValidator.getValidator();
	}
}
