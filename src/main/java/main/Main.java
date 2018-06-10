package main;

import graphical.views.SwitchScene;
import hibernate.dao.JPAConnection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	@Override
	public void start(Stage primaryStage) throws Exception {

		olNames = FXCollections.observableArrayList();
		JPAConnection.createJPAConnection();
		stage = primaryStage;
		stage.setTitle("CryptoChat");
		sc = new SwitchScene(stage);
		sc.goToLogin();
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public static SwitchScene getSwitchScene() {
		return sc;
	}

}
