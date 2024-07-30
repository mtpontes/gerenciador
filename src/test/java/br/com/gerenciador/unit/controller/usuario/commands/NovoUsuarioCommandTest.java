package br.com.gerenciador.unit.controller.usuario.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import br.com.gerenciador.controller.commands.usuario.NovoUsuarioCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.dto.usuario.NovoUsuarioDTO;
import br.com.gerenciador.service.UsuarioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class NovoUsuarioCommandTest {

    @InjectMocks
    private NovoUsuarioCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;
    @Mock
    private UsuarioService usuarioService;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    
    @Test
    @DisplayName("Deve redirecionar para página de login ao cadastrar usuário com sucesso")
    void novoUsuarioTest01() throws ServletException, IOException {
    	//arrange
    	
    	// act/assert
    	assertDoesNotThrow(() -> command.executa(request, response));
    	
    	verify(usuarioService).cadastraUsuario(any(NovoUsuarioDTO.class));

    	verify(response).sendRedirect(stringCaptor.capture());
    	String endereco = stringCaptor.getValue();
        assertTrue(endereco.contains("loginForm"));
    }
    @Test
    @DisplayName("Deve redirecionar para página atual ao falhar em cadastrar usuário")
    void novoUsuarioTest02() throws ServletException, IOException {
    	//arrange
    	willThrow(new FormValidationException(""))
			.given(usuarioService).cadastraUsuario(any());;

    	//act
    	command.executa(request, response);
    	
    	//assert
    	verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("novoUsuarioForm")); 
    	assertEquals("novoUsuarioForm", acao);
    }
    @Test
    @DisplayName("Deve encaminhar a requisicao para página de erro 500")
    void novoUsuarioTest03() throws ServletException, IOException {
    	//arrange
    	given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	willThrow(new DatabaseAccessException(""))
			.given(usuarioService).cadastraUsuario(any());;
    	given(request.getRequestDispatcher(anyString())).willReturn(rd);
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	String acao = endereco.substring(endereco.indexOf("500.html")); 
    	assertEquals("500.html", acao);
    }
}