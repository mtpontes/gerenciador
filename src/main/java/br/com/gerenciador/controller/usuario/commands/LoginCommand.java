package br.com.gerenciador.controller.usuario.commands;

import java.io.IOException;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.UsuarioService;
import br.com.gerenciador.util.ControllerUtil;
import jakarta.persistence.NoResultException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCommand implements Command {

    private UsuarioService usuarioService;

	public LoginCommand() {
		this.usuarioService = new UsuarioService();
	}
	public LoginCommand(UsuarioService service) {
		this.usuarioService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		try {
			Usuario usuario = usuarioService.buscaUsuarioPorLogin(login);
			
			if (usuario.verificarSenha(senha)) {
				HttpSession sessao = request.getSession();
				sessao.setAttribute("usuarioLogado", usuario);
				sessao.setMaxInactiveInterval(3600);

				response.sendRedirect(ControllerUtil.empresaParamAcao("listaEmpresasUsuario"));
			} 
			response.sendRedirect(ControllerUtil.usuarioParamAcao("loginForm"));

		} catch (NoResultException e) {
			response.sendRedirect(ControllerUtil.empresaParamAcao("loginForm"));
			
		} catch (DatabaseAccessException e) {
			RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
    }
}