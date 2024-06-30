package br.com.gerenciador.controller.empresa.commands;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.modelo.dto.empresa.response.EmpresaBaseWrapperDTO;
import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.HttpStatusErrorMapperUtil;
import br.com.gerenciador.util.PaginationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchAjaxCommand implements Command {

    private EmpresaService empresaService;

	public SearchAjaxCommand() {
		this.empresaService = new EmpresaService();
	}
	public SearchAjaxCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			String nomeEmpresa = request.getParameter("nomeEmpresa");
			Pagination pg = PaginationUtil.criaPagination(request, empresaService.getCountEmpresasSearch(nomeEmpresa));
			List<EmpresaBaseDTO> listaEmpresas = empresaService
					.getEmpresasByNamePaged(pg,nomeEmpresa);
			
			EmpresaBaseWrapperDTO wrapper = new EmpresaBaseWrapperDTO(listaEmpresas, pg);
			
			String listaEmpresaJson = new Gson().toJson(wrapper);
			response.getWriter().print(listaEmpresaJson);
			
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