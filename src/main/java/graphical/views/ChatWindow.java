package graphical.views;

import java.util.Date;

import graphical.functions.ChatWindowFunc;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Main;
//import hibernate.entities.User;
import sockets.Client;

public class ChatWindow extends BorderPane {

	private MenuBar menuBar;
	private Menu menuChat;
	private MenuItem menuLoadHistory, menuClearChat;

	private GridPane gp;
	private Label lbUsers, lbChat, lbFile, lbEncrypt;
	private TextField tfMessage, tfEncrypt;
	public Button btnChooseFile;
	private Button btnSendFile;
	private Button btnBack;
	private HBox hbSendFile, buttonsEncrypt;
	public VBox vbChatBox;
	private VBox vb, areaLogo, areaFile, areaEncrypt, areaRight;
	private ScrollPane spChatBox;
	private CheckBox chbEncrypt;
	private RowConstraints rowConstr1, rowConstr2, rowConstr3;

	
	public ListView lvAvailableUsers;
	public ListView<String> lvAttachments;

	private boolean isChatBoxEmpty = true;

	private ChatWindowFunc func;
	public static Date dateOfJoinToChat;

	// StringProperty textRecu = new SimpleStringProperty();
	// public Date currentDate;
	// public Date tempDate;

	public final static int CHAT_WIDTH = 400;
	public final static int CHAT_HEIGHT = 500;

	public ChatWindow() {
		func = new ChatWindowFunc(this);
		setTop(setMenuBar());
		setCenter(setGridPane());
		func.concurrentReceive();
		dateOfJoinToChat = new Date();
	}

	private MenuBar setMenuBar() {
		menuBar = new MenuBar();
		menuChat = new Menu("_Chat");
		menuLoadHistory = new MenuItem("_Load History");
		menuLoadHistory.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		menuLoadHistory.setOnAction(e -> func.loadHistory());
		menuClearChat = new MenuItem("_Clear Chat");
		menuClearChat.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		menuClearChat.setOnAction(e -> func.clearHistoryAtChatBox());
		menuChat.getItems().addAll(menuLoadHistory, menuClearChat);
		menuBar.getMenus().addAll(menuChat);
		return menuBar;
	}

