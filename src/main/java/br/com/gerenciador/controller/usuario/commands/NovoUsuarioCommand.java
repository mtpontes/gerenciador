package br.com.gerenciador.controller.usuario.commands;

import java.io.IOException;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.dto.usuario.NovoUsuarioDTO;
import br.com.gerenciador.service.UsuarioService;
import br.com.gerenciador.util.ControllerUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NovoUsuarioCommand implements Command {

    private UsuarioService usuarioService;
	
	public NovoUsuarioCommand() {
		this.usuarioService = new UsuarioService();
	}
	public NovoUsuarioCommand(UsuarioService service) {
		this.usuarioService = service;
	}

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String nome = request.getParameter("nome");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String confirma = request.getParameter("confirma");
		NovoUsuarioDTO dto = new NovoUsuarioDTO(nome, login, senha, confirma);
		
		try {
			usuarioService.cadastraUsuario(dto);
			response.sendRedirect(ControllerUtil.usuarioParamAcao("loginForm"));
		
		} catch (FormValidationException e) {
			response.sendRedirect(ControllerUtil.usuarioParamAcao("novoUsuarioForm"));
			
		} catch (IOException | DatabaseAccessException e) {
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
    }
}