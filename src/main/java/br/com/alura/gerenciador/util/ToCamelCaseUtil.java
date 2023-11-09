package main.java.br.com.alura.gerenciador.util;

public class ToCamelCaseUtil {
	
	public static String toCamelCase(String param) {
		String firstIndex = String.valueOf(param.charAt(0));
		String lowerCase = firstIndex.toLowerCase();
		
		String resultado = param.replaceFirst(firstIndex, lowerCase);
		
		return resultado;
	}
}