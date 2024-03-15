package br.com.gerenciador.integration.database;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.repository.EmpresaRepository;
import br.com.gerenciador.repository.EmpresaRepositoryJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class EmpresaRepositoryJPATest {
	private EntityManager em;
	private EmpresaRepository rp;
	
	private static final Long USUARIO_ID_VALIDO = 1l;
	private static final Long USUARIO_ID_INVALIDO = 9999l;
	private static final Usuario usuario = new UsuarioBuilder().setNome("Um Nome").setLogin("umLogin").setSenha("umaSenha").build();
	private static final List<Empresa> empresasTest = List.of(
			new EmpresaBuilder().setNome("Empresa1").setData("01/12/2023").setUsuario(usuario).build(),
			new EmpresaBuilder().setNome("Empresa2").setData("01/12/2023").setUsuario(usuario).build(),
			new EmpresaBuilder().setNome("Empresa3").setData("01/12/2023").setUsuario(usuario).build(),
			new EmpresaBuilder().setNome("Empresa4").setData("01/12/2023").setUsuario(usuario).build().removeOrRestoreEmpresa(),
			new EmpresaBuilder().setNome("Empresa4").setData("01/12/2023").setUsuario(usuario).build().removeOrRestoreEmpresa(),
			new EmpresaBuilder().setNome("Empresa4").setData("01/12/2023").setUsuario(usuario).build().removeOrRestoreEmpresa()
			);
	
	private static Long QUANTIDADE_REGISTROS_TRUE = empresasTest.stream().filter(empresa -> empresa.getAtivo().equals(true)).count();
	private static Long QUANTIDADE_REGISTROS_FALSE= empresasTest.stream().filter(empresa -> empresa.getAtivo().equals(false)).count();
	
	private static final int INDEX_START = 0;
	private static final int MAX_RESULTS = 2;
	private static final boolean ATIVO_TRUE = true;
	private static final boolean ATIVO_FALSE = false;
	
	@BeforeAll
	public static void setUpEmpresasDeTeste() {
		EntityManager emSetup = JPAUtilTest.getEntityManager();
		emSetup.getTransaction().begin();
		emSetup.persist(usuario);
		empresasTest.forEach(empresa -> {
			emSetup.persist(empresa);			
		});
		emSetup.getTransaction().commit();
	}
	
	@BeforeEach
	public void beforeEach() {
		this.em = JPAUtilTest.getEntityManager();
		this.rp = new EmpresaRepositoryJPA(em);
	}
	@AfterEach
	public void afterEach() {
	}
	
	
	@Test
	void findByIdTest01() {
		//act
		Empresa empresaDb = rp.findById(1l);
		//assert
		Assertions.assertNotNull(empresaDb);
		Assertions.assertNotNull(empresaDb.getId());
		Assertions.assertNotNull(empresaDb.getNome());
		Assertions.assertNotNull(empresaDb.getDataAbertura());
		Assertions.assertNotNull(empresaDb.getAtivo());
	}
	
	@Test
	void findByIdTest02() {
		//act e assert 
		Assertions.assertThrows(NoResultException.class, () -> rp.findById(USUARIO_ID_INVALIDO));
	}

	@Test
	void findAllPagedTest() {
		//act
		List<Empresa> listaEmpresas = rp.findAllPaged(INDEX_START, MAX_RESULTS);
		//arrange
		Assertions.assertEquals(empresasTest.get(0).getNome(), listaEmpresas.get(0).getNome());
		Assertions.assertEquals(empresasTest.get(0).getDataAbertura(), listaEmpresas.get(0).getDataAbertura());
		Assertions.assertEquals(empresasTest.get(1).getNome(), listaEmpresas.get(1).getNome());
		Assertions.assertEquals(empresasTest.get(1).getDataAbertura(), listaEmpresas.get(1).getDataAbertura());

		Assertions.assertFalse(listaEmpresas.isEmpty());
		Assertions.assertEquals(listaEmpresas.size(), MAX_RESULTS);
	}
	
	@Test
	void findByUsuarioIdAndAtivoPagedTest() {
		//act
		List<Empresa> listaEmpresas = rp.findByUsuarioIdAndAtivoPaged(USUARIO_ID_VALIDO, INDEX_START, MAX_RESULTS, ATIVO_TRUE);
		//assert
		Assertions.assertEquals(listaEmpresas.size(), MAX_RESULTS);
		listaEmpresas.forEach(empresa -> {
			Assertions.assertEquals(empresa.getAtivo(), ATIVO_TRUE);
			Assertions.assertEquals(empresa.getUsuario().getId(), USUARIO_ID_VALIDO);
		});
	}
	
	@Test
	void findByNameLikePagedTest() {
		//act
		List<Empresa> empresas = rp.findByNameLikePaged("Empresa", INDEX_START, MAX_RESULTS);
		//assert
		Assertions.assertEquals(empresas.size(), MAX_RESULTS);
	}
	
	@Test
	public void countByAtivoTrueTest() {
		//act
		Long quantidadeRegistros = rp.countByAtivoTrue();
		//assert
		Assertions.assertEquals(quantidadeRegistros, QUANTIDADE_REGISTROS_TRUE);
	}
	@Test
	public void countByUsuarioAndAtivoTest() {
		//act
		Long quantidadeRegistrosTrue = rp.countByUsuarioAndAtivo(USUARIO_ID_VALIDO, ATIVO_TRUE);
		Long quantidadeRegistrosFalse = rp.countByUsuarioAndAtivo(USUARIO_ID_VALIDO, ATIVO_FALSE);
		//assert
		Assertions.assertEquals(quantidadeRegistrosTrue, QUANTIDADE_REGISTROS_TRUE);
		Assertions.assertEquals(quantidadeRegistrosFalse, QUANTIDADE_REGISTROS_FALSE);
	}
	@Test
	public void countByParamSearchTest() {
		//act
		Long quantidadeRegistros = rp.countByParamSearch("Empresa");
		//assert
		Assertions.assertEquals(quantidadeRegistros, QUANTIDADE_REGISTROS_TRUE);
	}
}