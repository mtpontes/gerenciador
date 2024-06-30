package br.com.gerenciador.controller.empresa.commands;

import java.io.IOException;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.modelo.dto.empresa.request.NovaEmpresaDTO;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.ControllerUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NovaEmpresaCommand implements Command {

	private EmpresaService empresaService;

	public NovaEmpresaCommand() {
		this.empresaService = new EmpresaService();
	}
	public NovaEmpresaCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String nome = request.getParameter("nome");
		String data = request.getParameter("data");
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		
		NovaEmpresaDTO empresaDto = new NovaEmpresaDTO(new EmpresaBaseDTO(nome,  data), usuario);
		try {
			empresaService.cadastraEmpresa(empresaDto);
			response.sendRedirect(ControllerUtil.empresaParamAcao("listaEmpresasUsuario"));
			
		} catch (FormValidationException e) {
			response.sendRedirect(ControllerUtil.empresaParamAcao("novaEmpresaForm"));
			
		} catch (IOException | DatabaseAccessException e) {
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
    }
}