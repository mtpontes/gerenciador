package main.java.br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.modelo.UsuarioDTO;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepository;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepositoryMySQL;
import main.java.br.com.alura.gerenciador.util.JPAUtil;
import main.java.br.com.alura.gerenciador.util.ToCamelCaseUtil;
import main.java.br.com.alura.gerenciador.util.ValidatorUtil;


//@WebFilter("/usuario")
public class ControllerUsuario extends HttpServlet {

	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioRepository repository = new UsuarioRepositoryMySQL(em);
	
	/* ---------------------------------------- doPost ---------------------------------------------------*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu no ControladorUsuario doPost, o valor do parametro é : " + acao);

		switch (acao) {
			case "novoUsuario":
				novoUsuario(request, response);
				break;
			default:
				response.sendError(404);
		}
	}
	
	protected void novoUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("novoUsuario!");
		
		Validator validator = ValidatorUtil.getValidator();
		
		String nome = request.getParameter("nome");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String confirma = request.getParameter("confirma");
		UsuarioDTO dto = new UsuarioDTO(nome, login, senha, confirma);
		
		//executa as validações
		Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
		
		//se houver violations
		if(!violations.isEmpty()) {
			for(ConstraintViolation<UsuarioDTO> violation : violations) {
				//imprime as mensagens de erro das validações
				System.out.println(violation.getMessage());
			}
			//redireciona o usuário para página de validationError
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
			return;
		}
		
		//se passou nas validações, instancia a entidade Usuario e persiste
		repository.persist(new Usuario(dto));
		System.out.println("Usuario cadastrado!");
		response.sendRedirect(usuarioParamAcao("loginForm"));
	}
	
	/* ---------------------------------------- doGet ---------------------------------------------------*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu no ControladorUsuario doGet, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "login":
				login(request, response);
				break;
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
				response.sendError(404);
		}
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("login!");

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		Usuario usuario = repository.findByLogin(login);
		
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
		
		System.out.println("Sessão de usuário invalidada!");
		response.sendRedirect(usuarioParamAcao("loginForm"));
	}
	/* --------------------------------------------------------------------------------------------------*/
	
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
