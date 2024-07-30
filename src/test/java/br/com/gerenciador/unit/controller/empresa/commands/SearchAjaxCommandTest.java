package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

import br.com.gerenciador.controller.commands.empresa.SearchAjaxCommand;
import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.service.EmpresaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class SearchAjaxCommandTest {

    @InjectMocks
    private SearchAjaxCommand command;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private EmpresaService empresaService;
    @Mock
    private PrintWriter out;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    

    @Test
    @DisplayName("Deve retornar um json array contendo empresas")
    void searchAjaxTest01() throws ServletException, IOException {
    	//arrange
    	List<EmpresaBaseDTO> listaEmpresas = List.of(
            new EmpresaBaseDTO("nome-1", "15/01/2024"),
            new EmpresaBaseDTO("nome-2", "16/01/2024")
        );
    	
    	given(request.getParameter("nomeEmpresa")).willReturn("nome");
    	given(request.getParameter("page")).willReturn("1");
    	given(request.getParameter("size")).willReturn("5");
    	
    	given(empresaService.getEmpresasByNamePaged(any(), anyString()))
			.willReturn(listaEmpresas);
    	given(response.getWriter()).willReturn(out);
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue())
			.getAsJsonObject();
    	
    	var empresasArray = resposta.get("empresas").getAsJsonArray();
    	assertTrue(empresasArray.isJsonArray() && empresasArray.size() == 2);
    	assertTrue(resposta.has("empresas") && resposta.has("pagination"));
    }
    @Test
    @DisplayName("Deve retornar um json com mensagem de erro")
    void searchAjaxTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getParameter("nomeEmpresa")).willReturn("nome");
    	given(request.getParameter("page")).willReturn("1");
    	given(request.getParameter("size")).willReturn("5");
    	
    	willThrow(new DatabaseAccessException("test"))
			.given(empresaService).getEmpresasByNamePaged(any(), any());
    	given(response.getWriter()).willReturn(out);
    	
    	//act
    	command.executa(request, response);
    	
    	//assert
    	then(out).should().print(stringCaptor.capture());
    	JsonObject resposta = JsonParser.parseString(stringCaptor.getValue())
			.getAsJsonObject();
    	
    	assertTrue(resposta.has("message"));
    	assertEquals(resposta.get("message").getAsString(), "test");
    }
}