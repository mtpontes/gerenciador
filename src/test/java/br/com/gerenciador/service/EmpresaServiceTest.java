package br.com.gerenciador.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

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

import br.com.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.dto.empresa.request.AlteraEmpresaDTO;
import br.com.gerenciador.dto.empresa.request.NovaEmpresaDTO;
import br.com.gerenciador.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.gerenciador.exception.FormValidationException;
import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.repository.EmpresaRepository;
import br.com.gerenciador.util.LocalDateUtil;
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
    
    private static final String PARAM_ID = "id";
    
	@Test
	@DisplayName("Deveria cadastrar Empresa")
	void cadastraEmpresaTest01() {
	    //arrange
	    NovaEmpresaDTO dto = new NovaEmpresaDTO(new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA), usuario);
	    BDDMockito.given(em.getTransaction()).willReturn(transaction);

	    //act
	    empresaService.cadastraEmpresa(dto);

	    //assert
	    BDDMockito.then(em).should().persist(empresaCaptor.capture());
	    BDDMockito.then(transaction).should().commit();

	    var empresaCapturada = empresaCaptor.getValue();
	    Assertions.assertEquals(empresaCapturada.getNome(), dto.base().nome());
	    Assertions.assertEquals(empresaCapturada.getDataAbertura(), LocalDateUtil.formatStringToLocalDate(dto.base().data()));
	    Assertions.assertEquals(empresaCapturada.getUsuario(), dto.usuario());
	}
	@Test
	@DisplayName("Nao deveria cadastrar Empresa quando os campos 'nome' e 'data' estiverem errados")
	void cadastraEmpresaTest02() {
	    //arrange
	    NovaEmpresaDTO dto = new NovaEmpresaDTO(new EmpresaBaseDTO(NOME_INVALIDO, DATA_INVALIDA), usuario);

	    //act and Assert
	    BDDMockito.then(em).shouldHaveNoInteractions();
	    BDDMockito.then(transaction).shouldHaveNoInteractions();
	    Assertions.assertThrows(FormValidationException.class, () -> empresaService.cadastraEmpresa(dto));
	}

	@Test
	@DisplayName("Deveria alterar dados de Empresa")
	void alteraDadosEmpresaTest01() {
	    //arrange
	    AlteraEmpresaDTO dto = new AlteraEmpresaDTO(ID_VALIDO, new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA));
	    Empresa empresa = new Empresa(new NovaEmpresaDTO(new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA), usuario));

	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.setParameter(PARAM_ID, ID_VALIDO)).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getSingleResult()).willReturn(empresa);
	    BDDMockito.given(em.getTransaction()).willReturn((transaction));

	    //act
	    empresaService.alteraDadosEmpresa(dto, usuario);

	    //assert
	    BDDMockito.then(em).should().merge(empresaCaptor.capture());
	    var empresaCapturada = empresaCaptor.getValue();
	    
	    Assertions.assertEquals(empresaCapturada.getAtivo(), empresa.getAtivo());
	    Assertions.assertEquals(empresaCapturada.getNome(), empresa.getNome());
	    Assertions.assertEquals(empresaCapturada.getDataAbertura(), empresa.getDataAbertura());
	    Assertions.assertEquals(empresaCapturada.getUsuario(), empresa.getUsuario());
	    BDDMockito.then(em.merge(any()));
	}
	@Test
	@DisplayName("Nao deveria alterar dados de Empresa quando os campos 'nome' e 'data' estiverem errados")
	void alteraDadosEmpresaTest02() {
	    //arrange
	    AlteraEmpresaDTO dto = new AlteraEmpresaDTO(ID_INVALIDO, new EmpresaBaseDTO(NOME_INVALIDO, DATA_INVALIDA));

	    //act and Assert
	    BDDMockito.then(em).shouldHaveNoInteractions();
	    BDDMockito.then(transaction).shouldHaveNoInteractions();
	    Assertions.assertThrows(FormValidationException.class, () -> empresaService.alteraDadosEmpresa(dto, usuario));
	}
	@Test
	@DisplayName("Nao deveria alterar dados de Empresa quando os usuarios forem diferentes")
	void alteraDadosEmpresaTest03() {
		//arrange
	    AlteraEmpresaDTO dto = new AlteraEmpresaDTO(
	    		ID_VALIDO, 
	    		new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA)
	    		);
	    
	    Empresa empresa = new Empresa(
	    		new NovaEmpresaDTO(new EmpresaBaseDTO(NOME_VALIDO, DATA_VALIDA), usuario2)
	    		);

	    BDDMockito.given(usuario.getId()).willReturn(ID_VALIDO);
	    BDDMockito.given(usuario2.getId()).willReturn((ID_VALIDO+1));
	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.setParameter(PARAM_ID, ID_VALIDO)).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getSingleResult()).willReturn(empresa);

		//assert
		Assertions.assertThrows(FormValidationException.class, () -> empresaService.alteraDadosEmpresa(dto, usuario));
	}
	
	//criar o teste para arquiva empresa

	@Test
	@DisplayName("Deveria consultar empresas pelo nome")
	void pesquisaEmpresasTest() {
		// Arrange
		BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
		BDDMockito.given(typedQuery.setParameter(anyString(), anyString())).willReturn(typedQuery);
		BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);
		
		// Act
		List<EmpresaBaseDTO> empresasMock = empresaService.pesquisaEmpresas("empresa");
		
		// Assert
		Assertions.assertEquals(empresasMock.size(), empresas.size());
		Assertions.assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}
	@Test
	@DisplayName("Deveria consultar empresas de forma paginada")
	void getEmpresasByNamePaged() {
		// Arrange
		BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
		BDDMockito.given(typedQuery.setParameter(anyString(), anyString())).willReturn(typedQuery);
		BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);
		
		// Act
		List<EmpresaBaseDTO> empresasMock = empresaService.getEmpresasByNamePaged("empresa", 0, 2);
		
		// Assert
		Assertions.assertEquals(empresasMock.size(), empresas.size());
		Assertions.assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}
	
	@Test
	@DisplayName("Deveria consultar empresas")
	void consultaEmpresasTest() {
		// Arrange
	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);

	    // Act
	    List<EmpresaBaseDTO> empresasMock = empresaService.consultaEmpresas();

	    // Assert
		Assertions.assertEquals(empresasMock.size(), empresas.size());
	    Assertions.assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}
	@Test
	@DisplayName("Deveria consultar empresas")
	void getEmpresasPagedTest() {
		// Arrange
		BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
		BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);
		
		// Act
		List<EmpresaBaseDTO> empresasMock = empresaService.getEmpresasPaged(anyInt(), anyInt());
		
		// Assert
		Assertions.assertEquals(empresasMock.size(), empresas.size());
		Assertions.assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}

	@Test
	@DisplayName("Deveria consultar empresas do Usuario")
	void consultaEmpresasUsuarioTest() {
	    // Arrange
	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.setParameter(PARAM_ID, ID_VALIDO)).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);

	    // Act
	    List<ListaEmpresasUsuarioDTO> empresasMock = empresaService.consultaEmpresasUsuario(1l);

	    // Assert
		Assertions.assertEquals(empresasMock.size(), empresas.size());
	    Assertions.assertEquals(empresas.stream().map(ListaEmpresasUsuarioDTO::new).toList(), empresasMock);
	}
	@Test
	@DisplayName("Deveria consultar empresas do Usuario")
	void getEmpresasUsuarioPagedTest() {
		// Arrange
		BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
		BDDMockito.given(typedQuery.setParameter(PARAM_ID, ID_VALIDO)).willReturn(typedQuery);
		BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);
		
		// Act
		List<ListaEmpresasUsuarioDTO> empresasMock = empresaService.getEmpresasAtivoUsuarioPaged(1l, 0, 0, true);
		
		// Assert
		Assertions.assertEquals(empresasMock.size(), empresas.size());
		Assertions.assertEquals(empresas.stream().map(ListaEmpresasUsuarioDTO::new).toList(), empresasMock);
	}
	
	@Test
	void getCountEmpresasTest01() {
		// Arrange
		BDDMockito.given(em.createQuery(anyString())).willReturn(query);
		BDDMockito.given(query.getSingleResult()).willReturn(3L);
		
		// Act
		Long quantidade = empresaService.getCountEmpresas();
		
		// Assert
		Assertions.assertEquals(quantidade, 3L);
	}
	@Test
	void getCountEmpresasTest02() {
		// Arrange
		BDDMockito.given(em.createQuery(anyString())).willReturn(query);
		BDDMockito.given(query.getSingleResult()).willReturn(3L);
		
		// Act
		Long quantidade = empresaService.getCountEmpresas();
		
		// Assert
		Assertions.assertEquals(quantidade, 3L);
	}
	@Test
	void getCountEmpresasUsuarioAtivoTest() {
		// Arrange
		BDDMockito.given(em.createQuery(Mockito.anyString())).willReturn(query);
		BDDMockito.given(query.setParameter("id", 123L)).willReturn(query);
		BDDMockito.given(query.setParameter("ativo", true)).willReturn(query);
		BDDMockito.given(query.getSingleResult()).willReturn(3L);
		
		// Act
		Long quantidade = empresaService.getCountEmpresasUsuarioAtivo(123l, true);
		
		// Assert
		Assertions.assertEquals(quantidade, 3L);
	}
	@Test
	void getCountEmpresasSearchTest() {
		// Arrange
		String nomeEmpresa = "Um nome qualquer";
		BDDMockito.given(em.createQuery(anyString())).willReturn(query);
		BDDMockito.given(query.setParameter(anyString(), eq("%" + nomeEmpresa + "%"))).willReturn(query);
		BDDMockito.given(query.getSingleResult()).willReturn(3L);
		
		// Act
		Long quantidade = empresaService.getCountEmpresasSearch(nomeEmpresa);
		
		// Assert
		Assertions.assertEquals(quantidade, 3L);
	}
}



