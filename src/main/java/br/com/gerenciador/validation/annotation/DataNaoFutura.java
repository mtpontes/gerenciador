package br.com.gerenciador.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.gerenciador.validation.patterns.ValidationPatternsEmpresa;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataNaoFuturaValidation.class)
@Documented
public @interface DataNaoFutura {

    String message() default ValidationPatternsEmpresa.DATA_EMPRESA_ERROR_FUTURE_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}