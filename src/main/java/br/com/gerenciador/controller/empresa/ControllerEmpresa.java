package br.com.gerenciador.controller.empresa;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.controller.NotFoundCommand;
import br.com.gerenciador.controller.empresa.commands.AtualizaEmpresaCommand;
import br.com.gerenciador.controller.empresa.commands.NovaEmpresaFormCommand;
import br.com.gerenciador.controller.empresa.commands.ListaEmpresasAtivoUsuarioAjaxCommand;
import br.com.gerenciador.controller.empresa.commands.ListaEmpresasCommand;
import br.com.gerenciador.controller.empresa.commands.ListaEmpresasUsuarioCommand;
import br.com.gerenciador.controller.empresa.commands.NovaEmpresaCommand;
import br.com.gerenciador.controller.empresa.commands.RemoveEmpresaCommand;
import br.com.gerenciador.controller.empresa.commands.SearchAjaxCommand;
import br.com.gerenciador.controller.empresa.commands.SearchCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerEmpresa extends HttpServlet {

	private final Map<String, Command> postCommands;
	private final Map<String, Command> getCommands;
	private final Map<String, Command> putCommands;

	public ControllerEmpresa(
		Map<String, Command> postCommands, 
		Map<String, Command> getCommands, 
		Map<String, Command> putCommands
		) {
		this.postCommands = postCommands;
		this.getCommands = getCommands;
		this.putCommands = putCommands;
	}
	public ControllerEmpresa() {
		this.postCommands = new HashMap<>();
		this.postCommands.put("novaEmpresa", new NovaEmpresaCommand());

		this.getCommands = new HashMap<>();
		this.getCommands.put("search", new SearchCommand());
		this.getCommands.put("searchAjax", new SearchAjaxCommand());
		this.getCommands.put("listaEmpresas", new ListaEmpresasCommand());
		this.getCommands.put("listaEmpresasUsuario", new ListaEmpresasUsuarioCommand());
		this.getCommands.put("listaEmpresasAtivoUsuarioAjax", new ListaEmpresasAtivoUsuarioAjaxCommand());
		this.getCommands.put("novaEmpresaForm", new NovaEmpresaFormCommand());

		this.putCommands = new HashMap<>();
		this.putCommands.put("removeEmpresa", new RemoveEmpresaCommand());
		this.putCommands.put("atualizaEmpresa", new AtualizaEmpresaCommand());
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getCommand(request, postCommands)
			.executa(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getCommand(request, getCommands)
			.executa(request, response);
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getCommand(request, putCommands)
			.executa(request, response);
	}

	private Command getCommand(HttpServletRequest request, Map<String, Command> commands) {
		String acao = request.getParameter("acao");
		return commands.getOrDefault(acao, new NotFoundCommand());
	}
}