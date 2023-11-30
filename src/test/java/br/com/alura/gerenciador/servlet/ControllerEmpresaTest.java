package br.com.alura.gerenciador.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.service.EmpresaService;
import br.com.alura.gerenciador.validation.FormValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ControllerEmpresaTest {

    @InjectMocks
    private ControllerEmpresa controller;
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
    
    private static final String URL_PARAM_METHOD = "acao";
    private static final String PARAMETRO_INVALIDO = "umParametroInvalido";
    private static final String SESSION_ATRIBUTE = "usuarioLogado";

    private static final String PARAM_NOME = "nome";
    private static final String PARAM_NOME_VALUE = "Um Nome";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_DATA_VALUE = "01/01/2001";
    private static final String PARAM_ID = "id";
    private static final String PARAM_ID_VALUE = "1";
	
	private static final String RESPOSTA_VALIDA_CHAVE = "response";
	private static final String RESPOSTA_VALIDA_VALOR = "true";
	private static final String RESPOSTA_ERRO_CHAVE = "error";


    @Test
    @DisplayName("Erro 404 quando o parâmetro 'acao' é inválido")
    void doPostTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doPost(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
    @DisplayName("Deve redirecionar para um endereço específico")
    void alteraEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("alteraEmpresa");
    	
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_ID)).willReturn(PARAM_ID_VALUE);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = extractParamFromUrl(endereco, URL_PARAM_METHOD);
    	Assertions.assertEquals("listaEmpresasUsuario", acao);
    }
    
    @Test
    @DisplayName("Deve redirecionar para página de erro")
    void alteraEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("alteraEmpresa");
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_ID)).willReturn(PARAM_ID_VALUE);
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	BDDMockito.willThrow(new FormValidationException("")).given(empresaService).alteraDadosEmpresa(any());;

    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("validationError.html")); 
    	Assertions.assertEquals("validationError.html", acao);
    }
    
    @Test
    @DisplayName("Deve redirecionar nada um endereço específico")
    void novaEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("novaEmpresa");
    	
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getSession()).willReturn(session);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = extractParamFromUrl(endereco, URL_PARAM_METHOD);
    	Assertions.assertEquals("listaEmpresasUsuario", acao);
    }
    @Test
    @DisplayName("Deve redirecionar para página de erro")
    void novaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("novaEmpresa");
    	
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	BDDMockito.willThrow(new FormValidationException("")).given(empresaService).cadastraEmpresa(any());;

    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("validationError.html")); 
    	Assertions.assertEquals("validationError.html", acao);
    }
    
    
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
    @DisplayName("Deve definir atributo no request e repassar a requisicao")
    void listaEmpresaTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("listaEmpresas");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(request).should().setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve definir atributo no request e repassar a requisicao")
    void listaEmpresaUsuarioTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("listaEmpresasUsuario");

    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(request).should().setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve definir atributo no request e repassa a requisicao")
    void mostraEmpresaTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("mostraEmpresa");
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_ID)).willReturn(PARAM_ID_VALUE);
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(request).should().setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve repassar a requisicao")
    void novaEmpresaFormTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("novaEmpresaForm");
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    
    @Test
    @DisplayName("Erro 404 quando o parâmetro 'acao' é inválido")
    void doPutTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doPost(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
	@DisplayName("Deve retornar JSON valido")
    void removeEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("removeEmpresa");
    	BDDMockito.given(request.getParameter(PARAM_ID)).willReturn(PARAM_ID_VALUE);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doPut(request, response);
    	
    	//assert
    	BDDMockito.then(empresaService).should().atualizaEmpresa(anyLong());
    	BDDMockito.then(response).should().getWriter();
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	Assertions.assertTrue(json.has(RESPOSTA_VALIDA_CHAVE));
    	Assertions.assertEquals(json.get(RESPOSTA_VALIDA_CHAVE).toString(), RESPOSTA_VALIDA_VALOR);
    	
    	BDDMockito.then(out).should().flush();
    }
    
    @Test
	@DisplayName("Status code 500 e JSON de erro")
    void removeEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(URL_PARAM_METHOD)).willReturn("removeEmpresa");
    	BDDMockito.given(request.getParameter(PARAM_ID)).willReturn(PARAM_ID_VALUE);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	BDDMockito.doThrow(new PersistenceException()).when(empresaService).atualizaEmpresa(anyLong());
    	
    	//act
    	controller.doPut(request, response);

    	//assert
    	BDDMockito.then(empresaService).should().atualizaEmpresa(anyLong());
    	BDDMockito.then(response).should().getWriter();
    	
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	Assertions.assertTrue(json.has(RESPOSTA_ERRO_CHAVE));

    	BDDMockito.then(response).should().setStatus(statusCodeCaptor.capture());
		int statusCode = statusCodeCaptor.getValue();
    	Assertions.assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    	BDDMockito.then(out).should().flush();
    }

    private String extractParamFromUrl(String url, String paramName) {
        int startIndex = url.indexOf(paramName + "=");
        if (startIndex != -1) {
            startIndex += paramName.length() + 1;
            int endIndex = url.indexOf('&', startIndex);
            endIndex = (endIndex != -1) ? endIndex : url.length();
            return url.substring(startIndex, endIndex);
        }
        return null;
    }
}