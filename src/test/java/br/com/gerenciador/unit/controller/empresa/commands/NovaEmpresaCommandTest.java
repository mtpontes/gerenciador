package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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

import br.com.gerenciador.controller.commands.empresa.NovaEmpresaCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.service.EmpresaService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class NovaEmpresaCommandTest {

    @InjectMocks
    private NovaEmpresaCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;
    @Mock
    private HttpSession session;
    @Mock
    private EmpresaService empresaService;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<AlteraEmpresaDTO> empresaDtoCaptor;
    
    private static final String PARAM_NOME = "nome";
    private static final String PARAM_NOME_VALUE = "Um Nome";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_DATA_VALUE = "01/01/2001";
	

    @Test
    @DisplayName("Deve redirecionar para um endereço específico")
    void novaEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	given(request.getSession()).willReturn(session);
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
		assertTrue(endereco.contains("listaEmpresasUsuario"));
    }
    @Test
    @DisplayName("Deve redirecionar para a mesma página caso lance uma FormValidationException")
    void novaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	given(request.getSession()).willReturn(session);
    	willThrow(new FormValidationException("")).given(empresaService).cadastraEmpresa(any());
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("novaEmpresaForm")); 
    	assertEquals("novaEmpresaForm", acao);
    }
    @Test
    @DisplayName("Deve redirecionar para página de erro")
    void novaEmpresaTest03() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter(PARAM_NOME)).willReturn(PARAM_NOME_VALUE);
    	given(request.getParameter(PARAM_DATA)).willReturn(PARAM_DATA_VALUE);
    	given(request.getSession()).willReturn(session);
    	given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	willThrow(new DatabaseAccessException("")).given(empresaService).cadastraEmpresa(any());;
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("500.html")); 
    	assertEquals("500.html", acao);
    }
}