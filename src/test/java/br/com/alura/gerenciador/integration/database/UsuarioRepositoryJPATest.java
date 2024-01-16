package br.com.alura.gerenciador.integration.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;
import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.repository.UsuarioRepository;
import br.com.alura.gerenciador.repository.UsuarioRepositoryJPA;
import jakarta.persistence.EntityManager;

public class UsuarioRepositoryJPATest {
	private EntityManager em;
	private UsuarioRepository rp;
	
	public UsuarioRepositoryJPATest() {
		this.em = JPAUtilTest.getEntityManager();
		this.rp = new UsuarioRepositoryJPA(em);
	}
	
	@BeforeAll
	public static void beforeAll() {
		EntityManager emSetup = JPAUtilTest.getEntityManager();
		emSetup.getTransaction().begin();
		Usuario usuario = new Usuario(new NovoUsuarioDTO("Um Nome", "umLogin", "umaSenha", "umaSenha"));
		emSetup.persist(usuario);
		emSetup.getTransaction().commit();
	}
	
	@BeforeEach
	public void beforeEach() {
		em.getTransaction().begin();
	}
	@AfterEach
	public void afterEach() {
		em.getTransaction().rollback();
	}
	@AfterAll
	public static void setUpAfter() {
		EntityManager emSetup = JPAUtilTest.getEntityManager();
		emSetup.getTransaction().begin();
		emSetup.createNativeQuery("DELETE FROM empresas; DELETE FROM usuarios").executeUpdate();
		emSetup.createNativeQuery("ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 1").executeUpdate();
		emSetup.getTransaction().commit();
	}
	
	
	@Test
	void findByLoginTest01() {
		Usuario usuarioDB = rp.findByLogin("umLogin");
		Assertions.assertNotNull(usuarioDB);
		Assertions.assertNotNull(usuarioDB.getNome());
		Assertions.assertNotNull(usuarioDB.getLogin());
		Assertions.assertNotNull(usuarioDB.getSenha());
	}
	@Test
	void findByLoginTest02() {
		Usuario usuario = rp.findByLogin("outroLogin");
		Assertions.assertNull(usuario);
	}
	
	@Test
	void existsByLoginTest01() {
		boolean resposta = rp.existsByLogin("umLogin");
		Assertions.assertTrue(resposta);
	}
	@Test
	void existsByLoginTest02() {
		boolean resposta = rp.existsByLogin("outroLogin");
		Assertions.assertFalse(resposta);
	}
}