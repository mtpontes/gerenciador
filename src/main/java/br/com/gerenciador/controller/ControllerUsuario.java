package br.com.gerenciador.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.controller.commands.usuario.LoginCommand;
import br.com.gerenciador.controller.commands.usuario.LoginFormCommand;
import br.com.gerenciador.controller.commands.usuario.LogoutCommand;
import br.com.gerenciador.controller.commands.usuario.NovoUsuarioCommand;
import br.com.gerenciador.controller.commands.usuario.NovoUsuarioFormCommand;
import br.com.gerenciador.controller.commands.usuario.VerificaLoginCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerUsuario extends HttpServlet {

	private final Map<String, Command> postCommands;
	private final Map<String, Command> getCommands;

	public ControllerUsuario(
		Map<String, Command> postCommands, 
		Map<String, Command> getCommands
	) {
		this.postCommands = postCommands;
		this.getCommands = getCommands;
	}

	public ControllerUsuario() {
		this.postCommands = new HashMap<>();
		this.postCommands.put("login", new LoginCommand());
		this.postCommands.put("verificaLogin", new VerificaLoginCommand());
		this.postCommands.put("novoUsuario", new NovoUsuarioCommand());

		this.getCommands = new HashMap<>();
		this.getCommands.put("loginForm", new LoginFormCommand());
		this.getCommands.put("novoUsuarioForm", new NovoUsuarioFormCommand());
		this.getCommands.put("logout", new LogoutCommand());
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getCommand(request, this.postCommands)
			.executa(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getCommand(request, this.getCommands)
			.executa(request, response);
	}

	private Command getCommand(
		HttpServletRequest request, 
		Map<String, Command> commands
	) {
		String acao = request.getParameter("acao");
		return commands.getOrDefault(acao, new NotFoundCommand());
	}
}