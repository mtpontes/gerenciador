package br.com.gerenciador.unit.controller.usuario.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.controller.usuario.commands.LoginCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.UsuarioService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class LoginCommandTest {

    @InjectMocks
    private LoginCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;
    @Mock
    private HttpSession session;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private Usuario usuario;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_LOGIN_VALUE = "umLogin";
    private static final String PARAM_SENHA = "senha";
    private static final String PARAM_SENHA_VALUE = "umaSenha";


    @Test
    @DisplayName("Deve redirecionar para página de listar empresas do usuário")
    void loginTest01() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	given(request.getParameter(PARAM_SENHA)).willReturn(PARAM_SENHA_VALUE);
    	given(usuarioService.buscaUsuarioPorLogin(PARAM_LOGIN_VALUE)).willReturn(usuario);
    	given(request.getSession()).willReturn(session);
    	given(usuario.verificarSenha(any())).willReturn(true);
    	
    	//act
    	command.executa(request, response);
    	
    	//asert
    	then(request).should().getSession();
    	then(session).should().setAttribute(anyString(), any());
    	then(session).should().setMaxInactiveInterval(anyInt());
    }
    @Test
    @DisplayName("Deve redirecionar para a página atual")
    void loginTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	given(usuarioService.buscaUsuarioPorLogin(PARAM_LOGIN_VALUE)).willReturn(usuario);
    	given(usuario.verificarSenha(any())).willReturn(false);
    	
    	//act
    	command.executa(request, response);
    	
    	//asert
    	
    	verify(response).sendRedirect(stringCaptor.capture());
    	System.out.println(stringCaptor.getValue());
    	String endereco = stringCaptor.getValue();
    	endereco = endereco.substring(endereco.indexOf("loginForm"));
    	assertEquals(endereco, "loginForm");
    }
    @Test
    @DisplayName("Deve redirecionar para a página atual ao não encontrar o usuário no banco de dados")
    void loginTest03() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	willThrow(new NoResultException()).given(usuarioService).buscaUsuarioPorLogin(any());
    	
    	//act
    	command.executa(request, response);
    	
    	//asert
    	verify(response).sendRedirect(stringCaptor.capture());
    	System.out.println(stringCaptor.getValue());
    	String endereco = stringCaptor.getValue();
    	endereco = endereco.substring(endereco.indexOf("loginForm"));
    	
    	assertEquals(endereco, "loginForm");
    }
    @Test
    @DisplayName("Deve encaminhar a requisição para a página de erro 500")
    void loginTest04() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
    	willThrow(new DatabaseAccessException("")).given(usuarioService).buscaUsuarioPorLogin(any());
    	given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	//act
    	command.executa(request, response);
    	
    	//asert
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	String endereco = stringCaptor.getValue();
    	endereco = endereco.substring(endereco.indexOf("500.html"));
    	
    	assertEquals(endereco, "500.html");
    }
}