package br.com.alura.gerenciador.integration.database;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.repository.EmpresaRepository;
import br.com.alura.gerenciador.repository.EmpresaRepositoryJPA;
import jakarta.persistence.EntityManager;

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
	void findEmpresaByIdTest01() {
		//act
		Empresa empresaDb = rp.findEmpresaById(1l);
		//assert
		Assertions.assertNotNull(empresaDb);
		Assertions.assertNotNull(empresaDb.getId());
		Assertions.assertNotNull(empresaDb.getNome());
		Assertions.assertNotNull(empresaDb.getDataAbertura());
		Assertions.assertNotNull(empresaDb.getAtivo());
	}
	
	@Test
	void findEmpresaByIdTest02() {
		//act
		Empresa empresaDb = rp.findEmpresaById(USUARIO_ID_INVALIDO);
		//assert 
		Assertions.assertNull(empresaDb);
	}

	@Test
	void findEmpresasTest() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresas();
		//arrange
		Assertions.assertFalse(listaEmpresas.isEmpty());
		Assertions.assertEquals(empresasTest.get(0).getNome(), listaEmpresas.get(0).getNome());
		Assertions.assertEquals(empresasTest.get(0).getDataAbertura(), listaEmpresas.get(0).getDataAbertura());
		Assertions.assertEquals(empresasTest.get(1).getNome(), listaEmpresas.get(1).getNome());
		Assertions.assertEquals(empresasTest.get(1).getDataAbertura(), listaEmpresas.get(1).getDataAbertura());
	}
	
	@Test
	void findEmpresasPaged() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresasPaged(INDEX_START, MAX_RESULTS);
		//arrange
		Assertions.assertEquals(empresasTest.get(0).getNome(), listaEmpresas.get(0).getNome());
		Assertions.assertEquals(empresasTest.get(0).getDataAbertura(), listaEmpresas.get(0).getDataAbertura());
		Assertions.assertEquals(empresasTest.get(1).getNome(), listaEmpresas.get(1).getNome());
		Assertions.assertEquals(empresasTest.get(1).getDataAbertura(), listaEmpresas.get(1).getDataAbertura());

		Assertions.assertFalse(listaEmpresas.isEmpty());
		Assertions.assertEquals(listaEmpresas.size(), MAX_RESULTS);
	}
	
	@Test
	void findEmpresasByUsuarioIdTest01() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresasByUsuarioId(USUARIO_ID_VALIDO);
		//assert
		listaEmpresas.forEach(empresa -> Assertions.assertEquals(empresa.getUsuario().getId(), USUARIO_ID_VALIDO));
	}
	@Test
	void findEmpresasByUsuarioIdTest02() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresasByUsuarioId(USUARIO_ID_INVALIDO);
		//assert
		Assertions.assertTrue(listaEmpresas.isEmpty());
	}
	@Test
	void findEmpresasPagedByUsuarioIdAndAtivo() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresasPagedByUsuarioIdAndAtivo(USUARIO_ID_VALIDO, INDEX_START, MAX_RESULTS, ATIVO_TRUE);
		//assert
		Assertions.assertEquals(listaEmpresas.size(), MAX_RESULTS);
		listaEmpresas.forEach(empresa -> {
			Assertions.assertEquals(empresa.getAtivo(), ATIVO_TRUE);
			Assertions.assertEquals(empresa.getUsuario().getId(), USUARIO_ID_VALIDO);
		});
	}
	
	@Test
	void searchEmpresasByNameLike() {
		//act
		List<Empresa> empresas = rp.searchEmpresasByNameLike("Empresa");
		//assert
		Assertions.assertEquals(empresas.size(), QUANTIDADE_REGISTROS_TRUE);
	}
	@Test
	void searchEmpresasPagedByNameLike() {
		//act
		List<Empresa> empresas = rp.searchEmpresasPagedByNameLike("Empresa", INDEX_START, MAX_RESULTS);
		//assert
		Assertions.assertEquals(empresas.size(), MAX_RESULTS);
	}
	
	@Test
	public void countEmpresasByAtivoTrue() {
		//act
		Long quantidadeRegistros = rp.countEmpresasByAtivoTrue();
		//assert
		Assertions.assertEquals(quantidadeRegistros, QUANTIDADE_REGISTROS_TRUE);
	}
	@Test
	public void countEmpresasUsuarioByAtivo() {
		//act
		Long quantidadeRegistrosTrue = rp.countEmpresasUsuarioByAtivo(USUARIO_ID_VALIDO, ATIVO_TRUE);
		Long quantidadeRegistrosFalse = rp.countEmpresasUsuarioByAtivo(USUARIO_ID_VALIDO, ATIVO_FALSE);
		//assert
		Assertions.assertEquals(quantidadeRegistrosTrue, QUANTIDADE_REGISTROS_TRUE);
		Assertions.assertEquals(quantidadeRegistrosFalse, QUANTIDADE_REGISTROS_FALSE);
	}
	@Test
	public void countEmpresasByParamSearch() {
		//act
		Long quantidadeRegistros = rp.countEmpresasByParamSearch("Empresa");
		//assert
		Assertions.assertEquals(quantidadeRegistros, QUANTIDADE_REGISTROS_TRUE);
	}
}