package main;

import graphical.views.SwitchScene;
import hibernate.dao.JPAConnection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Main extends Application {

	static Stage stage;
	public static String nickname = "";
	public static ObservableList olNames;
	private static SwitchScene sc;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//wdkygdwkwdaa

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
