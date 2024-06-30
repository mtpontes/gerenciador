package br.com.gerenciador.controller.commands.empresa;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.gerenciador.modelo.dto.empresa.response.ListaEmpresasUsuarioWrapperDTO;
import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.HttpStatusErrorMapperUtil;
import br.com.gerenciador.util.PaginationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListaEmpresasAtivoUsuarioAjaxCommand implements Command {

    private EmpresaService empresaService;

	public ListaEmpresasAtivoUsuarioAjaxCommand() {
		this.empresaService = new EmpresaService();
	}
	public ListaEmpresasAtivoUsuarioAjaxCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			boolean ativo = request.getParameter("ativo") == null ? true : Boolean.valueOf(request.getParameter("ativo"));
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			Pagination pg = PaginationUtil.criaPagination(request, (empresaService.getCountEmpresasUsuarioAtivo(usuario.getId(), ativo)));
			
			List<ListaEmpresasUsuarioDTO> listaEmpresas = empresaService
				.getEmpresasAtivoUsuarioPaged(pg, usuario.getId(), ativo);
			ListaEmpresasUsuarioWrapperDTO wrapper = new ListaEmpresasUsuarioWrapperDTO(listaEmpresas, pg);
			
			String empresaWrapper = new Gson().toJson(wrapper);
			response.getWriter().print(empresaWrapper);
			
		} catch (DatabaseAccessException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
			jsonResponse.addProperty("message", e.getMessage());
			response.getWriter().print(jsonResponse.toString());
			
		} catch (IOException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
			jsonResponse.addProperty("message", "ocorreu um erro interno no servidor");
			response.getWriter().print(jsonResponse.toString());
		}
    }
}