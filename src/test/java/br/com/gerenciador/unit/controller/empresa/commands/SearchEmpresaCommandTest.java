package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.controller.empresa.commands.SearchCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.service.EmpresaService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class SearchEmpresaCommandTest {

    @InjectMocks
    private SearchCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;
    @Mock
    private EmpresaService empresaService;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    

    @Test
    @DisplayName("Deve deve encaminhar o request para a página de pesquisas")
    void searchTest01() throws ServletException, IOException {
    	//arrange
    	given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	then(request).should(times(6)).setAttribute(anyString(), any());
    	verify(request).getRequestDispatcher(any());
    	then(rd).should().forward(request, response);
    }
    @Test
    @DisplayName("Deve deve encaminhar o request para a página de erro 500")
    void searchTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	willThrow(new DatabaseAccessException("")).given(empresaService).getEmpresasByNamePaged(any(), any());
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	then(request).should(times(0)).setAttribute(anyString(), any());
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("500.html"));
    	assertEquals(enderecoHtml, "500.html");
    	
    	then(rd).should().forward(request, response);
    }
}