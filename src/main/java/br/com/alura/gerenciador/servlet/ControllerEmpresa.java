package main.java.br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;
import main.java.br.com.alura.gerenciador.service.EmpresaService;
import main.java.br.com.alura.gerenciador.util.JPAUtil;
import main.java.br.com.alura.gerenciador.util.ToCamelCaseUtil;
import main.java.br.com.alura.gerenciador.validation.FormValidationException;


//@WebFilter("/empresa")
public class ControllerEmpresa extends HttpServlet {

	private EntityManager em = JPAUtil.getEntityManager();
	private EmpresaService empresaService = new EmpresaService(em);

	
	/* ---------------------------------------- doPost ---------------------------------------------------*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu ControllerEmpresa doPost, o valor do parametro é : " + acao);

		switch (acao) {
			case "alteraEmpresa":
				alteraEmpresa(request, response);
				break;
			case "novaEmpresa":
				novaEmpresa(request, response);
				break;
			default:
				response.sendError(404);
		}
	}
	
	protected void alteraEmpresa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("alteraEmpresa!");
		
		String nomeEmpresa = request.getParameter("nome");
		String dataEmString = request.getParameter("data");
		Long empresaID = getParameterId(request);
		
		try {
			empresaService.alteraDadosEmpresa(empresaID, nomeEmpresa, dataEmString);
			response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
			
		} catch (FormValidationException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
		}
	}
	
	protected void novaEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("novaEmpresa!");
		
		String nomeEmpresa = request.getParameter("nome");
		String dataEmString = request.getParameter("data");
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");

		try {
			empresaService.cadastraEmpresa(nomeEmpresa, dataEmString, usuario);
			System.out.println("Empresa cadastrada!");
			response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
		} catch (FormValidationException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
		}
	}
	
	/* ---------------------------------------- doGet ---------------------------------------------------*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu ControllerEmpresa doGet, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "listaEmpresas":
				listaEmpresas(request, response);
				break;
			case "listaEmpresasUsuario":
				listaEmpresasUsuario(request, response);
				break;
		    case "mostraEmpresa":
		        mostraEmpresa(request, response);
		        break;
			case "novaEmpresaForm":
				novaEmpresaForm(request, response);
			break;
			default:
				response.sendError(404);
		}
	}

	protected void listaEmpresas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listaEmpresas!");
		
		List<Empresa> listaEmpresas = empresaService.consultaEmpresas();
		request.setAttribute("empresas", listaEmpresas);
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("listaEmpresas.jsp"));
		rd.forward(request, response);
	}
	
	protected void listaEmpresasUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("listaEmpresasUsuario!");

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		
		List<Empresa> listaEmpresas = empresaService.consultaEmpresasUsuario(usuario.getId());
		request.setAttribute("empresas", listaEmpresas);
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("usuarioEmpresas.jsp"));
		rd.forward(request, response);
	}
	
	protected void mostraEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("mostraEmpresa!");
		
		request.setAttribute("nome", request.getParameter("nome"));
		request.setAttribute("id", request.getParameter("id"));
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formAlteraEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	protected void novaEmpresaForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formNovaEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------//
	public Long getParameterId(HttpServletRequest request) {
		return Long.valueOf(request.getParameter("id"));  
	}
	public LocalDate getParameterData(HttpServletRequest request) {
		return LocalDate.parse(request.getParameter("data"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	public String enderecoJSP(String nomeDoJSP) {
		return "WEB-INF/view/".concat(nomeDoJSP);
	}
	public String empresaParamAcao(String nomeDoMetodo) {
		return "empresa?acao=".concat(nomeDoMetodo);
	}
	
}
