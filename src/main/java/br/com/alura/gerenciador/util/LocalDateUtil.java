package br.com.alura.gerenciador.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {

	public static String formatLocalDateToString(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return date.format(formatter);
	}
	
	public static LocalDate formatStringToLocalDate(String string) {
		return LocalDate.parse(string, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}
