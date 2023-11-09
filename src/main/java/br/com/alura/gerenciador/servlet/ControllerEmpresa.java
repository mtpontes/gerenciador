package main.java.br.com.alura.gerenciador.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import jakarta.persistence.EntityManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import main.java.br.com.alura.gerenciador.modelo.AlteraEmpresaDTO;
import main.java.br.com.alura.gerenciador.modelo.NovaEmpresaDTO;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;
import main.java.br.com.alura.gerenciador.util.JPAUtil;
import main.java.br.com.alura.gerenciador.util.ToCamelCaseUtil;
import main.java.br.com.alura.gerenciador.util.ValidatorUtil;


//@WebFilter("/empresa")
public class ControllerEmpresa extends HttpServlet {

	private EntityManager em = JPAUtil.getEntityManager();
	private EmpresaRepository empresaRepository = new EmpresaRepositoryMySQL(em);

	
	/* ---------------------------------------- doPost ---------------------------------------------------*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu ControllerEmpresa doPost, o valor do parametro é : " + acao);

		switch (acao) {
			case "alteraEmpresa":
				alteraEmpresa(request, response);
				break;
			case "novaEmpresa":
				novaEmpresa(request, response);
				break;
			default:
				response.sendError(404);
		}
	}
	
	protected void listaEmpresas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listaEmpresas!");
		
		List<Empresa> listaEmpresas = empresaRepository.findEmpresas();
		request.setAttribute("empresas", listaEmpresas);
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("listaEmpresas.jsp"));
		rd.forward(request, response);
	}
	
	protected void listaEmpresasUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("listaEmpresasUsuario!");

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		
		List<Empresa> listaEmpresas = empresaRepository.findEmpresasOfUsuarioById(usuario.getId());
		request.setAttribute("empresas", listaEmpresas);
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("usuarioEmpresas.jsp"));
		rd.forward(request, response);
	}
	
	protected void mostraEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("mostraEmpresa!");
		
		request.setAttribute("nome", request.getParameter("nome"));
		request.setAttribute("id", request.getParameter("id"));
		
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formAlteraEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	protected void alteraEmpresa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("alteraEmpresa!");
		
		Validator validator = ValidatorUtil.getValidator();
		
		String nomeEmpresa = request.getParameter("nome");
		String dataEmString = request.getParameter("data");
		Long empresaID = getParameterId(request);
		AlteraEmpresaDTO dto = new AlteraEmpresaDTO(nomeEmpresa, dataEmString);
		
		Set<ConstraintViolation<AlteraEmpresaDTO>> violations = validator.validate(dto);
		
		if(!violations.isEmpty()) {
			violations.forEach(msg -> System.out.println(msg.getMessage()));
			RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("/error/validationError.html"));
			rd.forward(request, response);
			return;
		}
		LocalDate dataConvertida = getParameterData(request);
		
		Empresa empresa = empresaRepository.findEmpresaById(empresaID).alteraDados(nomeEmpresa, dataConvertida);
		empresaRepository.update(empresa);
		
		response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
	}
	
	protected void novaEmpresa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("novaEmpresa!");
		
		Validator validator = ValidatorUtil.getValidator();
		
		String dataEmString = request.getParameter("data");
		String nomeEmpresa = request.getParameter("nome");
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		NovaEmpresaDTO dto = new NovaEmpresaDTO(nomeEmpresa, dataEmString, usuario.getId());
		
		Set<ConstraintViolation<NovaEmpresaDTO>> violations = validator.validate(dto);
		
		if(!violations.isEmpty()) {
			violations.forEach(msg -> System.out.println(msg.getMessage()));
			response.sendRedirect(enderecoJSP("/error/validationError.html"));
			return;
		}
		LocalDate dataConvertida = getParameterData(request);
		
		empresaRepository.persist(new Empresa(dto.nome(), dataConvertida, usuario));
		System.out.println("Empresa cadastrada!");
		
		response.sendRedirect(empresaParamAcao("listaEmpresasUsuario"));
	}
	
	/* ---------------------------------------- doGet ---------------------------------------------------*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramAcao = request.getParameter("acao");
		String acao = ToCamelCaseUtil.toCamelCase(paramAcao);
		
		System.out.println("Bateu ControllerEmpresa doGet, o valor do parametro é : " + acao);
		
		switch (acao) {
			case "listaEmpresas":
				listaEmpresas(request, response);
				break;
			case "listaEmpresasUsuario":
				listaEmpresasUsuario(request, response);
				break;
		    case "mostraEmpresa":
		        mostraEmpresa(request, response);
		        break;
			case "novaEmpresaForm":
				novaEmpresaForm(request, response);
			break;
			default:
				response.sendError(404);
		}
	}

	protected void novaEmpresaForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(enderecoJSP("formNovaEmpresa.jsp"));
		rd.forward(request, response);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------//
	public Long getParameterId(HttpServletRequest request) {
		return Long.valueOf(request.getParameter("id"));  
	}
	public LocalDate getParameterData(HttpServletRequest request) {
		return LocalDate.parse(request.getParameter("data"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	public String enderecoJSP(String nomeDoJSP) {
		return "WEB-INF/view/".concat(nomeDoJSP);
	}
	public String empresaParamAcao(String nomeDoMetodo) {
		return "empresa?acao=".concat(nomeDoMetodo);
	}
	
}
