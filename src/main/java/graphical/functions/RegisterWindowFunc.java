package graphical.functions;

import graphical.views.RegisterWindow;
import hibernate.dao.UserDAO;

public class RegisterWindowFunc {

	private RegisterWindow view;
	private UserDAO udao = new UserDAO();
	
	public RegisterWindowFunc(RegisterWindow registerWindow) {
		view = registerWindow;
	}
	
	public void registerUser() {
		udao.registerUser(view.getUsername(), view.getPassword());
	}
	
}
