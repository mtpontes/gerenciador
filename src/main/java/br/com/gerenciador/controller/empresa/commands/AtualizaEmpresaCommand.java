package br.com.gerenciador.controller.empresa.commands;

import java.io.IOException;

import com.google.gson.JsonObject;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.modelo.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.ControllerUtil;
import br.com.gerenciador.util.HttpStatusErrorMapperUtil;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AtualizaEmpresaCommand implements Command {

    private EmpresaService empresaService;

	public AtualizaEmpresaCommand() {
		this.empresaService = new EmpresaService();
	}
	public AtualizaEmpresaCommand(EmpresaService service) {
		this.empresaService = service;
	}
	

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			JsonObject jsonRequestBody = ControllerUtil.converteCorpoRequisicaoParaJsonObject(request);
			
			String nome = jsonRequestBody.get("nome").getAsString();
			String data = jsonRequestBody.get("data").getAsString();
			Long id = jsonRequestBody.get("id").getAsLong();
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
			
			AlteraEmpresaDTO dto = new AlteraEmpresaDTO(id, new EmpresaBaseDTO(nome, data));
			empresaService.alteraDadosEmpresa(dto, usuario);
			jsonResponse.addProperty("message", "empresa atualizada com sucesso");
			
		} catch(DatabaseAccessException| NoResultException | IllegalStateException | FormValidationException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
			jsonResponse.addProperty("message", e.getMessage());
			
		} catch (IOException e) {
			response.setStatus(HttpStatusErrorMapperUtil.getStatusCodeByException(e));
			jsonResponse.addProperty("message", "ocorreu um erro interno no servidor");
		}
		response.getWriter().print(jsonResponse.toString());
    }
}