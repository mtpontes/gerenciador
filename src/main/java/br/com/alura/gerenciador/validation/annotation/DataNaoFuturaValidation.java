package br.com.alura.gerenciador.validation.annotation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import br.com.alura.gerenciador.util.LocalDateUtil;
import br.com.alura.gerenciador.validation.patterns.ValidationPatternsEmpresa;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataNaoFuturaValidation implements ConstraintValidator<DataNaoFutura, String> {

	@Override
	public void initialize(DataNaoFutura constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String data, ConstraintValidatorContext context) {
		try {
			LocalDate localDate = LocalDateUtil.formatStringToLocalDate(data);
			return !localDate.isAfter(LocalDate.now());
			
		} catch (DateTimeParseException e) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationPatternsEmpresa.DATA_EMPRESA_ERROR_FORMAT_MESSAGE).addConstraintViolation();
			return false;//data inválida
		}
	}	
}
