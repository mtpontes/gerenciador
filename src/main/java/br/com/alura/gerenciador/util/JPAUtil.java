package main.java.br.com.alura.gerenciador.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

	//essa classe é a mesma coisa que uma ConnectionFactory
	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("gerenciador");
	//static final para que esse EntityManager seja sempre o mesmo, para que toda vez que essa classe
	//for chamadas não crie outros EntityManager
	
	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}
	
}