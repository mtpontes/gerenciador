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

import br.com.gerenciador.controller.commands.empresa.ListaEmpresasCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.EmpresaService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ListaEmpresasCommandTest {

    @InjectMocks
    private ListaEmpresasCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;
    @Mock
    private EmpresaService empresaService;
    @Mock
    private Usuario usuario;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    

    @Test
    @DisplayName("Deve encaminhar o request para a página de listar empresas")
    void listaEmpresaTest01() throws ServletException, IOException {
    	//arrange
    	given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
        //act
        command.executa(request, response);

    	//assert
    	then(request).should(times(5)).setAttribute(anyString(), any());
    	verify(request).getRequestDispatcher(any());
    	then(rd).should().forward(request, response);
    }
    @Test
    @DisplayName("Deve encaminhar o request para a página de erro 500")
    void listaEmpresaTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	willThrow(new DatabaseAccessException("")).given(empresaService).getEmpresasPaged(any());

    	//act
        command.executa(request, response);
    	
    	//assert
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("500.html"));
    	assertEquals(enderecoHtml, "500.html");
    	
    	then(rd).should().forward(request, response);
    }
}