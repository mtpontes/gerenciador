package br.com.gerenciador.controller.empresa.commands;

import java.io.IOException;

import com.google.gson.JsonObject;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.ControllerUtil;
import br.com.gerenciador.util.HttpStatusErrorMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RemoveEmpresaCommand implements Command {

    private EmpresaService empresaService;

	public RemoveEmpresaCommand() {
		this.empresaService = new EmpresaService();
	}
	public RemoveEmpresaCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			JsonObject jsonRequestBody = ControllerUtil.converteCorpoRequisicaoParaJsonObject(request);
			
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			Long empresaId = jsonRequestBody.get("empresaId").getAsLong();
			
			empresaService.arquivaEmpresa(empresaId, usuario.getId());
			jsonResponse.addProperty("response", true);
			
		} catch(DatabaseAccessException | IllegalStateException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
			jsonResponse.addProperty("message", e.getMessage());
			
		} catch (IOException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
			jsonResponse.addProperty("message", "ocorreu um erro interno no servidor");
		}
		response.getWriter().print(jsonResponse);
    }
}