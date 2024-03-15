package br.com.gerenciador.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gerenciador.controller.ControllerUsuario;
import br.com.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.UsuarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ControllerUsuarioTest {

    @InjectMocks
    private ControllerUsuario controller;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;
    @Mock
    private HttpSession session;
    @Mock
    private EntityManager em;

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private Usuario usuario;
    @Mock
    private PrintWriter out;

    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Integer> statusCodeCaptor;

    private static final String PARAM_ACAO = "acao";
    private static final String PARAMETRO_INVALIDO = "umParametroInvalido";

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_LOGIN_VALUE = "umLogin";


    @Test
    @DisplayName("Erro 404 quando o parâmetro 'acao' é inválido")
    void doPostTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doPost(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
    @DisplayName("Deve redirecionar para página de listar empresas do usuário")
    void loginTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("login");
    	
    	BDDMockito.given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	BDDMockito.given(usuarioService.buscaUsuarioPorLogin(PARAM_LOGIN_VALUE)).willReturn(usuario);
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(usuario.verificarSenha(any())).willReturn(true);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//asert
    	BDDMockito.then(request).should().getSession();
    	BDDMockito.then(session).should().setAttribute(anyString(), any());
    	BDDMockito.then(session).should().setMaxInactiveInterval(anyInt());
    }
    @Test
    @DisplayName("Deve redirecionar para a página atual")
    void loginTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("login");
    	
    	BDDMockito.given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	BDDMockito.given(usuarioService.buscaUsuarioPorLogin(PARAM_LOGIN_VALUE)).willReturn(usuario);
    	BDDMockito.given(usuario.verificarSenha(any())).willReturn(false);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//asert
    	
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	System.out.println(stringCaptor.getValue());
    	String endereco = stringCaptor.getValue();
    	endereco = endereco.substring(endereco.indexOf("loginForm"));
    	Assertions.assertEquals(endereco, "loginForm");
    }
    @Test
    @DisplayName("Deve redirecionar para a página atual ao não encontrar o usuário no banco de dados")
    void loginTest03() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("login");
    	
    	BDDMockito.given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	BDDMockito.willThrow(new NoResultException()).given(usuarioService).buscaUsuarioPorLogin(any());
    	
    	//act
    	controller.doPost(request, response);
    	
    	//asert
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	System.out.println(stringCaptor.getValue());
    	String endereco = stringCaptor.getValue();
    	endereco = endereco.substring(endereco.indexOf("loginForm"));
    	
    	Assertions.assertEquals(endereco, "loginForm");
    }
    @Test
    @DisplayName("Deve encaminhar a requisição para a página de erro 500")
    void loginTest04() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("login");
    	
    	BDDMockito.given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(usuarioService).buscaUsuarioPorLogin(any());
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//asert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	String endereco = stringCaptor.getValue();
    	endereco = endereco.substring(endereco.indexOf("500.html"));
    	
    	Assertions.assertEquals(endereco, "500.html");
    }
    
    @Test
    @DisplayName("Deve retornar json")
    void verificaLoginTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("verificaLogin");
    	
    	String requestBody = "{\"login\":\"umLogin\"}";
        BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(requestBody)));
        BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(out).print(stringCaptor.capture());
    	JsonObject jsonResponse = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	Assertions.assertTrue(jsonResponse.has("response"));
    }
    @Test
    @DisplayName("Deve retornar json com mensagem de erro")
    void verificaLoginTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("verificaLogin");
    	
    	String requestBody = "{\"login\":\"umLogin\"}";
    	BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(requestBody)));
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(usuarioService).verificaSeLoginExiste(anyString());
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(response).setStatus(statusCodeCaptor.capture());
    	Integer statusCode = statusCodeCaptor.getValue();
    	
    	BDDMockito.verify(out).print(stringCaptor.capture());
    	JsonObject jsonResponse = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	Assertions.assertTrue(jsonResponse.has("error"));
    	Assertions.assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    
    @Test
    @DisplayName("Deve redirecionar para página de login ao cadastrar usuário com sucesso")
    void novoUsuarioTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novoUsuario");
    	
    	// act/assert
    	Assertions.assertDoesNotThrow(() -> controller.doPost(request, response));
    	
    	BDDMockito.verify(usuarioService).cadastraUsuario(any(NovoUsuarioDTO.class));

    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	String endereco = stringCaptor.getValue();
    	String acao = extractParamFromUrl(endereco, PARAM_ACAO);
    	Assertions.assertEquals("loginForm", acao);
    }
    @Test
    @DisplayName("Deve redirecionar para página atual ao falhar em cadastrar usuário")
    void novoUsuarioTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novoUsuario");
    	BDDMockito.willThrow(new FormValidationException("")).given(usuarioService).cadastraUsuario(any());;

    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("novoUsuarioForm")); 
    	Assertions.assertEquals("novoUsuarioForm", acao);
    }
    @Test
    @DisplayName("Deve encaminhar a requisicao para página de erro 500")
    void novoUsuarioTest03() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novoUsuario");
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(usuarioService).cadastraUsuario(any());;
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("500.html")); 
    	Assertions.assertEquals("500.html", acao);
    }
    
    @Test
    @DisplayName("Deve lançar erro 404 quando o parâmetro 'acao' é inválido")
    void doGetTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
    @DisplayName("Deve encaminhar request para a página de login")
    void loginFormTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("loginForm");
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve encaminhar request para a página de criação de um novo usuário")
    void novoUsuarioFormTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novoUsuarioForm");
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve redirecionar para a de login ao deslogar com sucesso")
    void logoutTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("logout");
    	BDDMockito.given(request.getSession()).willReturn(session);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//asert
    	BDDMockito.verify(session).invalidate();
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String parametroURL = extractParamFromUrl(endereco, PARAM_ACAO);
    	Assertions.assertEquals("loginForm", parametroURL);
    }

    private String extractParamFromUrl(String url, String paramName) {
        int startIndex = url.indexOf(paramName + "=");
        if (startIndex != -1) {
            startIndex += paramName.length() + 1; // Adiciona o comprimento do nome do parâmetro e do '='
            int endIndex = url.indexOf('&', startIndex);
            endIndex = (endIndex != -1) ? endIndex : url.length();
            return url.substring(startIndex, endIndex);
        }
        return null;
    }
}
