package graphical.views;

import java.util.Set;

import hibernate.dao.JPAConnection;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class SwitchScene {

	private static Stage stage;

	private static LoginWindow lgWin;
	private static Scene scLogin;

	private static RegisterWindow rgWin;
	private static Scene scRegister;

	private static ChatWindow chWin;
	private static Scene scChat;

	public SwitchScene(Stage stage) {
		
		SwitchScene.stage = stage;
		
		stage.setOnCloseRequest(e -> {
			// TODO
			e.consume();
			Platform.runLater(() -> {
				if (AlertBox.showAndWait(AlertType.CONFIRMATION, "", "Do you exit Chat?")
						.orElse(ButtonType.CANCEL) == ButtonType.OK) {
					JPAConnection.closeJPAConnection();
					closeAllProcesses();
					stage.close();
				}
			});

		});
	}

	 /**
     * Interrupts all currently running threads to ensure that receive method is killed.
     */
    private void closeAllProcesses() {
        JPAConnection.closeJPAConnection();
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        threads.forEach(t ->{
            t.interrupt();
        });
    }
	
	public void goToLogin() {
		if (lgWin == null) {
			lgWin = new LoginWindow();
			lgWin.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		}
		if (scLogin == null) {
			scLogin = new Scene(lgWin);
		}
		stage.setScene(scLogin);
		// stage.centerOnScreen();
	}

	public void goToRegister() {
		if (rgWin == null) {
			rgWin = new RegisterWindow();
			rgWin.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		}
		if (scRegister == null) {
			scRegister = new Scene(rgWin);
		}
		stage.setScene(scRegister);
		// stage.centerOnScreen();
	}

	public void goToChat() {
		if (chWin == null) {
			chWin = new ChatWindow();
			chWin.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		}
		if (scChat == null) {
			scChat = new Scene(chWin);
		}
		stage.setScene(scChat);
		// stage.centerOnScreen();
	}

	public static Stage getStage() {
		return stage;
	}
}
