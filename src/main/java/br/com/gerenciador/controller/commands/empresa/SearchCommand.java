package br.com.gerenciador.controller.commands.empresa;

import java.io.IOException;
import java.util.List;

import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.ControllerUtil;
import br.com.gerenciador.util.PaginationUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchCommand implements Command {

	private EmpresaService empresaService;

	public SearchCommand() {
		this.empresaService = new EmpresaService();
	}
	public SearchCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String nomeEmpresa = request.getParameter("nomeEmpresa");
		
		try {
			Pagination pg = PaginationUtil.criaPagination(request, empresaService.getCountEmpresasSearch(nomeEmpresa));
			List<EmpresaBaseDTO> listaEmpresas = empresaService.getEmpresasByNamePaged(pg,nomeEmpresa);
			
			request.setAttribute("acao", "searchAjax");
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			request.setAttribute("nomeEmpresa", nomeEmpresa);
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("searchEmpresas.jsp"));
			rd.forward(request, response);
			
		} catch (IOException | DatabaseAccessException e) {
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
    }
}