package br.com.alura.gerenciador.validation.patterns;

public class ValidationPatternsEmpresa {
	public static final String NOME_EMPRESA_REGEX_PATTERN = "^[\\p{L}0-9\\s.'-]{1,100}$";
	public static final String NOME_EMPRESA_ERROR_MESSAGE = "VALIDATION ERROR EMPRESA: Nome pode só pode ter letras, pontos, apóstrofos e hífens";
	
	public static final String DATA_EMPRESA_REGEX_PATTERN = "^(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[0-2])\\/\\d{4}$";
	public static final String DATA_EMPRESA_ERROR_FORMAT_MESSAGE = "VALIDATION ERROR EMPRESA: Data não está no formato dd/MM/yyyy";
	public static final String DATA_EMPRESA_ERROR_FUTURE_MESSAGE = "VALIDATION ERROR EMPRESA: Data não pode ser superior que a data presente";
}
