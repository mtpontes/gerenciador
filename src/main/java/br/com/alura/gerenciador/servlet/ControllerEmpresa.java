package br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.alura.gerenciador.dto.empresa.AlteraEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.dto.empresa.ListaEmpresasUsuarioDTO;
import br.com.alura.gerenciador.dto.empresa.NovaEmpresaDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.service.EmpresaService;
import br.com.alura.gerenciador.util.JPAUtil;
import br.com.alura.gerenciador.util.ToCamelCaseUtil;
import br.com.alura.gerenciador.validation.FormValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@WebFilter("/empresa")
public class ControllerEmpresa extends HttpServlet {

	private EntityManager em = JPAUtil.getEntityManager();
	private EmpresaService empresaService = new EmpresaService(em);

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void alteraEmpresa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("alteraEmpresa!");
		
		String nome = request.getParameter("nome");
		String data = request.getParameter("data");
		Long idEmpresa = getParameterId(request);
		AlteraEmpresaDTO dto = new AlteraEmpresaDTO(idEmpresa, new EmpresaBaseDTO(nome, data));

		try {
			empresaService.alteraDadosEmpresa(dto);
			response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
			
		} catch (FormValidationException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
		}
	}
	
	protected void novaEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("novaEmpresa!");
		
		String nome = request.getParameter("nome");
		String data = request.getParameter("data");
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		
		NovaEmpresaDTO empresaDto = new NovaEmpresaDTO(new EmpresaBaseDTO(nome,  data), usuario);

		try {
			empresaService.cadastraEmpresa(empresaDto);
			response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
			
		} catch (FormValidationException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
		}
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void listaEmpresas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listaEmpresas!");
		
		List<EmpresaBaseDTO> listaEmpresas = empresaService.consultaEmpresas();
		request.setAttribute("empresas", listaEmpresas);
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("listaEmpresas.jsp"));
		rd.forward(request, response);
	}
	
	protected void listaEmpresasUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("listaEmpresasUsuario!");

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		
		List<ListaEmpresasUsuarioDTO> listaEmpresas = empresaService.consultaEmpresasUsuario(usuario.getId());
		request.setAttribute("empresas", listaEmpresas);
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("usuarioEmpresas.jsp"));
		rd.forward(request, response);
	}
	
	protected void mostraEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("mostraEmpresa!");
		
		String nome = request.getParameter("nome");
		String data = request.getParameter("data");
		Long id = getParameterId(request);
		AlteraEmpresaDTO dto = new AlteraEmpresaDTO(id, new EmpresaBaseDTO(nome, data));
		
		request.setAttribute("empresa", dto);
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formAlteraEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	protected void novaEmpresaForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formNovaEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu ControllerEmpresa doPut, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "removeEmpresa":
				removeEmpresa(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void removeEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("removeEmpresa!");
		
		Long id = getParameterId(request);
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			empresaService.atualizaEmpresa(id);
			json.addProperty("response", true);
			
		} catch(PersistenceException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			json.addProperty("error", "Ocorreu um erro no servidor");
		} 
		
		try(PrintWriter out = response.getWriter();) {
			out.print(json.toString());
			out.flush();
		}
	}
	
	public Long getParameterId(HttpServletRequest request) {
		return Long.valueOf(request.getParameter("id"));  
	}
	public String enderecoJSP(String nomeDoJSP) {
		return "WEB-INF/view/".concat(nomeDoJSP);
	}
	public String empresaParamAcao(String nomeDoMetodo) {
		return "empresa?acao=".concat(nomeDoMetodo);
	}
}
