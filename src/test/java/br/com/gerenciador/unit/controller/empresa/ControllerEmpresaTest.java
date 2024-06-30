package br.com.gerenciador.unit.controller.empresa;

import static org.mockito.ArgumentMatchers.any;
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

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.controller.empresa.ControllerEmpresa;
import br.com.gerenciador.controller.empresa.commands.AtualizaEmpresaCommand;
import br.com.gerenciador.controller.empresa.commands.ListaEmpresasAtivoUsuarioAjaxCommand;
import br.com.gerenciador.controller.empresa.commands.ListaEmpresasCommand;
import br.com.gerenciador.controller.empresa.commands.ListaEmpresasUsuarioCommand;
import br.com.gerenciador.controller.empresa.commands.NovaEmpresaCommand;
import br.com.gerenciador.controller.empresa.commands.NovaEmpresaFormCommand;
import br.com.gerenciador.controller.empresa.commands.RemoveEmpresaCommand;
import br.com.gerenciador.controller.empresa.commands.SearchAjaxCommand;
import br.com.gerenciador.controller.empresa.commands.SearchCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ControllerEmpresaTest {

    private static final String PARAM_ACAO = "acao";
    private static final String PARAMETRO_INVALIDO = "umParametroInvalido";

    @InjectMocks
    private ControllerEmpresa controller;
    
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

	@Mock
	private NovaEmpresaCommand novaEmpresaCommand;
	@Mock
	private SearchCommand searchCommand;
	@Mock
	private SearchAjaxCommand searchAjaxCommand;
	@Mock
	private ListaEmpresasCommand listaEmpresasCommand;
	@Mock
	private ListaEmpresasUsuarioCommand listaEmpresasUsuarioCommand;
	@Mock
	private ListaEmpresasAtivoUsuarioAjaxCommand listaEmpresasAtivoUsuarioAjaxCommand;
	@Mock
	private NovaEmpresaFormCommand formNovaEmpresaCommand;
	@Mock
	private RemoveEmpresaCommand removeEmpresaCommand;
	@Mock
	private AtualizaEmpresaCommand atualizaEmpresaCommand;

	private Map<String, Command> postCommands; 
	private Map<String, Command> getCommands; 
	private Map<String, Command> putCommands;

	@BeforeEach
    void setUp() {
        postCommands = new HashMap<>();
        postCommands.put("novaEmpresa", novaEmpresaCommand);

		getCommands = new HashMap<>();
        getCommands.put("search", searchCommand);
        getCommands.put("searchAjax", searchAjaxCommand);
        getCommands.put("listaEmpresas", listaEmpresasCommand);
        getCommands.put("listaEmpresasUsuario", listaEmpresasUsuarioCommand);
        getCommands.put("listaEmpresasAtivoUsuarioAjax", listaEmpresasAtivoUsuarioAjaxCommand);
        getCommands.put("novaEmpresaForm", formNovaEmpresaCommand);

		putCommands = new HashMap<>();
        putCommands.put("removeEmpresa", removeEmpresaCommand);
        putCommands.put("atualizaEmpresa", atualizaEmpresaCommand);

        controller = new ControllerEmpresa(postCommands, getCommands, putCommands);
    }

    
    @ParameterizedTest
    @CsvSource({
        "novaEmpresa, novaEmpresaCommand"
    })
    void testDoPost(String action, String commandMock) throws IOException, ServletException {
        // arrange
        when(request.getParameter(PARAM_ACAO)).thenReturn(action);

        // act
        controller.doPost(request, response);

        // assert
        switch (commandMock) {
            case "novaEmpresaCommand":
                verify(novaEmpresaCommand).executa(any(HttpServletRequest.class), any(HttpServletResponse.class));
                break;
        }
    }

    @ParameterizedTest
    @CsvSource({
        "search, searchCommand",
        "searchAjax, searchAjaxCommand",
        "listaEmpresas, listaEmpresasCommand",
        "listaEmpresasUsuario, listaEmpresasUsuarioCommand",
        "listaEmpresasAtivoUsuarioAjax, listaEmpresasAtivoUsuarioAjaxCommand",
        "novaEmpresaForm, formNovaEmpresaCommand"
    })
    void testDoGet(String action, String commandMock) throws IOException, ServletException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // arrange
        when(request.getParameter(PARAM_ACAO)).thenReturn(action);

        // act
        controller.doGet(request, response);

        // assert
		Command command = (Command) this.getClass().getDeclaredField(commandMock).get(this);
		verify(command).executa(request, response);
    }

    @ParameterizedTest
    @CsvSource({
        "removeEmpresa, removeEmpresaCommand",
        "atualizaEmpresa, atualizaEmpresaCommand"
    })
    void testDoPut(String action, String commandMock) throws IOException, ServletException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // arrange
        when(request.getParameter(PARAM_ACAO)).thenReturn(action);

        // act
        controller.doPut(request, response);

        // assert
		Command command = (Command) this.getClass().getDeclaredField(commandMock).get(this);
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