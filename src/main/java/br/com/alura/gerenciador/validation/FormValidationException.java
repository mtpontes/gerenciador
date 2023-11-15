package main.java.br.com.alura.gerenciador.validation;

public class FormValidationException extends RuntimeException{

	public FormValidationException(String message) {
		super(message);
	}
}
