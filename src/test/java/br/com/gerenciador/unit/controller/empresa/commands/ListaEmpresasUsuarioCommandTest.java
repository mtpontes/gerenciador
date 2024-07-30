package br.com.gerenciador.unit.controller.empresa.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.controller.commands.empresa.ListaEmpresasUsuarioCommand;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.service.EmpresaService;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ListaEmpresasUsuarioCommandTest {

    @InjectMocks
    private ListaEmpresasUsuarioCommand command;
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
    @Mock
    private Usuario usuario;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private static final String SESSION_ATRIBUTE = "usuarioLogado";
    

    @Test
    @DisplayName("Deve encaminhar o request para página de empresas do usuário")
    void listaEmpresaUsuarioTest01() throws ServletException, IOException {
    	//arrange
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	given(usuario.getId()).willReturn(1l);
    	given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
        command.executa(request, response);
    	
    	//assert
    	then(request)
            .should(Mockito.times(5))
                .setAttribute(anyString(), any());
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(enderecoHtml.indexOf("usuarioEmpresas.jsp"));
    	assertTrue(enderecoHtml.equals("usuarioEmpresas.jsp"));
    	
    	then(rd).should().forward(request, response);
    }
    @Test
    @DisplayName("Deve encaminhar o request para a página de erro 500")
    void listaEmpresaUsuarioTest02() throws ServletException, IOException {
    	//arrange
    	given(request.getSession()).willReturn(session);
    	given(session.getAttribute(SESSION_ATRIBUTE)).willReturn(usuario);
    	given(usuario.getId()).willReturn(1l);
    	willThrow(new PersistenceException(""))
            .given(empresaService)
                .getEmpresasAtivoUsuarioPaged(any(), anyLong(), anyBoolean());
    	given(request.getRequestDispatcher(Mockito.anyString())).willReturn(rd);
    	
    	//act
        command.executa(request, response);
    	
    	//assert
    	verify(request).getRequestDispatcher(stringCaptor.capture());
    	String enderecoHtml = stringCaptor.getValue();
    	enderecoHtml = enderecoHtml.substring(
            enderecoHtml.indexOf("500.html"));
    	assertTrue(enderecoHtml.equals("500.html"));
    	
    	then(rd).should().forward(request, response);
    }
}