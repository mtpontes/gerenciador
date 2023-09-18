package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;

public class Logout implements AcaoComEntityManager {


	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response, EntityManager em)
			throws ServletException, IOException {
		
		HttpSession sessao = request.getSession();
//		sessao.removeAttribute("usuarioLogado");
		sessao.invalidate();
		return "redirect:entrada?acao=LoginForm";
	}

}
