package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gerenciador.controller.commands.empresa.ListaEmpresasAtivoUsuarioAjaxCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.EmpresaService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ListaEmpresasAtivoUsuarioAjaxCommandTest {

    @InjectMocks
    private ListaEmpresasAtivoUsuarioAjaxCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private EntityManager em;
    @Mock
    private EmpresaService empresaService;
    @Mock
    private Usuario usuario;
    @Mock
    private Empresa empresa;
    @Mock
    private PrintWriter out;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Integer> statusCodeCaptor;
    @Captor
    private ArgumentCaptor<Boolean> booleanCaptor;

    private static final String SESSION_ATRIBUTE = "usuarioLogado";
    

    @Test
    @DisplayName("Deve retornar um json array contendo empresas do usuário")
    void listaEmpresaUsuarioAjaxTest01() throws ServletException, IOException {
    	//arrange
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	given(usuario.getId()).willReturn(1l);
    	
    	given(response.getWriter()).willReturn(out);
    	
    	//act
        command.executa(request, response);
    	
    	//assert
    	verify(empresaService).getCountEmpresasUsuarioAtivo(anyLong(), booleanCaptor.capture());
    	assertTrue(booleanCaptor.getValue());

    	then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	assertTrue(resposta.has("empresas"));
    	assertTrue(resposta.has("pagination"));
    	
    	assertDoesNotThrow(() -> empresaService.getCountEmpresasUsuarioAtivo(anyLong(), booleanCaptor.capture()));
    }
    @Test
    @DisplayName("Deve retornar json com mensagem de erro")
    void listaEmpresaUsuarioAjaxTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	given(usuario.getId()).willReturn(1l);
    	
    	willThrow(new DatabaseAccessException("test")).given(empresaService).getEmpresasAtivoUsuarioPaged(any(), any(), any());
    	given(response.getWriter()).willReturn(out);
    	
    	//act
        command.executa(request, response);
    	
    	//assert
    	verify(out).print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	assertTrue(json.has("message"));
    	assertEquals(json.get("message").getAsString(), "test");

    	then(response).should().setStatus(statusCodeCaptor.capture());
    	assertEquals(statusCodeCaptor.getValue(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}