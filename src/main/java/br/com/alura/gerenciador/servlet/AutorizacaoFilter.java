package main.java.br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


//@WebFilter("/entrada")
public class AutorizacaoFilter implements Filter {

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		System.out.println("AutorizacaoFilter");
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession sessao = request.getSession();
		
		String paramAcao = request.getParameter("acao");
		System.out.println("O parâmetro 'acao é : " + paramAcao);
		
		List<String> acoesLiberadas = Arrays.asList("login", "loginForm", "novoUsuarioForm", 
													"novoUsuario", "verificaLogin");
		
		
		String headerRequisicao = request.getHeader("X-Requested-With");
		boolean ehUmRequestAJAX = "XMLHttpRequest".equals(headerRequisicao);
		
		boolean usuarioNaoEstaLogado = (sessao.getAttribute("usuarioLogado") == null);
		boolean ehUmaAcaoProtegida = !acoesLiberadas.contains(paramAcao);
		
		System.out.println("É uma ação protegida?" + ehUmaAcaoProtegida);
		System.out.println("Usuário não está logado?" + usuarioNaoEstaLogado);
		System.out.println("É uma requisição AJAX?" + ehUmRequestAJAX);
		
		if(ehUmaAcaoProtegida && usuarioNaoEstaLogado) {
			System.out.println("AUTORIZAÇÃO FILTER REDIRECIONANDO PARA loginForm");
			response.sendRedirect("usuario?acao=loginForm");
			return;
		}
		
		System.out.println("Passou na validalidação de AutorizacaoFilter!!!");
		chain.doFilter(request, response);
	}
}