	private GridPane setGridPane() {

		gp = new GridPane();
		// gp.setGridLinesVisible(true);
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));
		gp.setMaxHeight(550);

		Image imageName = new Image(ClassLoader.getSystemResourceAsStream("chat-name.png"));
		ImageView imageViewName = new ImageView(imageName);
		imageViewName.setFitHeight(28);
		imageViewName.setFitWidth(150);
		HBox hbImageName = new HBox(imageViewName);
		hbImageName.setAlignment(Pos.CENTER);
		gp.add(hbImageName, 0, 0);

		lbUsers = new Label("AVAILABLE USERS:");
		lbUsers.setId("lbsChat");

		vbChatBox = new VBox();
		
		spChatBox = new ScrollPane();
		spChatBox.setPrefSize(CHAT_WIDTH, CHAT_HEIGHT);
		spChatBox.setMaxSize(CHAT_WIDTH, CHAT_HEIGHT);
		spChatBox.setPadding(new Insets(10));
		spChatBox.vvalueProperty().bind(vbChatBox.heightProperty());

		spChatBox.setContent(vbChatBox);
		gp.add(spChatBox, 0, 1);

		tfMessage = new TextField();
		tfMessage.setFocusTraversable(true);
		tfMessage.setPromptText("type here your message");
		gp.add(tfMessage, 0, 2);
		tfMessage.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				//if (tfEncrypt.getText() == "" && !chbEncrypt.isSelected()) {
				func.sendMessage(Main.nickname + "+" + tfMessage.getText());
				func.printingDate();
				func.addMessageToChatBox(Main.nickname, tfMessage.getText());
				tfMessage.setText("");
					// w poprzedniej wersji byl tu nastepujacy kod dzialajacy:
					// func.addMessage(false);
					// isChatBoxEmpty = false;
				//}
				// else {
				// func.addMessage(true);
				// }
			}
		});

		
		lvAvailableUsers = new ListView(Main.olNames);
		//olNames.add(Main.nickname);
		lvAvailableUsers.setCellFactory(ComboBoxListCell.forListView(Main.olNames));
		lvAvailableUsers.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					System.out.println("clicked on " + lvAvailableUsers.getSelectionModel().getSelectedItem());
				}
			}
		});
		
		Image imageLogo = new Image(ClassLoader.getSystemResourceAsStream("chat-logo.png"));
		ImageView imageViewLogo = new ImageView(imageLogo);
		imageViewLogo.setFitHeight(97);
		imageViewLogo.setFitWidth(150);
		areaLogo = new VBox(imageViewLogo);
		areaLogo.setAlignment(Pos.CENTER);
		areaLogo.setPadding(new Insets(20, 20, 20, 20));

		lbFile = new Label("SEND FILE:");
		lbFile.setId("lbsChat");

		btnChooseFile = new Button("choose image file");
		btnChooseFile.setPrefWidth(250);
		btnChooseFile.setOnAction(e -> {
			func.chooseFile();
		});

		btnSendFile = new Button("send");
		btnSendFile.setPrefWidth(70);
		hbSendFile = new HBox(btnSendFile);
		hbSendFile.setAlignment(Pos.BASELINE_RIGHT);

		areaFile = new VBox(lbFile, btnChooseFile, hbSendFile);
		areaFile.setPadding(new Insets(10, 0, 10, 0));
		areaFile.setSpacing(5);

		lbEncrypt = new Label("ENCRYPT MESSAGE:");
		lbEncrypt.setId("lbsChat");

		chbEncrypt = new CheckBox();

		tfEncrypt = new TextField();
		tfEncrypt.setPromptText("type encrypted code");
		tfEncrypt.setOnKeyTyped(e -> {
            char inputChar = e.getCharacter().charAt(0);
            if (Character.isDigit(inputChar) != true) {
                e.consume();
            }
        });


		buttonsEncrypt = new HBox(chbEncrypt, tfEncrypt);
		buttonsEncrypt.setAlignment(Pos.CENTER_LEFT);
		buttonsEncrypt.setSpacing(5);

		areaEncrypt = new VBox(lbEncrypt, buttonsEncrypt);
		areaEncrypt.setSpacing(5);

		areaRight = new VBox(lbUsers, lvAvailableUsers, areaLogo, areaFile, areaEncrypt);
		areaRight.setSpacing(5);

		gp.add(areaRight, 2, 0, 1, 3);

		btnBack = new Button("back");
		btnBack.setId("btnBack");
		btnBack.setOnAction(e -> {
			func.removeUserFromUserBox(Main.nickname);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Client.send("$2" + Main.nickname);
			SwitchScene sc = Main.getSwitchScene();
			sc.goToLogin();
		});
		gp.add(btnBack, 0, 4);

		rowConstr1 = new RowConstraints();
		rowConstr2 = new RowConstraints();
		rowConstr3 = new RowConstraints();
		rowConstr3.setMaxHeight(30);
		gp.getRowConstraints().addAll(rowConstr1, rowConstr2, rowConstr3);
		
		return gp;

	}

	public void showMessageOptions(MouseEvent e) {
		if (e.getButton() == MouseButton.SECONDARY) {
			final ContextMenu contextMenu = new ContextMenu();
			MenuItem decryptMenuItem = new MenuItem("Decrypt");
			contextMenu.getItems().addAll(decryptMenuItem);
			contextMenu.show(vbChatBox, e.getScreenX(), e.getScreenY());
		}
	}

	public VBox getVbChatBox() {
		return vbChatBox;
	}

	public ScrollPane getSpChatBox() {
		return spChatBox;
	}

	public boolean getIsChatBoxEmpty() {
		return isChatBoxEmpty;
	}

	public void setIsChatBoxEmpty(boolean param) {
		isChatBoxEmpty = param;
	}

	public ListView<String> getLvAttachments() {
		return lvAttachments;
	}

	public boolean getEncryptionCheckbox() {
		return chbEncrypt.isSelected();
	}

	public int getEncryptionKey() {
		return Integer.parseInt(tfEncrypt.getText());
	}

}