package br.com.gerenciador.controller.commands.empresa;

import java.io.IOException;
import java.util.List;

import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.ControllerUtil;
import br.com.gerenciador.util.PaginationUtil;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListaEmpresasUsuarioCommand implements Command {

    private EmpresaService empresaService;

	public ListaEmpresasUsuarioCommand() {
		this.empresaService = new EmpresaService();
	}
	public ListaEmpresasUsuarioCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			
			Pagination pg = PaginationUtil.criaPagination(
				request, 
				empresaService.getCountEmpresasUsuarioAtivo(usuario.getId(), true));
			List<ListaEmpresasUsuarioDTO> listaEmpresas = empresaService
				.getEmpresasAtivoUsuarioPaged(pg, usuario.getId(), true);
			
			request.setAttribute("acao", "listaEmpresasAtivoUsuarioAjax");
			request.setAttribute("empresas", listaEmpresas);
			request.setAttribute("currentPage", pg.getPageNumber());
			request.setAttribute("pageSize", pg.getPageSize());
			request.setAttribute("totalPages", pg.getTotalPages());
			
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("usuarioEmpresas.jsp"));
			rd.forward(request, response);
			
		} catch (IOException | PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
    }
}