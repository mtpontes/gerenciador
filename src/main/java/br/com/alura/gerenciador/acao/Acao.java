package main.java.br.com.alura.gerenciador.acao;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Acao {
	
	public abstract String executa(HttpServletRequest request, HttpServletResponse response, 
			EntityManager em) throws ServletException, IOException;
	
}