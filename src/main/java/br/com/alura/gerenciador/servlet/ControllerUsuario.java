package br.com.alura.gerenciador.servlet;

import java.io.IOException;

import com.google.gson.JsonObject;

import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.service.UsuarioService;
import br.com.alura.gerenciador.util.ControllerUtil;
import br.com.alura.gerenciador.util.JPAUtil;
import br.com.alura.gerenciador.validation.FormValidationException;
import jakarta.persistence.EntityManager;
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
		System.out.println("login!");

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		Usuario usuario = usuarioService.buscaUsuarioPorLogin(login);
		if (usuario != null && usuario.verificarSenha(senha)) {

			HttpSession sessao = request.getSession();
			sessao.setAttribute("usuarioLogado", usuario);
			sessao.setMaxInactiveInterval(3600);

			response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
		} else {
			response.sendRedirect(usuarioParamAcao("loginForm"));
		}
	}
	
    protected void verificaLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("verificaLogin!");
        JsonObject jsonRequest = getBodyJsonRequest(request);

        String login = jsonRequest.get("login").getAsString();
        JsonObject respostaJson = new JsonObject();
        try {
            if (usuarioService.verificaSeLoginExiste(login)) {
                respostaJson.addProperty("response", true);
            } else {
                respostaJson.addProperty("response", false);
            }
        } catch (PersistenceException e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            respostaJson.addProperty("error", "Ocorreu um erro ao buscar o usuário");
        }

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(respostaJson.toString());
    }
	
	protected void novoUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("novoUsuario!");
		
		String nome = request.getParameter("nome");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String confirma = request.getParameter("confirma");
		NovoUsuarioDTO dto = new NovoUsuarioDTO(nome, login, senha, confirma);
		
		try {
			usuarioService.cadastraUsuario(dto);
			response.sendRedirect(usuarioParamAcao("loginForm"));
		} catch (FormValidationException e) {
			//se o usuário conseguir enviar um formulário inválido, redireciona o usuário para página de validationError
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
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
		System.out.println("logout!");
		
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