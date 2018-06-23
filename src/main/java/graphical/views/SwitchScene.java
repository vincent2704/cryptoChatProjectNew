package graphical.views;

import java.util.Set;

import graphical.functions.ChatWindowFunc;
import hibernate.dao.JPAConnection;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import main.Main;
import sockets.Client;

/**
 * class that allows you to switch between scenes
 */
public class SwitchScene {

	private static Stage stage;

	private static LoginWindow lgWin;
	private static Scene scLogin;

	private static RegisterWindow rgWin;
	private static Scene scRegister;

	private static ChatWindow chWin;
	private static Scene scChat;
	
	/**
	 * field for the name of the currently used color theme
	 * @author Marcin Lesniewski
	 */
	public static String stylesheetName = "theme1.css";

	/**
	 * constructor that contains the setOnCloseRequest event
	 * @author Marcin Lesniewski
	 * @author Jaroslaw Biernacki
	 * @param stage application window
	 */
	public SwitchScene(Stage stage) {

		SwitchScene.stage = stage;
		
		stage.setOnCloseRequest(e -> {
			e.consume();
			Platform.runLater(() -> {
				if (AlertBox.showAndWait(AlertType.CONFIRMATION, "", "Are you sure?")
						.orElse(ButtonType.CANCEL) == ButtonType.OK) {
					try {
						Client.send("$2" + Main.nickname);						
					}catch(NullPointerException exc) {
						stage.close();
						closeAllProcesses();
					}
					stage.close();
					closeAllProcesses();
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
	
	/**
	 * method that switches to the login window
	 * @author Marcin Lesniewski
	 * @author Jaroslaw Biernacki
	 */
    public void goToLogin() {
		if (lgWin == null) {
			lgWin = new LoginWindow();
			//lgWin.getStylesheets().add(getClass().getResource("theme1.css").toExternalForm());
		}
		if (scLogin == null) {
			scLogin = new Scene(lgWin);
		}
		lgWin.getStylesheets().clear();
		lgWin.getStylesheets().add(stylesheetName);
		stage.setScene(scLogin);
		stage.centerOnScreen();
	}

	/**
	 * method that switches to the register window
	 * @author Marcin Lesniewski
	 * @author Jaroslaw Biernacki
	 */
    public void goToRegister() {
		if (rgWin == null) {
			rgWin = new RegisterWindow();
			//rgWin.getStylesheets().add(getClass().getResource("theme1.css").toExternalForm());
		}
		if (scRegister == null) {
			scRegister = new Scene(rgWin);
		}
		rgWin.getStylesheets().clear();
		rgWin.getStylesheets().add(stylesheetName);
		stage.setScene(scRegister);
		stage.centerOnScreen();
	}

	/**
	 * method that switches to the chat window
	 * @author Marcin Lesniewski
	 * @author Jaroslaw Biernacki
	 */
    public void goToChat() {
		if (chWin == null) {
			chWin = new ChatWindow();
			//chWin.getStylesheets().add(getClass().getResource("theme1.css").toExternalForm());
		}
		if (scChat == null) {
			scChat = new Scene(chWin);
		}
		chWin.getStylesheets().clear();
		chWin.getStylesheets().add(stylesheetName);
		stage.setScene(scChat);
		stage.centerOnScreen();
	}
	
	/**
	 * static method that creates a menu bar displayed in the registration and login window
	 * @author Marcin Lesniewski
	 * @return menuBar menu bar containing various buttons displayed in the registration and login window
	 */
    public static MenuBar setMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu menuThemes = new Menu("_Theme");
		menuThemes.setMnemonicParsing(true);
		MenuItem menuTheme1 = new MenuItem("_Tango Amore");
		menuTheme1.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
		menuTheme1.setOnAction(e -> setTheme(stage.getScene(), "theme1.css"));
		MenuItem menuTheme2 = new MenuItem("_Mint Garden");
		menuTheme2.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
		menuTheme2.setOnAction(e -> setTheme(stage.getScene(), "theme2.css"));
		MenuItem menuTheme3 = new MenuItem("_Purple Midnight");
		menuTheme3.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
		menuTheme3.setOnAction(e -> setTheme(stage.getScene(), "theme3.css"));
		MenuItem menuTheme4 = new MenuItem("_Golden Ink");
		menuTheme4.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
		menuTheme4.setOnAction(e -> setTheme(stage.getScene(), "theme4.css"));
		menuThemes.getItems().addAll(menuTheme1, menuTheme2, menuTheme3, menuTheme4);
		menuBar.getMenus().add(menuThemes);
		return menuBar;
	}

	/**
	 * static method that creates a menu bar displayed in the chat window
	 * @author Marcin Lesniewski
	 * @return menu bar containing various buttons displayed in the chat window
	 */
    public static MenuBar setMenuBarChat() {
		MenuBar menuBar = new MenuBar();
		Menu menuChat = new Menu("_Chat");
		MenuItem menuClearChat = new MenuItem("_Clear");
		menuClearChat.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		menuClearChat.setOnAction(e -> ChatWindowFunc.clearChatBox());
		menuChat.getItems().add(menuClearChat);
		Menu menuThemes = new Menu("_Theme");
		menuThemes.setMnemonicParsing(true);
		MenuItem menuTheme1 = new MenuItem("_Tango Amore");
		menuTheme1.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
		menuTheme1.setOnAction(e -> setTheme(stage.getScene(), "theme1.css"));
		MenuItem menuTheme2 = new MenuItem("_Mint Garden");
		menuTheme2.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
		menuTheme2.setOnAction(e -> setTheme(stage.getScene(), "theme2.css"));
		MenuItem menuTheme3 = new MenuItem("_Purple Midnight");
		menuTheme3.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
		menuTheme3.setOnAction(e -> setTheme(stage.getScene(), "theme3.css"));
		MenuItem menuTheme4 = new MenuItem("_Golden Ink");
		menuTheme4.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
		menuTheme4.setOnAction(e -> setTheme(stage.getScene(), "theme4.css"));
		menuThemes.getItems().addAll(menuTheme1, menuTheme2, menuTheme3, menuTheme4);
		menuBar.getMenus().addAll(menuChat, menuThemes);
		return menuBar;
	}

	/**
	 * method that deletes the previous theme for a given window and sets a new one using the css file
	 * @author Marcin Lesniewski
	 * @param scene scene name
	 * @param themeName name of the theme or css file
	 */
    public static void setTheme(Scene scene, String themeName) {
		scene.getRoot().getStylesheets().clear();  
		scene.getRoot().getStylesheets().add(themeName);
		stylesheetName = themeName;
	}
	
	public static Stage getStage() {
		return stage;
	}
}
