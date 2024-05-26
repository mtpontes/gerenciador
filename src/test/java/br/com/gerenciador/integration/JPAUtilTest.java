package br.com.gerenciador.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtilTest {
	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("test");
	
	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
}
}
