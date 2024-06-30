package br.com.gerenciador.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.usuario.NovoUsuarioDTO;
import br.com.gerenciador.repository.UsuarioRepository;
import br.com.gerenciador.service.UsuarioService;
import br.com.gerenciador.validation.ValidatorUtil;
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
	

	@BeforeEach
	void setup() {
		this.usuarioService = new UsuarioService(repository, new ValidatorUtil());
	}
	
	@Test
	@DisplayName("Deveria cadastrar usuario")
	void cadastraUsuarioTest01() {
	    // Arrange
	    NovoUsuarioDTO dto = new NovoUsuarioDTO(NOME_VALIDO, LOGIN_VALIDO, SENHA_VALIDA, SENHA_CONFIRMACAO_VALIDA);

	    // Act
	    usuarioService.cadastraUsuario(dto);

	    // Assert
	    then(repository).should().persist(usuarioCaptor.capture());
	    var empresaCapturada = usuarioCaptor.getValue();

	    assertEquals(empresaCapturada.getNome(), dto.nome());
	    assertEquals(empresaCapturada.getLogin(), dto.login());
	}
	@Test
	@DisplayName("Não deveria cadastrar usuario com os atributos inválidos")
	void cadastraUsuarioTest02() {
	    // Arrange
	    NovoUsuarioDTO dto = new NovoUsuarioDTO(NOME_INVALIDO, LOGIN_INVALIDO, SENHA_INVALIDA, SENHA_CONFIRMACAO_INVALIDA);

	    // Act and Assert
	    then(em).shouldHaveNoInteractions();
	    then(transaction).shouldHaveNoInteractions();
	    assertThrows(FormValidationException.class, () -> usuarioService.cadastraUsuario(dto));
	}

	@Test
	@DisplayName("Deveria retornar um Usuario")
	void buscaUsuarioPorLoginTest() {
	    // Arrange
		when(repository.findByLogin(anyString())).thenReturn(usuario);

	    // Act
	    var result = usuarioService.buscaUsuarioPorLogin(LOGIN_VALIDO);
	    
	    // Assert
	    assertInstanceOf(Usuario.class, result);
	}
}