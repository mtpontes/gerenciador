package br.com.alura.gerenciador.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.dto.empresa.request.AlteraEmpresaDTO;
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
    @Captor
    private ArgumentCaptor<AlteraEmpresaDTO> empresaDtoCaptor;
    @Captor
    private ArgumentCaptor<Boolean> booleanCaptor;
    
    private static final String PARAM_ACAO = "acao";
    private static final String PARAMETRO_INVALIDO = "umParametroInvalido";
    private static final String SESSION_ATRIBUTE = "usuarioLogado";

    private static final String PARAM_NOME = "nome";
    private static final String PARAM_NOME_VALUE = "Um Nome";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_DATA_VALUE = "01/01/2001";
	
	private static final String RESPOSTA_VALIDA_CHAVE = "response";
	private static final String RESPOSTA_VALIDA_VALOR = "true";
	private static final String RESPOSTA_ERRO_CHAVE = "error";


    @Test
    @DisplayName("Deve retornar erro 404")
    void doPostTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doPost(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
    @DisplayName("Deve redirecionar para um endereço específico")
    void novaEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novaEmpresa");
    	
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getSession()).willReturn(session);
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = extractParamFromUrl(endereco, PARAM_ACAO);
    	Assertions.assertEquals("listaEmpresasUsuario", acao);
    }
    @Test
    @DisplayName("Deve redirecionar para página de erro")
    void novaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novaEmpresa");
    	
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
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    //search
    @Test
    @DisplayName("Deve receber/retornar um request application/json")
    void searchTest01() throws ServletException, IOException {
    	//arrange
    	List<EmpresaBaseDTO> listaEmpresas = List.of(
    			new EmpresaBaseDTO("nome-1", "15/01/2024"),
    			new EmpresaBaseDTO("nome-2", "16/01/2024")
    			);
    	
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("search");
    	BDDMockito.given(request.getContentType()).willReturn("application/json");
    	
    	BDDMockito.given(request.getParameter("nomeEmpresa")).willReturn("nome");
    	BDDMockito.given(request.getParameter("page")).willReturn("1");
    	BDDMockito.given(request.getParameter("size")).willReturn("5");
    	
    	BDDMockito.given(empresaService.getEmpresasByNamePaged(anyString(), anyInt(), anyInt())).willReturn(listaEmpresas);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	
    	//act
    	controller.doGet(request, response);
    	
    	
    	//assert
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	var empresasArray = resposta.get("empresas").getAsJsonArray();
    	Assertions.assertTrue(empresasArray.isJsonArray() && empresasArray.size() == 2);
    	
    	Assertions.assertTrue(resposta.has("acao") && resposta.has("empresas") && resposta.has("pagination"));
    }
    
    @Test
    @DisplayName("Deve receber/retornar um request x-www-form-urlencoded")
    void searchTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("search");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(request).should(times(5)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve receber/retornar um request application/json")
    void listaEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	List<EmpresaBaseDTO> listaEmpresas = List.of(
    			new EmpresaBaseDTO("nome-1", "15/01/2024"),
    			new EmpresaBaseDTO("nome-2", "16/01/2024")
    			);
    	
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresas");
    	BDDMockito.given(request.getContentType()).willReturn("application/json");
    	BDDMockito.given(empresaService.getEmpresasPaged(anyInt(), anyInt())).willReturn(listaEmpresas);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	
    	//act
    	controller.doGet(request, response);
    	
    	
    	//assert
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonArray resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonArray();
    	
    	var nome1 = resposta.get(0).getAsJsonObject().get("nome").getAsJsonPrimitive().getAsString();
    	var nome2 = resposta.get(1).getAsJsonObject().get("nome").getAsJsonPrimitive().getAsString();
    	
    	Assertions.assertTrue(nome1.equals("nome-1"));
    	Assertions.assertTrue(nome2.equals("nome-2"));
    	Assertions.assertTrue(resposta.size() == 2);
    }
    @Test
    @DisplayName("Deve receber/retornar um request x-www-form-urlencoded")
    void listaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresas");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(request).should(times(4)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    

    @Test
    @DisplayName("Deve receber/responder um request application/json")
    void listaEmpresaUsuarioTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasUsuario");
    	BDDMockito.given(request.getContentType()).willReturn("application/json");
    	
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	Assertions.assertTrue(resposta.has("empresas"));
    	Assertions.assertTrue(resposta.has("pagination"));
    	Assertions.assertTrue(resposta.has("acao"));
    	
    	BDDMockito.verify(empresaService).getCountEmpresasUsuarioAtivo(anyLong(), booleanCaptor.capture());
    	Assertions.assertTrue(booleanCaptor.getValue());
    }
    @Test
    @DisplayName("Deve fazer consulta por empresas desativadas")
    void listaEmpresaUsuarioTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasDesativadasUsuario");
    	BDDMockito.given(request.getContentType()).willReturn("application/json");
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.verify(empresaService).getCountEmpresasUsuarioAtivo(anyLong(), booleanCaptor.capture());
    	Assertions.assertFalse(booleanCaptor.getValue());
    }
    @Test
    @DisplayName("Deve receber/responder um request x-www-form-urlencoded")
    void listaEmpresaUsuarioTest03() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasUsuario");

    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(request).should(Mockito.times(4)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve repassar a requisicao")
    void novaEmpresaFormTest() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novaEmpresaForm");
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
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn(PARAMETRO_INVALIDO);
    	//act
    	controller.doPost(request, response);
    	//assert
    	BDDMockito.then(response).should().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
	@DisplayName("Deve retornar JSON valido")
    void removeEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("removeEmpresa");
    	
    	String corpoRequisicao = "{\"empresaId\": 123}";
        BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doPut(request, response);
    	
    	//assert
    	Assertions.assertDoesNotThrow(() -> empresaService.arquivaEmpresa(anyLong(), anyLong()));
    	BDDMockito.then(out).should().print(anyString());
    	
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	Assertions.assertTrue(json.has(RESPOSTA_VALIDA_CHAVE));
    	Assertions.assertEquals(json.get(RESPOSTA_VALIDA_CHAVE).toString(), RESPOSTA_VALIDA_VALOR);
    }
    
    @Test
	@DisplayName("Status code 500 e JSON de erro")
    void removeEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("removeEmpresa");
    	
    	String corpoRequisicao = "{\"empresaId\": 123}";
        BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	BDDMockito.doThrow(new PersistenceException()).when(empresaService).arquivaEmpresa(anyLong(), anyLong());
    	
    	//act
    	controller.doPut(request, response);

    	//assert
    	BDDMockito.then(empresaService).should().arquivaEmpresa(anyLong(), anyLong());
    	BDDMockito.then(response).should().getWriter();
    	
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	Assertions.assertTrue(json.has(RESPOSTA_ERRO_CHAVE));

    	BDDMockito.then(response).should().setStatus(statusCodeCaptor.capture());
		int statusCode = statusCodeCaptor.getValue();
    	Assertions.assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    
    @Test
    @DisplayName("Deve atualizar dados de Empresa")
    void atualizaEmpresa01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("atualizaEmpresa");

    	String requestBody = "{\"nome\":\"EmpresaX\",\"data\":\"2022-01-01\",\"id\":1}";
        BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(requestBody)));
        BDDMockito.given(request.getSession()).willReturn(session);
        BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
        BDDMockito.given(response.getWriter()).willReturn(out);
        
       
        controller.doPut(request, response);

        //act
        verify(empresaService).alteraDadosEmpresa(empresaDtoCaptor.capture(), any());
        AlteraEmpresaDTO capturado = empresaDtoCaptor.getValue();

        //assert
        Assertions.assertEquals("EmpresaX", capturado.base().nome());
        Assertions.assertEquals("2022-01-01", capturado.base().data());
        Assertions.assertEquals(1L, capturado.id());
        
        verify(out).print(anyString());
    }
    
    @Test
    @DisplayName("Deve retornar json de erro")
    void atualizaEmpresa02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("atualizaEmpresa");
    	
    	String corpoRequisicao = "{\"nome\":\"EmpresaX\",\"data\":\"2022-01-01\",\"id\":1}";
    	BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
        BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	BDDMockito.willThrow(new FormValidationException("")).given(empresaService).alteraDadosEmpresa(any(), any());;
    	
    	//act
    	controller.doPut(request, response);
    	
    	BDDMockito.verify(response).setStatus(statusCodeCaptor.capture());
    	int currentStatus = statusCodeCaptor.getValue();
    	
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	//assert
    	Assertions.assertEquals(currentStatus, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	Assertions.assertTrue(json.has(RESPOSTA_ERRO_CHAVE));
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