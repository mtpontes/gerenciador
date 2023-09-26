package main.java.br.com.alura.gerenciador.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	public static String formatDate(LocalDate date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(formatter);
	}
}
