package br.com.alura.gerenciador.service;

import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.repository.UsuarioRepository;
import br.com.alura.gerenciador.validation.FormValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@InjectMocks
	private UsuarioService usuarioService;
	@Mock
	private Usuario usuario;
	@Captor
	private ArgumentCaptor<Usuario> usuarioCaptor;
	
	@Mock
	private UsuarioRepository repository;
	@Mock
	private EntityTransaction transaction;
	@Mock
	private EntityManager em;
	@Mock //representa a variável 'query' nos Repositorys
	private TypedQuery<Usuario> typedQuery;
	
	private static final String NOME_VALIDO = "A Nome";
	private static final String LOGIN_VALIDO = "umLogin";
	private static final String SENHA_VALIDA = "umaSenha123";
	private static final String SENHA_CONFIRMACAO_VALIDA = SENHA_VALIDA;
	
    private static final String NOME_INVALIDO = "1";
    private static final String LOGIN_INVALIDO = "ab";
    private static final String SENHA_INVALIDA = "1 34567";
    private static final String SENHA_CONFIRMACAO_INVALIDA = "12345678";
	
	
	@Test
	@DisplayName("Deveria cadastrar usuario")
	void cadastraUsuarioTest01() {
	    // Arrange
	    NovoUsuarioDTO dto = new NovoUsuarioDTO(NOME_VALIDO, LOGIN_VALIDO, SENHA_VALIDA, SENHA_CONFIRMACAO_VALIDA);
	    BDDMockito.given(em.getTransaction()).willReturn(transaction);

	    // Act
	    usuarioService.cadastraUsuario(dto);

	    // Assert
	    BDDMockito.then(em).should().persist(usuarioCaptor.capture());
	    BDDMockito.then(transaction).should().commit();
	    
	    var empresaCapturada = usuarioCaptor.getValue();
	    Assertions.assertEquals(empresaCapturada.getNome(), dto.nome());
	    Assertions.assertEquals(empresaCapturada.getLogin(), dto.login());
	}

	@Test
	@DisplayName("Não deveria cadastrar usuario com os atributos inválidos")
	void cadastraUsuarioTest02() {
	    // Arrange
	    NovoUsuarioDTO dto = new NovoUsuarioDTO(NOME_INVALIDO, LOGIN_INVALIDO, SENHA_INVALIDA, SENHA_CONFIRMACAO_INVALIDA);
	    
	    // Act and Assert
	    BDDMockito.then(em).shouldHaveNoInteractions();
	    BDDMockito.then(transaction).shouldHaveNoInteractions();
	    Assertions.assertThrows(FormValidationException.class, () -> usuarioService.cadastraUsuario(dto));
	}

	@Test
	@DisplayName("Deveria retornar um Usuario")
	void getUsuarioPorLoginTest01() {
	    // Arrange
	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Usuario.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getSingleResult()).willReturn(usuario);

	    // Act
	    var usuarioMock = usuarioService.buscaUsuarioPorLogin(LOGIN_VALIDO);
	    
	    // Assert
	    BDDMockito.then(typedQuery).should().getSingleResult();
	    Assertions.assertInstanceOf(Usuario.class, usuarioMock);
	}

}
