package main.java.br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManager;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;
import main.java.br.com.alura.gerenciador.util.JPAUtil;
import jakarta.servlet.annotation.WebFilter;


//@WebFilter("/usuario")
public class ControladorUsuario implements Filter {

	private EntityManager em = JPAUtil.getEntityManager();
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		
		System.out.println("Controlador de Usuarios");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String paramAcao = request.getParameter("acao");
		String nomeDaClasse = "main.java.br.com.alura.gerenciador.acao.usuario." + paramAcao;
		
		String nome;
		try {
			Object instancia = criaInstanciaComEntityManager(nomeDaClasse, null);
			
		    if (instancia instanceof AcaoComEntityManager) {
		    	Object instanciaComEntityManager = criaInstanciaComEntityManager(nomeDaClasse, em);
		    	
		    	AcaoComEntityManager acao = (AcaoComEntityManager) instanciaComEntityManager;
				nome = acao.executa(request, response);
				
			} else if (instancia instanceof AcaoSemEntityManager) {
				AcaoSemEntityManager acao = (AcaoSemEntityManager) instancia;
				nome = acao.executa(request, response);
			} else {
				throw new ServletException("A instancia não implementa nenhuma interface/classe valida");
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


	public Object criaInstanciaComEntityManager(String nomeDaClasse, EntityManager em)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (em == null) {
			Class<?> classe = Class.forName(nomeDaClasse);
			Constructor<?> constructor = classe.getDeclaredConstructor();
			constructor.setAccessible(true);
			Object instancia = constructor.newInstance();
			return instancia;
		}
		
		Class<?> classe = Class.forName(nomeDaClasse);
		Constructor<?> constructor = classe.getDeclaredConstructor(EntityManager.class);
		constructor.setAccessible(true);
		Object instancia = constructor.newInstance(em);
		return instancia;
	}
}
