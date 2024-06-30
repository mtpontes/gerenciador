package br.com.gerenciador.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import br.com.gerenciador.modelo.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AutorizacaoFilter implements Filter {

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession sessaoAtual = request.getSession();
		
		String paramAcao = request.getParameter("acao");
		List<String> acoesLiberadas = Arrays.asList(
			"login", 
			"loginForm", 
			"novoUsuarioForm",
			"novoUsuario", 
			"verificaLogin"
		);
		
		boolean usuarioNaoEstaLogado = (sessaoAtual.getAttribute("usuarioLogado") == null);
		boolean ehUmaAcaoProtegida = !acoesLiberadas.contains(paramAcao);
		
		if ( ehUmaAcaoProtegida && usuarioNaoEstaLogado ) {
			response.sendRedirect("usuario?acao=loginForm");
			return;
		}
		Usuario user = (Usuario) sessaoAtual.getAttribute("usuarioLogado");
		sessaoAtual.invalidate();

		HttpSession novaSessao = request.getSession(true);
		novaSessao.setAttribute("usuarioLogado", user);
		novaSessao.setMaxInactiveInterval(3600);
		
		chain.doFilter(request, response);
	}
}
