package br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.JsonObject;

import br.com.alura.gerenciador.service.UsuarioService;
import br.com.alura.gerenciador.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@WebServlet("/ajax")
public class ControllerAJAX extends HttpServlet{
	
	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioService us = new UsuarioService(em);
	

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
		System.out.println("Bateu ControllerAJAX doGet, o valor do parametro é : " + acao);

        switch (acao) {
    		case "verificaLogin":
    			verificaLogin(request, response);
    			break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void verificaLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("verificaLogin!");
        String login = request.getParameter("login");

        JsonObject respostaJson = new JsonObject();
        try {
            if (us.verificaSeLoginExiste(login)) {
                respostaJson.addProperty("response", true);
            } else {
                respostaJson.addProperty("response", false);
            }
        } catch (PersistenceException e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            respostaJson.addProperty("error", "Ocorreu um erro ao buscar o usuário");
        }

        PrintWriter out = response.getWriter();
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    out.print(respostaJson.toString());
	    out.flush();
    }
}
