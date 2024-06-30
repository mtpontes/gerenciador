package br.com.gerenciador.controller.commands.usuario;

import java.io.IOException;

import com.google.gson.JsonObject;

import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.service.UsuarioService;
import br.com.gerenciador.util.ControllerUtil;
import br.com.gerenciador.util.HttpStatusErrorMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class VerificaLoginCommand implements Command {

	private UsuarioService usuarioService;

	public VerificaLoginCommand() {
		this.usuarioService = new UsuarioService();
	}
	public VerificaLoginCommand(UsuarioService service) {
		this.usuarioService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	JsonObject respostaJson = new JsonObject();
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");

    	try {
        	JsonObject jsonRequest = ControllerUtil.converteCorpoRequisicaoParaJsonObject(request);
        	String login = jsonRequest.get("login").getAsString();

        	if (usuarioService.verificaSeLoginExiste(login)) {
                respostaJson.addProperty("response", true);
            } else {
                respostaJson.addProperty("response", false);
            }
        } catch (DatabaseAccessException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
            respostaJson.addProperty("error", e.getMessage());
        }
	    response.getWriter().print(respostaJson.toString());
    }
}