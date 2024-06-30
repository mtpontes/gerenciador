package br.com.gerenciador.unit.controller.usuario.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
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

import br.com.gerenciador.controller.commands.usuario.LogoutCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class LogoutCommandTest {

    @InjectMocks
    private LogoutCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    @Captor
    private ArgumentCaptor<String> stringCaptor;


    @Test
    @DisplayName("Deve redirecionar para a de login ao deslogar com sucesso")
    void logoutTest() throws ServletException, IOException {
    	//arrange
    	given(request.getSession()).willReturn(session);
    	
    	//act
    	command.executa(request, response);
    	
    	//asert
    	verify(session).invalidate();
    	verify(response).sendRedirect(stringCaptor.capture());
    	
    	String endereco = stringCaptor.getValue();
    	assertTrue(endereco.contains("loginForm"));
    }
}