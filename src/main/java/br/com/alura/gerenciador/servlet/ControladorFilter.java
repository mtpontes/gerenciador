package main.java.br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;
import main.java.br.com.alura.gerenciador.util.JPAUtil;


@WebFilter("/entrada")
public class ControladorFilter implements Filter {

	private EntityManager em = JPAUtil.getEntityManager();
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		
		System.out.println("ControladorFilter");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String paramAcao = request.getParameter("acao");
		String nomeDaClasse = "main.java.br.com.alura.gerenciador.acao.empresas." + paramAcao;
		
		String nome;
		try {
		    Class<?> classe = Class.forName(nomeDaClasse);
		    Constructor<?> constructor = classe.getDeclaredConstructor();
		    constructor.setAccessible(true);
		    Object instancia = constructor.newInstance();
		    
		    if (instancia instanceof AcaoComEntityManager) {
			    constructor = classe.getDeclaredConstructor(EntityManager.class);
			    constructor.setAccessible(true);
			    instancia = constructor.newInstance(em);
		    	
				AcaoComEntityManager acao = (AcaoComEntityManager) instancia;
				nome = acao.executa(request, response);
			} else if (instancia instanceof AcaoSemEntityManager) {
				AcaoSemEntityManager acao = (AcaoSemEntityManager) instancia;
				nome = acao.executa(request, response);
			} else {
				throw new ServletException("A instância não implementa nenhuma interface válida");
			}
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
		    throw new ServletException(e);
		}
		String[] tipoEEndereco = nome.split(":");
		if(tipoEEndereco[0].equals("forward")) {
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/view/" + tipoEEndereco[1]);
			rd.forward(request, response);
		} else {
			response.sendRedirect(tipoEEndereco[1]);
		}
	}
}
