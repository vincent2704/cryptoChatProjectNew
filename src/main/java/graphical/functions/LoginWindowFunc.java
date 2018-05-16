package graphical.functions;

import graphical.views.LoginWindow;
import hibernate.dao.UserDAO;
import sockets.Client;

public class LoginWindowFunc {
	
	private LoginWindow view;
	private UserDAO udao = new UserDAO();
	
	public LoginWindowFunc(LoginWindow loginWindow) {
		view = loginWindow;
	}

	/**
	 * Involves database verifying method as well as sending information about new
	 * user in chat to others via sockets.
	 */
	public void login() {
		udao.login(view.getUsername(), view.getUserPassword());
		Client.userLoggedIn();
	}
	
}
