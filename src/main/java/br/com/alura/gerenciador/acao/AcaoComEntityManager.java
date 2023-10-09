package main.java.br.com.alura.gerenciador.acao;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AcaoComEntityManager {
	
	protected EntityManager em;
	
	public AcaoComEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public abstract String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	
	public Long getParameterId(HttpServletRequest request) {
		return Long.valueOf(request.getParameter("id"));  
	}
	public LocalDate getParameterData(HttpServletRequest request) {
		return LocalDate.parse(request.getParameter("data"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}
