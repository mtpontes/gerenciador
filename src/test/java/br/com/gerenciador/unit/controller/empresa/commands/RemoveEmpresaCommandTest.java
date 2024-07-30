package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;

import br.com.gerenciador.controller.commands.empresa.RemoveEmpresaCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.service.EmpresaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class RemoveEmpresaCommandTest {

    @InjectMocks
    private RemoveEmpresaCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private EmpresaService empresaService;
    @Mock
    private Usuario usuario;
    @Mock
    private PrintWriter out;
    
    @Captor
    private ArgumentCaptor<JsonObject> jsonCaptor;
    @Captor
    private ArgumentCaptor<Integer> statusCodeCaptor;
    @Captor
    private ArgumentCaptor<AlteraEmpresaDTO> empresaDtoCaptor;
    @Captor
    private ArgumentCaptor<Boolean> booleanCaptor;

	private static final String RESPOSTA_VALIDA_VALOR = "true";
	private static final String RESPOSTA_JSON = "message";


    @Test
	@DisplayName("Deve retornar JSON valido")
    void removeEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	String corpoRequisicao = "{\"empresaId\": 123}";
        given(request.getReader())
			.willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	given(response.getWriter()).willReturn(out);
    	
    	//act
        command.executa(request, response);
    	
    	//assert
    	assertDoesNotThrow(() -> empresaService.arquivaEmpresa(anyLong(), anyLong()));
    	
    	then(out).should().print(jsonCaptor.capture());
    	JsonObject json = jsonCaptor.getValue();

    	assertTrue(json.has("response"));
    	assertEquals(json.get("response").toString(), RESPOSTA_VALIDA_VALOR);
    }
    @Test
    @DisplayName("Deve retornar status code 500 e JSON de erro")
    void removeEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	String corpoRequisicao = "{\"empresaId\": 123}";
        given(request.getReader())
			.willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	given(response.getWriter()).willReturn(out);
    	doThrow(new DatabaseAccessException(""))
			.when(empresaService).arquivaEmpresa(anyLong(), anyLong());
    	
    	//act
        command.executa(request, response);

    	//assert
    	then(empresaService).should().arquivaEmpresa(anyLong(), anyLong());
    	then(response).should().getWriter();
    	
    	then(out).should().print(jsonCaptor.capture());
    	JsonObject json = jsonCaptor.getValue();
    	assertTrue(json.has(RESPOSTA_JSON));

    	then(response).should().setStatus(statusCodeCaptor.capture());
		int statusCode = statusCodeCaptor.getValue();
    	assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    @Test
    @DisplayName("Deve retornar status code 403 e JSON de erro")
    void removeEmpresaTest03() throws ServletException, IOException {
    	//arrange
    	String corpoRequisicao = "{\"empresaId\": 123}";
    	given(request.getReader())
			.willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	given(response.getWriter()).willReturn(out);
    	doThrow(new IllegalStateException(""))
			.when(empresaService).arquivaEmpresa(anyLong(), anyLong());
    	
    	//act
        command.executa(request, response);
    	
    	//assert
    	then(empresaService).should().arquivaEmpresa(anyLong(), anyLong());
    	then(response).should().getWriter();
    	
    	then(out).should().print(jsonCaptor.capture());
    	JsonObject json = jsonCaptor.getValue();
    	assertTrue(json.has(RESPOSTA_JSON));
    	
    	then(response).should().setStatus(statusCodeCaptor.capture());
    	int statusCode = statusCodeCaptor.getValue();
    	assertEquals(statusCode, HttpServletResponse.SC_FORBIDDEN);
    }
}