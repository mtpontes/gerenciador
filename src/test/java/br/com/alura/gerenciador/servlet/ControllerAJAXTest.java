package br.com.alura.gerenciador.servlet;

import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.io.PrintWriter;

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

import br.com.alura.gerenciador.service.UsuarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ControllerAJAXTest {

	@InjectMocks
	private ControllerAJAX controller;
	@Mock
	private EntityManager em;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private UsuarioService service;
	@Mock
	private PrintWriter out;

	@Captor
	private ArgumentCaptor<String> stringJsonCaptor;
	@Captor
	private ArgumentCaptor<Integer> statusCodeCaptor;
	
    private static final String URL_PARAM_METHOD = "acao";
    private static final String PARAMETRO_INVALIDO = "umParametroInvalido";

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_LOGIN_VALUE = "umLogin";
	
	private static final String RESPOSTA_VALIDA_CHAVE = "response";
	private static final String RESPOSTA_VALIDA_VALOR = "true";
	
	private static final String RESPOSTA_ERRO_CHAVE = "error";
	
	
    @Test
	@DisplayName("Erro 404 quando o parâmetro 'acao' é inválido")
    void doGetTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
	
	@Test
	@DisplayName("Deve retornar JSON válido ao verificar login existente")
	void verificaLoginTest01() throws ServletException, IOException {
		//arrange
		BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("verificaLogin");
		
		BDDMockito.given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
		BDDMockito.given(service.verificaSeLoginExiste(PARAM_LOGIN_VALUE)).willReturn(true);
		BDDMockito.given(response.getWriter()).willReturn(out);
		
		//act
		controller.doGet(request, response);
		
		//assert
		BDDMockito.then(response).should().getWriter();
		
		BDDMockito.then(out).should().print(stringJsonCaptor.capture());
		JsonObject jsonResponse = JsonParser.parseString(stringJsonCaptor.getValue()).getAsJsonObject();
		Assertions.assertEquals(jsonResponse.get(RESPOSTA_VALIDA_CHAVE).toString(), RESPOSTA_VALIDA_VALOR);
		
		BDDMockito.then(out).should().flush();
	}
	
	@Test
	@DisplayName("Status code 500 e JSON de erro")
	void verificaLoginTest02() throws ServletException, IOException {
		//arrange
		BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("verificaLogin");
		
		BDDMockito.given(request.getParameter(PARAM_LOGIN)).willReturn(PARAM_LOGIN_VALUE);
		BDDMockito.willThrow(new PersistenceException()).given(service).verificaSeLoginExiste(anyString());
		BDDMockito.given(response.getWriter()).willReturn(out);
		
		//act
		controller.doGet(request, response);

		//assert
		BDDMockito.then(response).should().getWriter();

		BDDMockito.then(response).should().setStatus(statusCodeCaptor.capture());
		int statusCode = statusCodeCaptor.getValue();
		Assertions.assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		BDDMockito.then(out).should().print(stringJsonCaptor.capture());
		JsonObject jsonResponse = JsonParser.parseString(stringJsonCaptor.getValue()).getAsJsonObject();
		Assertions.assertTrue(jsonResponse.has(RESPOSTA_ERRO_CHAVE));

		BDDMockito.then(out).should().flush();
	}
}
