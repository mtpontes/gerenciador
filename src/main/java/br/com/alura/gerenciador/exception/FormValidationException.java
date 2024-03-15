package br.com.alura.gerenciador.exception;

public class FormValidationException extends RuntimeException{
	public FormValidationException(String message) {
		super(message);
	}
}
