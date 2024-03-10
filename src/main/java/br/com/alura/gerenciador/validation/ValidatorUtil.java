package br.com.alura.gerenciador.validation;

import java.util.Set;

import com.google.gson.JsonObject;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidatorUtil {

	private static ValidatorFactory factoryValidator = Validation.buildDefaultValidatorFactory();
	
	private static Validator getValidator() {
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
	
	public void validaJson(Record record) {
		Validator validator = getValidator();
		Set<ConstraintViolation<Record>> violations = validator.validate(record);
		
		JsonObject jsonErrors = new JsonObject();
		violations.forEach(violation -> {
			String field = violation.getPropertyPath().toString();
			String message = violation.getMessage() + " - Valor inserido - " + violation.getInvalidValue();
			if(field.contains(".")) {
				int indexLastDot = field.lastIndexOf(".");
				field = field.substring(indexLastDot +1);
			};
			
			jsonErrors.addProperty(field, message);
		});
		
		if(!jsonErrors.isEmpty()) {
			JsonObject jsonMessage = new JsonObject();
			jsonMessage.addProperty("message", "houve um ou mais erros na validação dos dados enviados");
			jsonMessage.add("erros", jsonErrors);
			
			throw new FormValidationException(jsonMessage.toString());
		}
	}
}
