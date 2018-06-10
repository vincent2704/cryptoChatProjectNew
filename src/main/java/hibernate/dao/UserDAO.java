package hibernate.dao;

import java.net.ConnectException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import graphical.views.AlertBox;
import graphical.views.SwitchScene;
import hibernate.entities.User;
import javafx.scene.control.Alert.AlertType;
import main.Main;
import sockets.Client;

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
	 * method to check whether a given user can log into the chat window
	 * if it can be switched to the chat window and its username is sent to another user to the chat window with available users
	 * if it can not, the corresponding alert will be displayed with an error
	 * @author Marcin Lesniewski
	 * @param username name of the user who logs in
	 * @param password password of the user who logs in
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
			Client.send("$1" + Main.nickname);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Client.send("$3");
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
