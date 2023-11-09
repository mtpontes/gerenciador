package main.java.br.com.alura.gerenciador.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.google.gson.JsonObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepository;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepositoryMySQL;
import main.java.br.com.alura.gerenciador.util.JPAUtil;

//@WebServlet("/ajax")
public class ControllerAJAX extends HttpServlet{
	
	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioRepository usuarioRepository = new UsuarioRepositoryMySQL(em);
	private EmpresaRepository empresaRepository = new EmpresaRepositoryMySQL(em);
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(404);
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
		System.out.println("Bateu ControllerAJAX doPost, o valor do parametro é : " + acao);

        switch (acao) {
    		case "verificaLogin":
    			verificaLogin(request, response);
    			break;
    		case "removeEmpresa":
    			removeEmpresa(request, response);
    			break;
            default:
                response.sendError(404);
        }
    }

    
    //lê o corpo da requisição JSON, transformando o JSON em String
    protected void verificaLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            
            //lê uma linha, quando terminar retorna null
            while ((line = reader.readLine()) != null) {
            	//adiciona a linha capturada 
                stringBuilder.append(line);
            }
            String corpoDaRequisicaoEmString = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(corpoDaRequisicaoEmString);

            if (jsonObject.has("valorDeLogin")) {
                String valorDeLogin = jsonObject.getString("valorDeLogin");

                boolean resposta = usuarioRepository.existsByLogin(valorDeLogin);
                System.out.println("Consultou no DB, o resultado é : " + resposta);

                //Preparar a resposta como JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("resposta", resposta);

                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
            } else {
            	throw new IOException();
            }
        } catch (IOException e) {
        	//resposta de erro em Json
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Ocorreu um erro no servidor");

            PrintWriter out = response.getWriter();
            out.print(jsonObject.toString());
        }
    }
    
    //os parâmetros da requisição vieram pela URL
	protected void removeEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("removeEmpresa!");
		
		Long id = getParameterId(request);
		Empresa empresa = empresaRepository.findEmpresaById(id);
				
		if(empresa != null) {
			empresa = empresa.removeOrRestoreEmpresa();
			empresaRepository.update(empresa);
			
			//resposta em Json
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonResponse = new JSONObject().put("response", true);
			
			PrintWriter out = response.getWriter();
			out.print(jsonResponse.toString());
			out.flush();
		} else {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Ocorreu um erro no servidor");

            PrintWriter out = response.getWriter();
            out.print(jsonObject.toString());
		}
	}
	
	public Long getParameterId(HttpServletRequest request) {
		return Long.valueOf(request.getParameter("id"));  
	}

}
