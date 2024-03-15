package br.com.gerenciador.validation.patterns;

public class ValidationPatternsUsuario {
	public static final String NOME_USUARIO_REGEX_PATTERN = "^[A-Za-zÀ-ÖØ-öø-ÿ' _-]+$";
	public static final String NOME_USUARIO_ERROR_MESSAGE = "Nome só permite letras, apóstrofo (\\') e hífen (-)";
}
