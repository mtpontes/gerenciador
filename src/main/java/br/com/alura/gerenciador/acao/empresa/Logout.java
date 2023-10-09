package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;

public class Logout implements AcaoSemEntityManager {

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession sessao = request.getSession();
//		sessao.removeAttribute("usuarioLogado");
		sessao.invalidate();
		return "redirect:entrada?acao=LoginForm";
	}

}
