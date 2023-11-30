package br.com.alura.gerenciador.servlet;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

//@WebFilter(urlPatterns="/entrada")
public class MonitoramentoFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("MonitoramentoFilter");
		
		long antes = System.currentTimeMillis();
		String acao = request.getParameter("acao");
		
		chain.doFilter(request, response);
		long depois = System.currentTimeMillis();
		System.out.println("Tempo de execucao da acao " + acao + " -> " + (depois - antes) + "ms");
	}
}
