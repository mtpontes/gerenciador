package br.com.alura.gerenciador.servlet;

import java.io.IOException;

import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.service.UsuarioService;
import br.com.alura.gerenciador.util.JPAUtil;
import br.com.alura.gerenciador.util.ToCamelCaseUtil;
import br.com.alura.gerenciador.validation.FormValidationException;
import jakarta.persistence.EntityManager;
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
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);

		switch (acao) {
			case "novoUsuario":
				novoUsuario(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
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
			
			System.out.println("Usuario cadastrado!");
			response.sendRedirect(usuarioParamAcao("loginForm"));
		} catch (FormValidationException e) {
			//se o usuário conseguir enviar um formulário inválido, redireciona o usuário para página de validationError
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
		}
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		switch (acao) {
			case "loginForm":
				loginForm(request, response);
				break;
			case "login":
				login(request, response);
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
	
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("login!");

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		Usuario usuario = usuarioService.buscaUsuarioPorLogin(login);
		if (usuario != null && usuario.verificarSenha(senha)) {

			HttpSession sessao = request.getSession();
			sessao.setAttribute("usuarioLogado", usuario);
			sessao.setMaxInactiveInterval(3600);

			System.out.println("Usuario autenticado!");
			RequestDispatcher rd = request.getRequestDispatcher(empresaParamAcao("listaEmpresasUsuario")); 
			rd.forward(request, response);
		} else {
			System.out.println("usuario não existe. Redirecionando para loginForm...");
			response.sendRedirect(usuarioParamAcao("loginForm"));
		}
	}
	
	protected void novoUsuarioForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formNovoUsuario.jsp"));
		rd.forward(request, response);
	}
	
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("logout!");
		
		HttpSession sessao = request.getSession();
		sessao.invalidate();
		
		System.out.println("Sessão de usuário invalidada!");
		response.sendRedirect(usuarioParamAcao("loginForm"));
	}

	
	public String enderecoJSP(String nomeDoJSP) {
		return "WEB-INF/view/".concat(nomeDoJSP);
	}
	public String usuarioParamAcao(String nomeDoMetodo) {
		return "usuario?acao=".concat(nomeDoMetodo);
	}
	public String empresaParamAcao(String nomeDoMetodo) {
		return "empresa?acao=".concat(nomeDoMetodo);
	}
}
