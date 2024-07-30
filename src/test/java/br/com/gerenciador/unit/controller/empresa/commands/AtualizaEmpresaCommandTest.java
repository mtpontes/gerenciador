package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

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
import com.google.gson.JsonParser;

import br.com.gerenciador.controller.commands.empresa.AtualizaEmpresaCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.service.EmpresaService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class AtualizaEmpresaCommandTest {

    @InjectMocks
    private AtualizaEmpresaCommand command;
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
    private ArgumentCaptor<AlteraEmpresaDTO> empresaDtoCaptor;

	private static final String RESPOSTA_JSON = "message";


    @Test
    @DisplayName("Deve atualizar dados de Empresa")
    void atualizaEmpresa01() throws ServletException, IOException {
    	//arrange
    	String requestBody = "{\"nome\":\"EmpresaX\",\"data\":\"2022-01-01\",\"id\":1}";
        given(request.getReader())
            .willReturn(new BufferedReader(new StringReader(requestBody)));
        given(request.getSession()).willReturn(this.session);
        given(session.getAttribute("usuarioLogado"))
            .willReturn(this.usuario);
        given(response.getWriter()).willReturn(this.out);
       
        //act
        command.executa(request, response);

        //assert
        verify(empresaService).alteraDadosEmpresa(empresaDtoCaptor.capture(), any());
        AlteraEmpresaDTO capturado = empresaDtoCaptor.getValue();

        assertEquals("EmpresaX", capturado.base().nome());
        assertEquals("2022-01-01", capturado.base().data());
        assertEquals(1L, capturado.id());
        
        verify(out).print(anyString());
    }
    @Test
    @DisplayName("Deve retornar json de erro e status code 400")
    void atualizaEmpresa02() throws ServletException, IOException {
    	//arrange
    	String corpoRequisicao = "{\"nome\":\"EmpresaX\",\"data\":\"2022-01-01\",\"id\":1}";
    	given(request.getReader())
            .willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
        given(request.getSession()).willReturn(session);
    	given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	given(response.getWriter()).willReturn(out);
    	
    	willThrow(new FormValidationException(""))
            .given(empresaService).alteraDadosEmpresa(any(), any());;
    	
    	//act
        command.executa(request, response);
    	
    	verify(response).setStatus(statusCodeCaptor.capture());
    	int currentStatus = statusCodeCaptor.getValue();
    	
    	then(out).should().print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue())
            .getAsJsonObject();
    	
    	//assert
    	assertEquals(currentStatus, HttpServletResponse.SC_BAD_REQUEST);
    	assertTrue(json.has(RESPOSTA_JSON));
    }
    @Test
    @DisplayName("Deve retornar json de erro e status code 500")
    void atualizaEmpresa03() throws ServletException, IOException {
    	//arrange
    	String corpoRequisicao = 
            "{\"nome\":\"EmpresaX\",\"data\":\"2022-01-01\",\"id\":1}";
    	given(request.getReader())
            .willReturn(new BufferedReader(new StringReader(corpoRequisicao)));
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute("usuarioLogado")).willReturn(usuario);
    	given(response.getWriter()).willReturn(out);
    	
    	willThrow(new DatabaseAccessException(""))
            .given(empresaService).alteraDadosEmpresa(any(), any());;
    	
    	//act
        command.executa(request, response);
    	
    	verify(response).setStatus(statusCodeCaptor.capture());
    	int currentStatus = statusCodeCaptor.getValue();
    	
    	then(out).should().print(stringCaptor.capture());
    	JsonObject json = JsonParser.parseString(stringCaptor.getValue())
            .getAsJsonObject();
    	
    	//assert
    	assertEquals(currentStatus, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	assertTrue(json.has(RESPOSTA_JSON));
    }
}