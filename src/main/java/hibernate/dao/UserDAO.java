package hibernate.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import graphical.views.AlertBox;
import graphical.views.SwitchScene;
import hibernate.entities.User;
import javafx.scene.control.Alert.AlertType;
import main.Main;

public class UserDAO {

	private static EntityManager em;
	
	public UserDAO() {
	}
	
	/**
	 * Used to register a new user. Creates new entry in users table.
	 * @param username
	 * @param password
	 */
	public void registerUser(String username, String password) {
		em = JPAConnection.getJPAEntityManager();
		try {
			if (userExists(username)) {
				AlertBox.showAndWait(AlertType.ERROR, "User already exists!", "Enter other login data!");
				return;
			}
			em.getTransaction().begin();
			User user = new User(username, password);
			em.persist(user);
			em.getTransaction().commit();
			SwitchScene sc = Main.getSwitchScene();
			sc.goToLogin();
		}catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Uses {@link #checkCredentials(String, String)} to confirm user name and password and then switches Scene to ChatWindow.
	 * @param username
	 * @param password
	 */
	public void login(String username, String password) {
		if (checkCredentials(username, password)) {
			System.out.println("zalogowano");
			Main.nickname = username;
			if (!Main.olNames.contains(username)) {
				Main.olNames.add(username);
			}
			SwitchScene sc = Main.getSwitchScene();
			sc.goToChat();
		} else {
			AlertBox.showAndWait(AlertType.ERROR, "User not exist!", "Check login and password you entered!");
		}
	}

	/**
	 * Checks if input user name and password are matching.
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean checkCredentials(String username, String password) {
		em = JPAConnection.getJPAEntityManager();
		String jpql = "SELECT u FROM User u WHERE userName = :name AND password = :password";
		TypedQuery<User> q = em.createQuery(jpql, User.class);
		q.setParameter("name", username);
		q.setParameter("password", password);
		try {
			q.getSingleResult();
		} catch (NoResultException exc) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if user exists, as a part or registration process.
	 * @param name
	 * @return
	 */
	public static boolean userExists(String name) {
		// string is required to create NoResultException
		String str = null;
		em = JPAConnection.getJPAEntityManager();
		String jpql = "SELECT u.userName FROM User u WHERE userName = :name";
		TypedQuery<String> q = em.createQuery(jpql, String.class);
		q.setParameter("name", name);
		try {
			str = q.getSingleResult();
		} catch (NoResultException exc) {
			return false;
		}
		return true;
	}
	
}
