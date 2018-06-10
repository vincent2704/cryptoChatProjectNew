package graphical.views;

import graphical.functions.RegisterWindowFunc;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import main.Main;

/**
 * class representing the application register window
 */
public class RegisterWindow extends BorderPane {

	private GridPane gp;
	private Label lbRegisterTitle, lbUser, lbPassword;
	private TextField tfUser;
	private PasswordField pfPassword;
	private Button createUserButton, btnBack;
	private RegisterWindowFunc func;

	public RegisterWindow() {
		func = new RegisterWindowFunc(this);
		setTop(SwitchScene.setMenuBar());
		setCenter(setGridPane());
	}

	/**
	 * method creating the application register window
	 * @author Marcin Lesniewski
	 * @return gridPane application register window
	 */
	private GridPane setGridPane() {
		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));
		// gp.setGridLinesVisible(true);
		Image imageName = new Image(ClassLoader.getSystemResourceAsStream("chat-name.png"));
		ImageView imageViewName = new ImageView(imageName);
		imageViewName.setFitHeight(47);
		imageViewName.setFitWidth(250);
		HBox hbImageName = new HBox(imageViewName);
		hbImageName.setAlignment(Pos.CENTER);
		gp.add(hbImageName, 0, 0, 2, 1);
		lbRegisterTitle = new Label("Register account now!");
		lbRegisterTitle.setId("lbRegisterTitle");
		gp.add(lbRegisterTitle, 0, 1, 2, 1);
		gp.setHalignment(lbRegisterTitle, HPos.CENTER);
		setUpRegisterFields();
		createUserButton = new Button("CREATE USER");
		createUserButton.setId("createUserButton");
		gp.add(createUserButton, 1, 4);
		gp.setHalignment(createUserButton, HPos.CENTER);	
		//stara wersja metody pod JDBC
		//createUserButton.setOnAction(e -> func.createUser());
		//wersja Hibernate
		createUserButton.setOnAction(e -> func.registerUser());
		btnBack.setOnAction(e -> {
			SwitchScene sc = Main.getSwitchScene();
			sc.goToLogin();
		});
		return gp;
	}

	/**
	 * method that loads register fields
	 * @author Marcin Lesniewski
	 */
	private void setUpRegisterFields() {
		lbUser = new Label("USERNAME:");
		gp.add(lbUser, 0, 2);
		tfUser = new TextField();
		tfUser.setOnKeyTyped(e -> {
            char inputChar = e.getCharacter().charAt(0);
            if (inputChar=='+' || inputChar=='$') {
                e.consume();
            }
        });
		gp.add(tfUser, 1, 2);
		lbPassword = new Label("PASSWORD:");
		gp.add(lbPassword, 0, 3);
		pfPassword = new PasswordField();
		gp.add(pfPassword, 1, 3);	
		btnBack = new Button("back");
		btnBack.setId("btnBack");
		gp.add(btnBack, 0, 5);
	}
	
	public String getUsername() {
		return tfUser.getText();
	}
	
	public String getPassword() {
		return pfPassword.getText();
	}

}
