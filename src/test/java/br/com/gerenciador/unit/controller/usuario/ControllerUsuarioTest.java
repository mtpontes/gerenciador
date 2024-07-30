package br.com.gerenciador.unit.controller.usuario;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.controller.ControllerUsuario;
import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.controller.commands.usuario.LoginCommand;
import br.com.gerenciador.controller.commands.usuario.LoginFormCommand;
import br.com.gerenciador.controller.commands.usuario.LogoutCommand;
import br.com.gerenciador.controller.commands.usuario.NovoUsuarioCommand;
import br.com.gerenciador.controller.commands.usuario.NovoUsuarioFormCommand;
import br.com.gerenciador.controller.commands.usuario.VerificaLoginCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ControllerUsuarioTest {

	private static final String PARAM_ACAO = "acao";
	private static final String PARAMETRO_INVALIDO = "umParametroInvalido";

    @InjectMocks
    private ControllerUsuario controller;
	
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

	@Mock
	private LoginCommand loginCommand;
	@Mock
	private VerificaLoginCommand verificaLoginCommand;
	@Mock
	private NovoUsuarioCommand novoUsuarioCommand;
	@Mock
	private LoginFormCommand loginFormCommand;
	@Mock
	private NovoUsuarioFormCommand novoUsuarioFormCommand;
	@Mock
	private LogoutCommand logoutCommand;

	private Map<String, Command> postCommands; 
	private Map<String, Command> getCommands; 

	@BeforeEach
    void setUp() {
		this.postCommands = new HashMap<>();
		this.postCommands.put("login", loginCommand);
		this.postCommands.put("verificaLogin", verificaLoginCommand);
		this.postCommands.put("novoUsuario", novoUsuarioCommand);

		this.getCommands = new HashMap<>();
		this.getCommands.put("loginForm", loginFormCommand);
		this.getCommands.put("novoUsuarioForm", novoUsuarioFormCommand);
		this.getCommands.put("logout", logoutCommand);

        controller = new ControllerUsuario(postCommands, getCommands);
    }


	@ParameterizedTest
	@CsvSource({
		"login, loginCommand",
		"verificaLogin, verificaLoginCommand",
		"novoUsuario, novoUsuarioCommand",
	})
	void testDoPost(String action, String commandMock) throws IOException, ServletException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		// arrange
		when(request.getParameter(PARAM_ACAO)).thenReturn(action);

		// act
		controller.doPost(request, response);

		Command command = 
			(Command) this.getClass().getDeclaredField(commandMock).get(this);
		verify(command).executa(request, response);
	}


	@ParameterizedTest
	@CsvSource({
		"loginForm, loginFormCommand",
		"novoUsuarioForm, novoUsuarioFormCommand",
		"logout, logoutCommand",
	})
	void testDoGet(String action, String commandMock) throws IOException, ServletException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		// arrange
		when(request.getParameter(PARAM_ACAO)).thenReturn(action);

		// act
		controller.doGet(request, response);

		Command command = 
			(Command) this.getClass().getDeclaredField(commandMock).get(this);
		verify(command).executa(request, response);
	}

	@Test
    void testInvalidPostCommand() throws IOException, ServletException {
        when(request.getParameter(PARAM_ACAO)).thenReturn(PARAMETRO_INVALIDO);
        controller.doPost(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND));
    }

    @Test
    void testInvalidGetCommand() throws IOException, ServletException {
        when(request.getParameter(PARAM_ACAO)).thenReturn(PARAMETRO_INVALIDO);
        controller.doGet(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND));
    }
}