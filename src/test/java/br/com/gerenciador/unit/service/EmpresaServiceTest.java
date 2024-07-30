package br.com.gerenciador.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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
import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.modelo.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.modelo.dto.empresa.request.NovaEmpresaDTO;
import br.com.gerenciador.modelo.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.pagination.PaginationBuilder;
import br.com.gerenciador.repository.EmpresaRepository;
import br.com.gerenciador.service.EmpresaService;
import br.com.gerenciador.util.LocalDateUtil;
import br.com.gerenciador.validation.ValidatorUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

	@InjectMocks
	private EmpresaService empresaService;
	
	@Mock
	private List<Empresa> empresas;
	@Mock
	private Usuario usuario;
	@Mock
	private Usuario usuario2;
	@Captor
	private ArgumentCaptor<Empresa> empresaCaptor;

	@Mock
	private ValidatorUtil validator;
	@Mock
	private EmpresaRepository repository;
	@Mock
	private EntityTransaction transaction;
	@Mock
	private EntityManager em;
	@Mock
	private TypedQuery<Empresa> typedQuery;
	@Mock
	private Query query;
	
	private static final Long ID_VALIDO = 1l;
	private static final String NOME_VALIDO = "Um Nome Para Empresa";
	private static final String DATA_VALIDA = "01/01/2021";
	
	private static final Long ID_INVALIDO = null;
    private static final String NOME_INVALIDO = "1@_";
    private static final String DATA_INVALIDA = "01-13-2100";
    
	private final Pagination pg = new PaginationBuilder()
		.setPageNumber("1")
		.setPageSize("5")
		.setTotalPages(1L)
		.build();

	@BeforeEach
	void setup() {
		this.empresaService = new EmpresaService(repository, new ValidatorUtil());
	}
    
	
	@Test
	@DisplayName("Deveria cadastrar Empresa")
	void cadastraEmpresaTest01() {
	    //arrange
	    NovaEmpresaDTO dto = new NovaEmpresaDTO(
			new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA), usuario);

	    //act
	    empresaService.cadastraEmpresa(dto);

	    //assert
	    then(repository).should().persist(empresaCaptor.capture());

	    var empresaCapturada = empresaCaptor.getValue();
	    assertEquals(empresaCapturada.getNome(), dto.base().nome());
	    assertEquals(empresaCapturada.getDataAbertura(), LocalDateUtil.formatStringToLocalDate(dto.base().data()));
	    assertEquals(empresaCapturada.getUsuario(), dto.usuario());
	}
	@Test
	@DisplayName("Nao deveria cadastrar Empresa quando os campos 'nome' e 'data' estiverem errados")
	void cadastraEmpresaTest02() {
	    //arrange
	    NovaEmpresaDTO dto = new NovaEmpresaDTO(
			new EmpresaBaseDTO(NOME_INVALIDO, DATA_INVALIDA), usuario);

	    //act and Assert
	    then(em).shouldHaveNoInteractions();
	    then(transaction).shouldHaveNoInteractions();
	    assertThrows(FormValidationException.class, () -> empresaService.cadastraEmpresa(dto));
	}

	@Test
	@DisplayName("Deveria alterar dados de Empresa")
	void alteraDadosEmpresaTest01() {
	    //arrange
	    AlteraEmpresaDTO dto = new AlteraEmpresaDTO(
			ID_VALIDO, new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA));

		Empresa empresa = new Empresa(
			new NovaEmpresaDTO(
				new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA), usuario));
		when(repository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(empresa);

	    //act
	    empresaService.alteraDadosEmpresa(dto, usuario);

	    //assert
	    then(repository).should().update(empresaCaptor.capture());
	    var empresaCapturada = empresaCaptor.getValue();
	    
	    assertEquals(empresaCapturada.getAtivo(), empresa.getAtivo());
	    assertEquals(empresaCapturada.getNome(), empresa.getNome());
	    assertEquals(empresaCapturada.getDataAbertura(), empresa.getDataAbertura());
	    assertEquals(empresaCapturada.getUsuario(), empresa.getUsuario());
	}
	@Test
	@DisplayName("Nao deveria alterar dados de Empresa quando os campos 'nome' e 'data' estiverem errados")
	void alteraDadosEmpresaTest02() {
	    //arrange
	    AlteraEmpresaDTO dto = new AlteraEmpresaDTO(
			ID_INVALIDO, new EmpresaBaseDTO(NOME_INVALIDO, DATA_INVALIDA));

	    //act and Assert
	    
        verify(repository, times(0)).update(any());
	    assertThrows(FormValidationException.class, () -> empresaService.alteraDadosEmpresa(dto, usuario));
	}

	@Test
	@DisplayName("Deveria consultar empresas")
	void getEmpresasNamePagedTest() {
		// Arrange
		String nomeEmpresa = "empresa";
		when(repository.findByNameLikePaged(anyString(), anyInt(), any()))
			.thenReturn(empresas);
		
		// Act
		List<EmpresaBaseDTO> empresasMock = 
			empresaService.getEmpresasByNamePaged(this.pg, nomeEmpresa);
		
		// Assert
		assertEquals(empresasMock.size(), empresas.size());
		assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}
	@Test
	@DisplayName("Deveria consultar empresas")
	void getEmpresasPagedTest() {
		// Arrange
		when(repository.findAllPaged(anyInt(), any())).thenReturn(empresas);
		
		// Act
		List<EmpresaBaseDTO> empresasMock = 
			empresaService.getEmpresasPaged(this.pg);
		
		// Assert
		assertEquals(empresasMock.size(), empresas.size());
		assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}

	@Test
	@DisplayName("Deveria consultar empresas do Usuario")
	void getEmpresasUsuarioPagedTest() {
		// Arrange
		when(repository.findByUsuarioIdAndAtivoPaged(anyLong(), any(), any(), anyBoolean()))
			.thenReturn(empresas);

		// Act
		List<ListaEmpresasUsuarioDTO> empresasMock = 
			empresaService.getEmpresasAtivoUsuarioPaged(this.pg, 1l, true);
		
		// Assert
		assertEquals(empresasMock.size(), empresas.size());
		assertEquals(empresas.stream().map(ListaEmpresasUsuarioDTO::new).toList(), empresasMock);
	}
	
	@Test
	void getCountEmpresasTest01() {
		// Arrange
		var QUANTIDADE_ESPERADA = 3L;
		when(repository.countByAtivoTrue()).thenReturn(QUANTIDADE_ESPERADA);

		
		// Act
		Long resultado = empresaService.getCountEmpresas();
		
		// Assert
		assertEquals(QUANTIDADE_ESPERADA, resultado);
	}
	@Test
	void getCountEmpresasUsuarioAtivoTest() {
		// Arrange
		var QUANTIDADE_ESPERADA = 3L;
		when(repository.countByUsuarioAndAtivo(anyLong(), any()))
			.thenReturn(QUANTIDADE_ESPERADA);
		
		// Act
		Long resultado = empresaService.getCountEmpresasUsuarioAtivo(123l, true);
		
		// Assert
		assertEquals(QUANTIDADE_ESPERADA, resultado);
	}
	@Test
	void getCountEmpresasSearchTest01() {
		// Arrange
		var QUANTIDADE_ESPERADA = 3L;
		String nomeEmpresa = "Um nome qualquer";
		given(repository.countByParamSearch(anyString()))
			.willReturn(QUANTIDADE_ESPERADA);
		
		// Act
		Long resultado = empresaService.getCountEmpresasSearch(nomeEmpresa);
		
		// Assert
		assertEquals(QUANTIDADE_ESPERADA, resultado);
	}
	@Test
	void getCountEmpresasSearchTest02() {
		// Arrange
		var QUANTIDADE_ESPERADA = 0L;
		String nomeEmpresa = "";
		
		// Act
		Long resultado = empresaService.getCountEmpresasSearch(nomeEmpresa);
		
		// Assert
		assertEquals(QUANTIDADE_ESPERADA, resultado);
	}
	@Test
	void getCountEmpresasSearchTest03() {
		// Arrange
		var QUANTIDADE_ESPERADA = 0L;
		String nomeEmpresa = null;
		
		// Act
		Long resultado = empresaService.getCountEmpresasSearch(nomeEmpresa);
		
		// Assert
		assertEquals(QUANTIDADE_ESPERADA, resultado);
	}
}