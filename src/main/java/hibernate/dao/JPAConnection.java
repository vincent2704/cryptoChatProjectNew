package hibernate.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import graphical.views.AlertBox;
import graphical.views.SwitchScene;
import javafx.scene.control.Alert.AlertType;

public class JPAConnection {

	private static EntityManagerFactory emf;
	private static EntityManager em;
	
	/**
	 * This method is public only for the JPAConnectionTest purposes.
	 * Every method should use {@link #getJPAEntityManager()} as it calls
	 * this method under the hood.
	 * @return connection success
	 */
	public static boolean createJPAConnection() {
		try {
			emf = Persistence.createEntityManagerFactory("kryptoczat");
			em = emf.createEntityManager();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Used to create database transactions.
	 * @return EntityManager for database transactions
	 */
	public static EntityManager getJPAEntityManager() {
		return em;
	}
	
	/**
	 * Closes connection at the program exit.
	 */
	public static void closeJPAConnection() {
		if (emf.isOpen())
			emf.close();
		if (em.isOpen())
			em.close();
	}

}