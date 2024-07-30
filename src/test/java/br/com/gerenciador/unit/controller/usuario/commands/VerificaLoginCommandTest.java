package br.com.gerenciador.unit.controller.usuario.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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

import br.com.gerenciador.controller.commands.usuario.VerificaLoginCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class VerificaLoginCommandTest {

    @InjectMocks
    private VerificaLoginCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private PrintWriter out;

    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Integer> statusCodeCaptor;


    @Test
    @DisplayName("Deve retornar json")
    void verificaLoginTest01() throws ServletException, IOException {
    	//arrange
    	String requestBody = "{\"login\":\"umLogin\"}";
        given(request.getReader()).willReturn(
            new BufferedReader(new StringReader(requestBody)));
        given(response.getWriter()).willReturn(out);
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	verify(out).print(stringCaptor.capture());
    	JsonObject jsonResponse = 
            JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	assertTrue(jsonResponse.has("response"));
    }
    @Test
    @DisplayName("Deve retornar json com mensagem de erro")
    void verificaLoginTest02() throws ServletException, IOException {
    	//arrange
    	String requestBody = "{\"login\":\"umLogin\"}";
    	given(request.getReader())
            .willReturn(new BufferedReader(new StringReader(requestBody)));
    	given(response.getWriter()).willReturn(out);
    	
    	willThrow(new DatabaseAccessException(""))
            .given(usuarioService).verificaSeLoginExiste(anyString());
    	
    	//act
		command.executa(request, response);

    	//assert
    	verify(response).setStatus(statusCodeCaptor.capture());
    	Integer statusCode = statusCodeCaptor.getValue();
    	
    	verify(out).print(stringCaptor.capture());
    	JsonObject jsonResponse = 
            JsonParser.parseString(stringCaptor.getValue()).getAsJsonObject();
    	
    	assertTrue(jsonResponse.has("error"));
    	assertEquals(statusCode, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}