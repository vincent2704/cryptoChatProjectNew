package graphical.functions;

import graphical.views.LoginWindow;
import hibernate.dao.UserDAO;
import javafx.application.Platform;
import main.Main;
import sockets.Client;

public class LoginWindowFunc {
	
	private LoginWindow view;
	private UserDAO udao = new UserDAO();
	/**
	 * static field used to store the number of login attempts
	 * @author Marcin Lesniewski
	 */
	public static int loginCounter;
	
	
	public LoginWindowFunc(LoginWindow loginWindow) {
		view = loginWindow;
		loginCounter = 0;
	}

	/**
	 * Involves database verifying method as well as sending information about new
	 * user in chat to others via sockets.
	 */
	public void login() {
		udao.login(view.getUsername(), view.getUserPassword());
		//Client.userLoggedIn();
	}
	
}
