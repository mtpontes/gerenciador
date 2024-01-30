package br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.dto.empresa.request.AlteraEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.request.NovaEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.alura.gerenciador.dto.empresa.response.paginated.EmpresaBaseWrapperDTO;
import br.com.alura.gerenciador.dto.empresa.response.paginated.ListaEmpresasUsuarioWrapperDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.pagination.Pagination;
import br.com.alura.gerenciador.pagination.PaginationBuilder;
import br.com.alura.gerenciador.service.EmpresaService;
import br.com.alura.gerenciador.util.ControllerUtil;
import br.com.alura.gerenciador.util.JPAUtil;
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
		String acao = request.getParameter("acao");
		System.out.println("Bateu ControllerEmpresa doPost, o valor do parametro é : " + acao);

		switch (acao) {
			case "novaEmpresa":
				novaEmpresa(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
		} catch (PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		System.out.println("Bateu ControllerEmpresa doGet, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "search":
				search(request, response);
				break;
			case "listaEmpresas":
				listaEmpresas(request, response);
				break;
			case "listaEmpresasUsuario":
				listaEmpresasUsuario(request, response);
				break;
			case "listaEmpresasDesativadasUsuario":
				listaEmpresasUsuario(request, response);
				break;
			case "novaEmpresaForm":
				novaEmpresaForm(request, response);
			break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("search!");
		String requestType = (request.getContentType() == null) ? "" : request.getContentType();
		String nomeEmpresa = request.getParameter("nomeEmpresa");
		
		Pagination pg = criaPagination(request, empresaService.getCountEmpresasSearch(nomeEmpresa));
		List<EmpresaBaseDTO> listaEmpresas = empresaService.getEmpresasByNamePaged(
				nomeEmpresa, 
				pg.getStartIndex(), 
				pg.getPageSize()
				);
		
		EmpresaBaseWrapperDTO wrapper = new EmpresaBaseWrapperDTO(listaEmpresas, pg);
		if (requestType.equals("application/json")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String listaEmpresaJson = new Gson().toJson(wrapper);
			response.getWriter().print(listaEmpresaJson);
			
		} else {
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			request.setAttribute("nomeEmpresa", nomeEmpresa);
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("searchEmpresas.jsp"));
			rd.forward(request, response);
		}
	}
	
	protected void listaEmpresas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listaEmpresas!");
		String requestType = (request.getContentType() == null) ? "" : request.getContentType();

		Pagination pg = criaPagination(request, empresaService.getCountEmpresas());
		List<EmpresaBaseDTO> listaEmpresas = empresaService.getEmpresasPaged(pg.getStartIndex(), pg.getPageSize());
		if(requestType.equals("application/json")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String listaEmpresaJson = new Gson().toJson(listaEmpresas);
			response.getWriter().print(listaEmpresaJson);
			
		} else {
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("listaEmpresas.jsp"));
			rd.forward(request, response);
		}
	}
	
	protected void listaEmpresasUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("listaEmpresasUsuario!");
		String requestType = (request.getContentType() == null) ? "" : request.getContentType();
		
		String requestAcao = request.getParameter("acao");
		String requestAtivoTrue = "listaEmpresasUsuario";
		String requestAtivoFalse = "listaEmpresasDesativadasUsuario";
		

		//define qual será o paramAcao da página após o sucesso dessa requisição
		String responseParamAcao;
		//define se esta será uma consulta empresa.ativo = 1 (true) ou empresa.tivo = 0 (false)
		Boolean switcher;
		if(requestAcao.equals(requestAtivoTrue)) {
			responseParamAcao = requestAtivoTrue;
			switcher = true;
		} else {
			responseParamAcao = requestAtivoFalse;
			switcher = false;
		}
		
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		Pagination pg = criaPagination(request, (empresaService.getCountEmpresasUsuarioAtivo(usuario.getId(), switcher)));
		
		List<ListaEmpresasUsuarioDTO> listaEmpresas = empresaService
				.getEmpresasUsuarioPaged(usuario.getId(),
						pg.getStartIndex(),
						pg.getPageSize(),
						switcher);
		ListaEmpresasUsuarioWrapperDTO wrapper = new ListaEmpresasUsuarioWrapperDTO(listaEmpresas, pg, responseParamAcao);
		listaEmpresas.forEach(empresa -> System.out.println(empresa.getNome()));
		if(requestType.equals("application/json")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String empresaWrapper = new Gson().toJson(wrapper);
			response.getWriter().print(empresaWrapper);
			
		} else {
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("usuarioEmpresas.jsp"));
			rd.forward(request, response);
		}
	}
	
	
	protected void novaEmpresaForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formNovaEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		System.out.println("Bateu ControllerEmpresa doPut, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "removeEmpresa":
				removeEmpresa(request, response);
				break;
			case "atualizaEmpresa":
				atualizaEmpresa(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void removeEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("removeEmpresa!");
		
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			JsonObject jsonRequestBody = getBodyJsonRequest(request);
			
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			Long empresaId = (jsonRequestBody.get("empresaId").getAsLong());
			empresaService.arquivaEmpresa(empresaId, usuario.getId());
			jsonResponse.addProperty("response", true);
			
		} catch(PersistenceException | FormValidationException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jsonResponse.addProperty("error", "Ocorreu um erro no servidor");
		} 
		response.getWriter().print(jsonResponse.toString());
	}
	
	protected void atualizaEmpresa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("atualizaEmpresa!");
		
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			JsonObject jsonRequestBody = getBodyJsonRequest(request);
			
			String nome = jsonRequestBody.get("nome").getAsString();
			String data = jsonRequestBody.get("data").getAsString();
			Long id = jsonRequestBody.get("id").getAsLong();
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			
			AlteraEmpresaDTO dto = new AlteraEmpresaDTO(id, new EmpresaBaseDTO(nome, data));
			
			empresaService.alteraDadosEmpresa(dto, usuario);
			response.getWriter().print(jsonResponse.toString());
			
		} catch(IOException | FormValidationException e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	jsonResponse.addProperty("error", "Ocorreu um erro ao processar a sua requisição.");
        	response.getWriter().print(jsonResponse.toString());
		}
	}
	
	
	//metodos auxiliares
	private Pagination criaPagination(HttpServletRequest request, Long totalPages) {
		String paramPageNumber = request.getParameter("page");
		String paramPageSize = request.getParameter("size");
		Pagination pg = new PaginationBuilder()
				.setPageNumber(paramPageNumber)
				.setPageSize(paramPageSize)
				.setTotalPages(totalPages)
				.build();
		return pg;
	}
	protected JsonObject getBodyJsonRequest(HttpServletRequest request) throws IOException {
		return ControllerUtil.converteCorpoRequisicaoParaJsonObject(request);
	}
	private String enderecoJSP(String nomeJSP) {
		return ControllerUtil.enderecoJSP(nomeJSP);
	}
	private String empresaParamAcao(String nomeDoMetodo) {
		return ControllerUtil.empresaParamAcao(nomeDoMetodo);
	}
}