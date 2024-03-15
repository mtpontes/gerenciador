package br.com.alura.gerenciador.servlet;

import java.io.IOException;

import com.google.gson.JsonObject;

import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.alura.gerenciador.exception.DatabaseAccessException;
import br.com.alura.gerenciador.exception.FormValidationException;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.service.UsuarioService;
import br.com.alura.gerenciador.util.ControllerUtil;
import br.com.alura.gerenciador.util.HttpStatusErrorMapperUtil;
import br.com.alura.gerenciador.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@WebFilter("/usuario")
public class ControllerUsuario extends HttpServlet {

	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioService usuarioService = new UsuarioService(em);
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");

		switch (acao) {
			case "login":
				login(request, response);
				break;
			case "verificaLogin":
				verificaLogin(request, response);
				break;
			case "novoUsuario":
				novoUsuario(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		try {
			Usuario usuario = usuarioService.buscaUsuarioPorLogin(login);
			
			if (usuario.verificarSenha(senha)) {
				HttpSession sessao = request.getSession();
				sessao.setAttribute("usuarioLogado", usuario);
				sessao.setMaxInactiveInterval(3600);

				response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
				
			} else {
				response.sendRedirect(usuarioParamAcao("loginForm"));
			}
		} catch (NoResultException e) {
			response.sendRedirect(usuarioParamAcao("loginForm"));
			
		} catch (DatabaseAccessException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}
	
    protected void verificaLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	JsonObject respostaJson = new JsonObject();
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");

    	try {
        	JsonObject jsonRequest = getBodyJsonRequest(request);
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
	
	protected void novoUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String nome = request.getParameter("nome");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String confirma = request.getParameter("confirma");
		NovoUsuarioDTO dto = new NovoUsuarioDTO(nome, login, senha, confirma);
		
		try {
			usuarioService.cadastraUsuario(dto);
			response.sendRedirect(usuarioParamAcao("loginForm"));
		
		} catch (FormValidationException e) {
			response.sendRedirect(usuarioParamAcao("novoUsuarioForm"));
			
		} catch (IOException | PersistenceException e) {
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/500.html"));
			rd.forward(request, response);
		}
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		
		switch (acao) {
			case "loginForm":
				loginForm(request, response);
				break;
			case "novoUsuarioForm":
			    novoUsuarioForm(request, response);
			    break;
			case "logout":
				logout(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void loginForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formLogin.jsp"));
		rd.forward(request, response);
	}
	
	protected void novoUsuarioForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formNovoUsuario.jsp"));
		rd.forward(request, response);
	}
	
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessao = request.getSession();
		sessao.invalidate();
		response.sendRedirect(usuarioParamAcao("loginForm"));
	}

	//metodos auxiliares
	private JsonObject getBodyJsonRequest(HttpServletRequest request) throws IOException {
		return ControllerUtil.converteCorpoRequisicaoParaJsonObject(request);
	}
	private String enderecoJSP(String nomeJSP) {
		return ControllerUtil.enderecoJSP(nomeJSP);
	}
	private String usuarioParamAcao(String nomeDoMetodo) {
		return ControllerUtil.usuarioParamAcao(nomeDoMetodo);
	}
	private String empresaParamAcao(String nomeDoMetodo) {
		return ControllerUtil.empresaParamAcao(nomeDoMetodo);
	}
}