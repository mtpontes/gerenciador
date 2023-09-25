package main.java.br.com.alura.gerenciador.acao;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AcaoComEntityManager {
	
	protected EntityManager em;
	
	public AcaoComEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public abstract String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
