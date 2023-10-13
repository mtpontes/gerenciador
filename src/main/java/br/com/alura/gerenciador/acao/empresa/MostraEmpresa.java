package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;

@NoArgsConstructor
public class MostraEmpresa implements AcaoSemEntityManager{
	
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("mostrando dados da empresa");
		
		request.setAttribute("nome", request.getParameter("nome"));
		request.setAttribute("id", request.getParameter("id"));
		
		return "forward:formAlteraEmpresa.jsp";
	}
}
