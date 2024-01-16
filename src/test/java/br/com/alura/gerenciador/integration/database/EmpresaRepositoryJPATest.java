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
	private static final List<Empresa> listaEmpresasTest = List.of(
			new EmpresaBuilder().setNome("Empresa1").setData("01/12/2023").setUsuario(usuario).build(),
			new EmpresaBuilder().setNome("Empresa2").setData("01/12/2023").setUsuario(usuario).build()
			);
	
	@BeforeAll
	public static void setUpEmpresasDeTeste() {
		EntityManager emSetup = JPAUtilTest.getEntityManager();
		emSetup.getTransaction().begin();
		emSetup.persist(usuario);
		listaEmpresasTest.forEach(empresa -> {
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
	void findEmpresasTest01() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresas();
		//arrange
		Assertions.assertFalse(listaEmpresas.isEmpty());
		Assertions.assertEquals(listaEmpresasTest.get(0).getNome(), listaEmpresas.get(0).getNome());
		Assertions.assertEquals(listaEmpresasTest.get(0).getDataAbertura(), listaEmpresas.get(0).getDataAbertura());
		Assertions.assertEquals(listaEmpresasTest.get(1).getNome(), listaEmpresas.get(1).getNome());
		Assertions.assertEquals(listaEmpresasTest.get(1).getDataAbertura(), listaEmpresas.get(1).getDataAbertura());
	}
	
	@Test
	void findEmpresasByUsuarioIdTest01() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresasByUsuarioId(USUARIO_ID_VALIDO);
		//assert
		Assertions.assertEquals(listaEmpresas.get(0), listaEmpresas.get(0));
		Assertions.assertEquals(listaEmpresas.get(1), listaEmpresas.get(1));
	}
	
	@Test
	void findEmpresasByUsuarioIdTest02() {
		//act
		List<Empresa> listaEmpresas = rp.findEmpresasByUsuarioId(USUARIO_ID_INVALIDO);
		//assert
		Assertions.assertTrue(listaEmpresas.isEmpty());
	}
}