package br.com.gerenciador.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gerenciador.controller.ControllerEmpresa;
import br.com.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.service.EmpresaService;
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
    @Mock
    private Pagination pgMock;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<JsonObject> jsonCaptor;
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
	private static final String RESPOSTA_JSON = "message";
	

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
    @DisplayName("Deve redirecionar para a mesma página caso lance uma FormValidationException")
    void novaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novaEmpresa");
    	
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.willThrow(new FormValidationException("")).given(empresaService).cadastraEmpresa(any());
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	System.out.println(endereco);
    	String acao = endereco.substring(endereco.indexOf("novaEmpresaForm")); 
    	Assertions.assertEquals("novaEmpresaForm", acao);
    }
    @Test
    @DisplayName("Deve redirecionar para página de erro")
    void novaEmpresaTest03() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("novaEmpresa");
    	
    	BDDMockito.given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	BDDMockito.given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(empresaService).cadastraEmpresa(any());;
    	
    	//act
    	controller.doPost(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("500.html")); 
    	Assertions.assertEquals("500.html", acao);
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
    
    @Test
    @DisplayName("Deve deve encaminhar o request para a página de pesquisas")
    void searchTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("search");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(request).should(times(6)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    @Test
    @DisplayName("Deve deve encaminhar o request para a página de erro 500")
    void searchTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("search");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(empresaService).getEmpresasByNamePaged(any(), any());
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(request).should(times(0)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("500.html"));
    	Assertions.assertEquals(enderecoHtml, "500.html");
    	
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve retornar um json array contendo empresas")
    void searchAjaxTest01() throws ServletException, IOException {
    	//arrange
    	List<EmpresaBaseDTO> listaEmpresas = List.of(
    			new EmpresaBaseDTO("nome-1", "15/01/2024"),
    			new EmpresaBaseDTO("nome-2", "16/01/2024")
    			);
    	
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("searchAjax");
    	
    	BDDMockito.given(request.getParameter("nomeEmpresa")).willReturn("nome");
    	BDDMockito.given(request.getParameter("page")).willReturn("1");
    	BDDMockito.given(request.getParameter("size")).willReturn("5");
    	
    	BDDMockito.given(empresaService.getEmpresasByNamePaged(any(), anyString())).willReturn(listaEmpresas);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	var empresasArray = resposta.get("empresas").getAsJsonArray();
    	Assertions.assertTrue(empresasArray.isJsonArray() && empresasArray.size() == 2);
    	Assertions.assertTrue(resposta.has("empresas") && resposta.has("pagination"));
    }
    @Test
    @DisplayName("Deve retornar um json com mensagem de erro")
    void searchAjaxTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("searchAjax");
    	
    	BDDMockito.given(request.getParameter("nomeEmpresa")).willReturn("nome");
    	BDDMockito.given(request.getParameter("page")).willReturn("1");
    	BDDMockito.given(request.getParameter("size")).willReturn("5");
    	
    	BDDMockito.willThrow(new DatabaseAccessException("test")).given(empresaService).getEmpresasByNamePaged(any(), any());
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	Assertions.assertTrue(resposta.has("message"));
    	Assertions.assertEquals(resposta.get("message").getAsString(), "test");
    }
    
    @Test
    @DisplayName("Deve encaminhar o request para a página de listar empresas")
    void listaEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresas");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	//act
    	controller.doGet(request, response);
    	//assert
    	BDDMockito.then(request).should(times(5)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(any());
    	BDDMockito.then(rd).should().forward(request, response);
    }
    @Test
    @DisplayName("Deve encaminhar o request para a página de erro 500")
    void listaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresas");
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(empresaService).getEmpresasPaged(any());

    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("500.html"));
    	Assertions.assertEquals(enderecoHtml, "500.html");
    	
    	BDDMockito.then(rd).should().forward(request, response);
    }

    @Test
    @DisplayName("Deve encaminhar o request para página de empresas do usuário")
    void listaEmpresaUsuarioTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasUsuario");

    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.then(request).should(Mockito.times(5)).setAttribute(anyString(), any());
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("usuarioEmpresas.jsp"));
    	Assertions.assertTrue(enderecoHtml.equals("usuarioEmpresas.jsp"));
    	
    	BDDMockito.then(rd).should().forward(request, response);
    }
    @Test
    @DisplayName("Deve encaminhar o request para a página de erro 500")
    void listaEmpresaUsuarioTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasUsuario");
    	
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	BDDMockito.willThrow(new PersistenceException("")).given(empresaService).getEmpresasAtivoUsuarioPaged(any(), anyLong(), anyBoolean());
    	BDDMockito.given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.verify(request).getRequestDispatcher(stringCaptor.capture());
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("500.html"));
    	Assertions.assertTrue(enderecoHtml.equals("500.html"));
    	
    	BDDMockito.then(rd).should().forward(request, response);
    }
    
    @Test
    @DisplayName("Deve retornar um json array contendo empresas do usuário")
    void listaEmpresaUsuarioAjaxTest01() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasAtivoUsuarioAjax");
    	
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.verify(empresaService).getCountEmpresasUsuarioAtivo(anyLong(), booleanCaptor.capture());
    	Assertions.assertTrue(booleanCaptor.getValue());

    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	Assertions.assertTrue(resposta.has("empresas"));
    	Assertions.assertTrue(resposta.has("pagination"));
    	
    	Assertions.assertDoesNotThrow(() -> empresaService.getCountEmpresasUsuarioAtivo(anyLong(), booleanCaptor.capture()));
    }
    @Test
    @DisplayName("Deve retornar json com mensagem de erro")
    void listaEmpresaUsuarioAjaxTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("listaEmpresasAtivoUsuarioAjax");
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	BDDMockito.given(usuario.getId()).willReturn(1l);
    	
    	BDDMockito.willThrow(new DatabaseAccessException("test")).given(empresaService).getEmpresasAtivoUsuarioPaged(any(), any(), any());
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	//act
    	controller.doGet(request, response);
    	
    	//assert
    	BDDMockito.verify(out).print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	Assertions.assertTrue(json.has("message"));
    	Assertions.assertEquals(json.get("message").getAsString(), "test");

    	BDDMockito.then(response).should().setStatus(statusCodeCaptor.capture());
    	Assertions.assertEquals(statusCodeCaptor.getValue(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
    	
    	BDDMockito.then(out).should().print(jsonCaptor.capture());
    	JsonObject json = jsonCaptor.getValue();

    	Assertions.assertTrue(json.has("response"));
    	Assertions.assertEquals(json.get("response").toString(), RESPOSTA_VALIDA_VALOR);
    }
    @Test
    @DisplayName("Deve retornar status code 500 e JSON de erro")
    void removeEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("removeEmpresa");
    	
    	String corpoRequisicao = "{\"empresaId\": 123}";
        BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	BDDMockito.doThrow(new DatabaseAccessException("")).when(empresaService).arquivaEmpresa(anyLong(), anyLong());
    	
    	//act
    	controller.doPut(request, response);

    	//assert
    	BDDMockito.then(empresaService).should().arquivaEmpresa(anyLong(), anyLong());
    	BDDMockito.then(response).should().getWriter();
    	
    	BDDMockito.then(out).should().print(jsonCaptor.capture());
    	JsonObject json = jsonCaptor.getValue();
    	Assertions.assertTrue(json.has(RESPOSTA_JSON));

    	BDDMockito.then(response).should().setStatus(statusCodeCaptor.capture());
		int statusCode = statusCodeCaptor.getValue();
    	Assertions.assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    @Test
    @DisplayName("Deve retornar status code 403 e JSON de erro")
    void removeEmpresaTest03() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("removeEmpresa");
    	
    	String corpoRequisicao = "{\"empresaId\": 123}";
    	BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	BDDMockito.doThrow(new IllegalStateException("")).when(empresaService).arquivaEmpresa(anyLong(), anyLong());
    	
    	//act
    	controller.doPut(request, response);
    	
    	//assert
    	BDDMockito.then(empresaService).should().arquivaEmpresa(anyLong(), anyLong());
    	BDDMockito.then(response).should().getWriter();
    	
    	BDDMockito.then(out).should().print(jsonCaptor.capture());
    	JsonObject json = jsonCaptor.getValue();
    	Assertions.assertTrue(json.has(RESPOSTA_JSON));
    	
    	BDDMockito.then(response).should().setStatus(statusCodeCaptor.capture());
    	int statusCode = statusCodeCaptor.getValue();
    	Assertions.assertEquals(statusCode, HttpServletResponse.SC_FORBIDDEN);
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
       
        //act
        controller.doPut(request, response);

        //assert
        verify(empresaService).alteraDadosEmpresa(empresaDtoCaptor.capture(), any());
        AlteraEmpresaDTO capturado = empresaDtoCaptor.getValue();

        Assertions.assertEquals("EmpresaX", capturado.base().nome());
        Assertions.assertEquals("2022-01-01", capturado.base().data());
        Assertions.assertEquals(1L, capturado.id());
        
        verify(out).print(anyString());
    }
    @Test
    @DisplayName("Deve retornar json de erro e status code 400")
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
    	Assertions.assertEquals(currentStatus, HttpServletResponse.SC_BAD_REQUEST);
    	Assertions.assertTrue(json.has(RESPOSTA_JSON));
    }
    @Test
    @DisplayName("Deve retornar json de erro e status code 500")
    void atualizaEmpresa03() throws ServletException, IOException {
    	//arrange
    	BDDMockito.given(request.getParameter(PARAM_ACAO)).willReturn("atualizaEmpresa");
    	
    	String corpoRequisicao = "{\"nome\":\"EmpresaX\",\"data\":\"2022-01-01\",\"id\":1}";
    	BDDMockito.given(request.getReader()).willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	BDDMockito.given(request.getSession()).willReturn(session);
    	BDDMockito.given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	BDDMockito.given(response.getWriter()).willReturn(out);
    	
    	BDDMockito.willThrow(new DatabaseAccessException("")).given(empresaService).alteraDadosEmpresa(any(), any());;
    	
    	//act
    	controller.doPut(request, response);
    	
    	BDDMockito.verify(response).setStatus(statusCodeCaptor.capture());
    	int currentStatus = statusCodeCaptor.getValue();
    	
    	BDDMockito.then(out).should().print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	//assert
    	Assertions.assertEquals(currentStatus, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	Assertions.assertTrue(json.has(RESPOSTA_JSON));
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