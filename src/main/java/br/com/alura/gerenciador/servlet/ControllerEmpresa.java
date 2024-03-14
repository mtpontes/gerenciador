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
import jakarta.persistence.NoResultException;
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
			
		} catch (IOException | PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		System.out.println("ControllerEmpresa doGet, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "search":
				search(request, response);
				break;
			case "searchAjax":
				searchAjax(request, response);
				break;
			case "listaEmpresas":
				listaEmpresas(request, response);
				break;
			case "listaEmpresasUsuario":
				listaEmpresasUsuario(request, response);
				break;
			case "listaEmpresasAtivoUsuarioAjax":
				listaEmpresasAtivoUsuarioAjax(request, response);
				break;
			case "novaEmpresaForm":
				novaEmpresaForm(request, response);
			break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nomeEmpresa = request.getParameter("nomeEmpresa");
		
		try {
			Pagination pg = criaPagination(request, empresaService.getCountEmpresasSearch(nomeEmpresa));
			List<EmpresaBaseDTO> listaEmpresas = empresaService
					.getEmpresasByNamePaged(pg,nomeEmpresa);
			
			request.setAttribute("acao", "searchAjax");
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			request.setAttribute("nomeEmpresa", nomeEmpresa);
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("searchEmpresas.jsp"));
			rd.forward(request, response);
			
		} catch (IOException | PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}
	protected void searchAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			String nomeEmpresa = request.getParameter("nomeEmpresa");
			Pagination pg = criaPagination(request, empresaService.getCountEmpresasSearch(nomeEmpresa));
			List<EmpresaBaseDTO> listaEmpresas = empresaService
					.getEmpresasByNamePaged(pg,nomeEmpresa);
			
			EmpresaBaseWrapperDTO wrapper = new EmpresaBaseWrapperDTO(listaEmpresas, pg);
			System.out.println(new Gson().toJson(wrapper));
			
			String listaEmpresaJson = new Gson().toJson(wrapper);
			response.getWriter().print(listaEmpresaJson);
			
		} catch (IOException | PersistenceException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jsonResponse.addProperty("message", "ocorreu um erro no servidor");
			response.getWriter().print(jsonResponse.toString());
		}
	}
	
	protected void listaEmpresas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Pagination pg = this.criaPagination(request, empresaService.getCountEmpresas());
			List<EmpresaBaseDTO> listaEmpresas = empresaService.getEmpresasPaged(pg);
			
			request.setAttribute("acao", "listaEmpresas");
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("listaEmpresas.jsp"));
			
			rd.forward(request, response);
			
		} catch (IOException | PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}
	
	protected void listaEmpresasUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			
			Pagination pg = criaPagination(
					request, 
					(empresaService.getCountEmpresasUsuarioAtivo(
							usuario.getId(),
							true))
					);
			List<ListaEmpresasUsuarioDTO> listaEmpresas = empresaService
					.getEmpresasAtivoUsuarioPaged(pg, usuario.getId(), true);
			
			request.setAttribute("acao", "listaEmpresasAtivoUsuarioAjax");
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("usuarioEmpresas.jsp"));
			rd.forward(request, response);
			
		} catch (IOException | PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}
	
	protected void listaEmpresasAtivoUsuarioAjax(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			boolean ativo = request.getParameter("ativo") == null ? true : Boolean.valueOf(request.getParameter("ativo"));
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			Pagination pg = criaPagination(request, (empresaService.getCountEmpresasUsuarioAtivo(usuario.getId(), ativo)));
			
			List<ListaEmpresasUsuarioDTO> listaEmpresas = empresaService
					.getEmpresasAtivoUsuarioPaged(pg, usuario.getId(), ativo);
			ListaEmpresasUsuarioWrapperDTO wrapper = new ListaEmpresasUsuarioWrapperDTO(listaEmpresas, pg);
			
			String empresaWrapper = new Gson().toJson(wrapper);
			response.getWriter().print(empresaWrapper);
			
		} catch (IOException | PersistenceException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jsonResponse.addProperty("message", "ocorreu um erro no servidor");
			response.getWriter().print(jsonResponse.toString());
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
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			JsonObject jsonRequestBody = getBodyJsonRequest(request);
			
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			Long empresaId = jsonRequestBody.get("empresaId").getAsLong();
			
			empresaService.arquivaEmpresa(empresaId, usuario.getId());
			jsonResponse.addProperty("response", true);
			
		} catch(FormValidationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			jsonResponse.addProperty("message", e.getMessage());

		} catch(IOException | PersistenceException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jsonResponse.addProperty("message", "ocorreu um erro no servidor");
		}
		
		response.getWriter().print(jsonResponse);
	}
	
	protected void atualizaEmpresa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			
		} catch(IllegalStateException e) {
        	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        	jsonResponse.addProperty("message", e.getMessage());
        	
		} catch(NoResultException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			jsonResponse.addProperty("message", e.getMessage());
		
		} catch(IOException | PersistenceException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jsonResponse.addProperty("message", "ocorreu um erro no servidor");;
		}

		response.getWriter().print(jsonResponse.toString());
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