package main;

import graphical.views.AlertBox;
import graphical.views.SwitchScene;
import hibernate.dao.JPAConnection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {

	static Stage stage;
	/**
	 * static field for the username of the user currently using the chat
	 * @author Marcin Lesniewski
	 */
	public static String nickname = "";
	/**
	 * static field that specifies the list of users currently using chat
	 * @author Marcin Lesniewski
	 */
	public static ObservableList olNames;
	private static SwitchScene sc;

	/**
	 * the main method of program initialization
	 * @author Marcin Lesniewski
	 * @author Jaroslaw Biernacki
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		olNames = FXCollections.observableArrayList();
		JPAConnection.createJPAConnection();
		stage = primaryStage;
		stage.setTitle("CryptoChat");
		sc = new SwitchScene(stage);
		sc.goToLogin();
		stage.show();
		if (!JPAConnection.createJPAConnection()) {
			AlertBox.showAndWait(AlertType.ERROR, "DATABASE ERROR!", "The connection to the database failed");
			SwitchScene.getStage().close();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	public static SwitchScene getSwitchScene() {
		return sc;
	}
	
	/**
	 * "chat-logo.png" and "chat-name.png"
	 * pictures that are the main graphics in the program
	 * @author Marcin Lesniewski
	 */
	
	/**
	 * "theme1.css" and "theme2.css" and "theme3.css" and "theme4.css"
	 * css files used in the program to change the color theme of the windows
	 * @author Marcin Lesniewski
	 */

}
