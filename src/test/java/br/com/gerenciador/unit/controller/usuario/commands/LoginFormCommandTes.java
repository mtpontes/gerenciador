package br.com.gerenciador.unit.controller.usuario.commands;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.controller.commands.usuario.LoginFormCommand;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class LoginFormCommandTes {

    @InjectMocks
    private LoginFormCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher rd;


    @Test
    @DisplayName("Deve encaminhar request para a página de login")
    void loginFormTest() throws ServletException, IOException {
    	//arrange
    	given(request.getRequestDispatcher(anyString())).willReturn(rd);

    	//act
    	command.executa(request, response);
        
    	//assert
    	then(rd).should().forward(request, response);
    }
}