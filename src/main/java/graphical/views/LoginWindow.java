package graphical.views;

import graphical.functions.LoginWindowFunc;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import main.Main;

public class LoginWindow extends BorderPane {

	private GridPane gp;
	private Label lbLoginTitle, lbUser, lbPassword, lbAuthors;
	private TextField tfUser;
	private PasswordField pfPassword;
	private Button registerButton, loginButton;
	private HBox hb;
	private ColumnConstraints columnConstr1, columnConstr2, columnConstr3, columnConstr4;
	// do dopisania licznik logowan do 3 razy tak jak ponizej w przykladzie z
	// plikiem
	private int loginCounter = 0;
	private LoginWindowFunc func;


	public LoginWindow() {
		func = new LoginWindowFunc(this);
		setCenter(setGridPane());
	}

	private GridPane setGridPane() {
		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));

		lbLoginTitle = new Label("Join to cryptoChat!");
		lbLoginTitle.setId("lbLoginTitle");
		gp.add(lbLoginTitle, 2, 2, 2, 1);
		gp.setHalignment(lbLoginTitle, HPos.CENTER);

		setUpLogo();
		setUpLoginFields();
		setUpButtons();
		setUpAuthors();

		columnConstr1 = new ColumnConstraints();
		columnConstr2 = new ColumnConstraints();
		columnConstr2.setMinWidth(25);
		columnConstr3 = new ColumnConstraints();
		columnConstr4 = new ColumnConstraints();
		columnConstr4.setHalignment(HPos.RIGHT);
		gp.getColumnConstraints().addAll(columnConstr1, columnConstr2, columnConstr3, columnConstr4);

		return gp;
	}
	
	private void setUpLogo() {

		Image imageLogo = new Image(ClassLoader.getSystemResourceAsStream("chat-logo.png"));
		ImageView imageViewLogo = new ImageView(imageLogo);
		imageViewLogo.setFitHeight(161);
		imageViewLogo.setFitWidth(250);
		gp.add(imageViewLogo, 0, 1, 1, 5);

		Image imageName = new Image(ClassLoader.getSystemResourceAsStream("chat-name.png"));
		ImageView imageViewName = new ImageView(imageName);
		HBox hbImageName = new HBox(imageViewName);
		hbImageName.setAlignment(Pos.CENTER);
		gp.add(hbImageName, 0, 0, 4, 1);
	}
	
	private void setUpLoginFields() {
		lbUser = new Label("USERNAME:");
		gp.add(lbUser, 2, 3);

		tfUser = new TextField();
		gp.add(tfUser, 3, 3);

		lbPassword = new Label("PASSWORD:");
		gp.add(lbPassword, 2, 4);

		pfPassword = new PasswordField();
		gp.add(pfPassword, 3, 4);
		pfPassword.setOnAction(e -> {
			func.login();
		});
	}

	private void setUpButtons() {		
		registerButton = new Button("REGISTER");
		registerButton.setId("registerButton");
		registerButton.setOnAction(e -> {
			SwitchScene sc = Main.getSwitchScene();
			sc.goToRegister();
		});

		loginButton = new Button("LOGIN");
		loginButton.setId("loginButton");
		loginButton.setOnAction(e -> func.login());
		
		hb = new HBox(registerButton, loginButton);
		hb.setAlignment(Pos.CENTER);
		hb.setSpacing(25);
		gp.add(hb, 2, 5, 2, 1);
		
	}
	
	private void setUpAuthors() {		
		lbAuthors = new Label("project on Politechnika Warszawska "
				+ "\"Java EE - produkcja oprogramowania\"\n"
				+ "made by Jaros³aw Biernacki & Marcin Leœniewski\n"
				+ "June 2018");
		lbAuthors.setId("lbAuthors");
		gp.add(lbAuthors, 0, 7, 4, 1);
	}
	
	public String getUsername() {
		return tfUser.getText();
	}
	
	public String getUserPassword() {
		return pfPassword.getText();
	}
	

	
	// METODA LOGOWANIA PRZEZ PLIK
//	private void loginButton_Click() throws FileNotFoundException {
//
//		UsersDB usersDB = new UsersDB();
//		usersDB.readUsers(userTextField.getText(), pwBox.getText());
//		if (usersDB.getFlag()) {
//			Main.nickname = userTextField.getText();
//			SwitchScene.goToChat();
//		} else {
//			userTextField.setText("");
//			pwBox.setText("");
//			AlertBox.showAndWait(AlertType.ERROR, "LOGIN FAILED", "Check Your Login of Password.");
//			loginCounter++;
//			if (loginCounter == 3) {
//				AlertBox.showAndWait(AlertType.ERROR, "LOGIN FAILED 3 TIMES!", "You must leave the chat.");
//				SwitchScene.stage.close();
//			}
//		}
//	}

}
