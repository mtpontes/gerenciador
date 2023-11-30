package br.com.alura.gerenciador.service;

import static org.mockito.ArgumentMatchers.any;
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

import br.com.alura.gerenciador.dto.empresa.AlteraEmpresaDTO;
import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.dto.empresa.ListaEmpresasUsuarioDTO;
import br.com.alura.gerenciador.dto.empresa.NovaEmpresaDTO;
import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.repository.EmpresaRepository;
import br.com.alura.gerenciador.util.LocalDateUtil;
import br.com.alura.gerenciador.validation.FormValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

	@InjectMocks
	private EmpresaService empresaService;
	
	@Mock
	private List<Empresa> empresas;
	@Mock
	private Usuario usuario;
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
	
	private static final Long ID_VALIDO = 1l;
	private static final String NOME_VALIDO = "Um Nome Para Empresa";
	private static final String DATA_VALIDA = "01/01/2021";
	
	private static final Long ID_INVALIDO = null;
    private static final String NOME_INVALIDO = "1@_";
    private static final String DATA_INVALIDA = "01-13-2100";
    
    private static final String PARAM_ID = "id";
	
	
	@Test
	@DisplayName("Deveria consultar empresas no db")
	void consultaEmpresasTest() {
		// Arrange
	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);

	    // Act
	    List<EmpresaBaseDTO> empresasMock = empresaService.consultaEmpresas();

	    // Assert
	    Assertions.assertEquals(empresas.stream().map(EmpresaBaseDTO::new).toList(), empresasMock);
	}

	@Test
	@DisplayName("Deveria consultar empresas do Usuario no db")
	void consultaEmpresasUsuarioTest() {
	    // Arrange
	    BDDMockito.given(em.createQuery(Mockito.anyString(), eq(Empresa.class))).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.setParameter(PARAM_ID, ID_VALIDO)).willReturn(typedQuery);
	    BDDMockito.given(typedQuery.getResultList()).willReturn(empresas);

	    // Act
	    List<ListaEmpresasUsuarioDTO> empresasMock = empresaService.consultaEmpresasUsuario(1l);

	    // Assert
	    Assertions.assertEquals(empresas.stream().map(ListaEmpresasUsuarioDTO::new).toList(), empresasMock);
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
	    empresaService.alteraDadosEmpresa(dto);

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
	    Assertions.assertThrows(FormValidationException.class, () -> empresaService.alteraDadosEmpresa(dto));
	}

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

}
