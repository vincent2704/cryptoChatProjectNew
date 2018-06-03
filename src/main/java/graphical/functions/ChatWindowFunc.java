package graphical.functions;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import graphical.views.AlertBox;
import graphical.views.ChatWindow;
import graphical.views.SwitchScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import sockets.Client;
import sockets.Server;

public class ChatWindowFunc {

	private ChatWindow view;
	private Server server;
	private Scene scEncrypted;
	private Stage stage;
	private FlowPane fp;
	private VBox vb;
	private PasswordField pfEncryptedKey;
	private Button btnEncryptedKey;
	private Stage newStage;
	private int index;

	public ChatWindowFunc(ChatWindow chatWindow) {
		view = chatWindow;
		server = new Server(this);
	}

	public void concurrentReceive() {
		server.concurrentReceive();
	}

	public void addAttachment() {
		ListView<String> attachments = view.getLvAttachments();
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(SwitchScene.getStage());
		if (selectedFile != null) {
			attachments.getItems().add(selectedFile.getAbsolutePath());
			System.out.println(attachments);
		} else {
			AlertBox.showAndWait(AlertType.ERROR, "", "File selection error!");
		}
	}

	public void sendMessage(String message) {
		Client.send(message);
	}

	public void addMessageToChatBox(String username, String message, boolean isEncrypted) {
		this.printingDate();

		Date currentDate = new Date();

		Format formatter = new SimpleDateFormat("HH:mm");
		String formatDate = formatter.format(currentDate);

		VBox vbMessage = new VBox();
		vbMessage.setPadding(new Insets(5));
		vbMessage.setId("vbMessage");

		HBox hb = new HBox();
		Label lbUser = new Label(username + ": ");
		lbUser.setId("lbUser");

		Label lbMessage = new Label(message);
		lbMessage.setId("lbMessage");
		lbMessage.setMaxWidth(ChatWindow.CHAT_WIDTH - 75 - lbUser.getWidth());
		if (isEncrypted) {
			view.alLabel.add(lbMessage);
			lbMessage.setOnMouseClicked(e -> {
				if (e.getButton().equals(MouseButton.PRIMARY)) {
					if (e.getClickCount() == 2) {
						index = view.alLabel.indexOf(lbMessage);
						newStage = new Stage();
						Scene scene = new Scene(setEncryptedPane(), 230, 120);
						newStage.setScene(scene);
						//newStage.setAlwaysOnTop(true);
						newStage.initModality(Modality.APPLICATION_MODAL);
						newStage.showAndWait();
					}
				}
			});
		}

		Label lbDate = new Label(formatDate);
		lbDate.setId("lbDate");
		lbDate.setMinWidth(ChatWindow.CHAT_WIDTH - 60);
		lbDate.setAlignment(Pos.TOP_RIGHT);

		hb.getChildren().addAll(lbUser, lbMessage);
		vbMessage.getChildren().addAll(hb, lbDate);
		view.vbChatBox.getChildren().add(vbMessage);

		view.setIsChatBoxEmpty(false);
	}

	private FlowPane setEncryptedPane() {

		fp = new FlowPane();
		fp.setPadding(new Insets(25, 25, 25, 25));
		fp.setAlignment(Pos.CENTER);

		pfEncryptedKey = new PasswordField();
		pfEncryptedKey.setPromptText("enter decryption key");
		pfEncryptedKey
				.setStyle("-fx-text-fill: black; -fx-prompt-text-fill: rgb(186, 180, 180); -fx-border-color: black;");
		pfEncryptedKey.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) showEncryptedMessageAlert();
		});

		btnEncryptedKey = new Button("decrypt");
		btnEncryptedKey.setStyle(
				"-fx-font-weight: bold; -fx-border-color: black; -fx-text-fill: black; -fx-background-color: rgb(229, 224, 224);");
		btnEncryptedKey.setOnAction(e -> {
			showEncryptedMessageAlert();
		});

		vb = new VBox(pfEncryptedKey, btnEncryptedKey);
		vb.setSpacing(20);
		vb.setAlignment(Pos.CENTER);

		fp.getChildren().add(vb);

		return fp;

	}

	private void showEncryptedMessageAlert() {
		newStage.close();
		if (pfEncryptedKey.getText().equals(view.alKey.get(index)))
			AlertBox.showAndWait(AlertType.INFORMATION, "decrypt message: ", view.alMessage.get(index));
		else
			AlertBox.showAndWait(AlertType.ERROR, "invalid key", "the message has not been decrypted");
	}

	public void printingDate() {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String d1 = formatter.format(ChatWindow.dateOfJoinToChat);
		String d2 = formatter.format(new Date());
		boolean compareDates = d1.equals(d2);
		if (view.getIsChatBoxEmpty() || !compareDates) {
			if (!compareDates) {
				ChatWindow.dateOfJoinToChat = new Date();
			}
			this.addDateToChatBox(new Date());
		}
	}

	public void addDateToChatBox(Date date) {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		Instant now = Instant.now(); // current date
		String tempFormatDate = formatter.format(date);
		String tempFormatToday = formatter.format(new Date());
		String tempFormatYesterday = formatter.format(Date.from(now.minus(Duration.ofDays(1))));
		String lbDate;
		if (tempFormatDate.equals(tempFormatToday)) {
			lbDate = "today";
		} else if (tempFormatDate.equals(tempFormatYesterday)) {
			lbDate = "yesterday";
		} else {
			lbDate = tempFormatDate;
		}
		Label printDate = new Label(lbDate);
		printDate.setId("printDate");
		printDate.setMinWidth(ChatWindow.CHAT_WIDTH - 40);
		printDate.setAlignment(Pos.CENTER);
		printDate.setPadding(new Insets(10, 20, 10, 20));
		view.vbChatBox.getChildren().add(printDate);
		view.setIsChatBoxEmpty(false);
	}

	public void addUserLoggedToChatBox(String message) {
		this.printingDate();
		Date currentDate = new Date();
		Format formatter = new SimpleDateFormat("HH:mm");
		String formatDate = formatter.format(currentDate);
		HBox hb = new HBox();
		hb.setPadding(new Insets(5, 10, 5, 20));
		Label userLogged = new Label(message);
		userLogged.setId("userLogged");
		userLogged.setMinWidth(ChatWindow.CHAT_WIDTH - 90);
		userLogged.setAlignment(Pos.CENTER_LEFT);
		Label lbDate = new Label(formatDate);
		lbDate.setId("lbDate");
		lbDate.setAlignment(Pos.CENTER_RIGHT);
		hb.getChildren().addAll(userLogged, lbDate);
		view.vbChatBox.getChildren().add(hb);
		view.setIsChatBoxEmpty(false);
	}

	public void addUserToUserBox(String username) {
		if (!Main.olNames.contains(username)) {
			Main.olNames.add(username);
		}
	}

	public void removeUserFromUserBox(String username) {
		if (Main.olNames.contains(username)) {
			Main.olNames.remove(username);
		}
	}

	// metoda do poprawy (stara wersja dzialajaca)
	public void loadHistory() {
		// clearHistoryAtChatBox();
		// dbc = new DBConnection();
		// int count = dbc.checkRowTableDB();
		// if (count == 0) {
		// isChatBoxEmpty = true;
		// }
		// for (int i = 1; i <= count; i++) {
		// Message tempmessage = dbc.loadMessageFromDB(i);
		// currentDate = tempmessage.getDate();
		// if (tempDate == null) {addDate(currentDate);}
		// else {
		// if (isPrintDate(tempDate, currentDate)) {
		// addDate(currentDate);
		// }
		// }
		// Format formatter = new SimpleDateFormat("HH:mm:ss");
		// String formatDate = formatter.format(currentDate);
		// vb = new VBox();
		// vb.setOnMousePressed(e -> encryptMessageMenu(e));
		// vb.setPadding(new Insets(5));
		// HBox hb = new HBox();
		// Label lbUser = new Label(tempmessage.getUser() + ": ");
		// lbUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
		// lbUser.setTextFill(Color.BLUE);
		// Label lbMessage = new Label(tempmessage.getMessage());
		// lbMessage.setFont(Font.font("Helvetica", 13));
		// lbMessage.setWrapText(true);
		// lbMessage.setMaxWidth(CHAT_WIDTH - 50 - lbUser.getWidth());
		// Label lbDate = new Label(formatDate);
		// lbDate.setFont(Font.font("Helvetica", 8));
		// lbDate.setTextFill(COLOR_TIME);
		// lbDate.setMinWidth(CHAT_WIDTH - 50);
		// lbDate.setAlignment(Pos.TOP_RIGHT);
		// hb.getChildren().addAll(lbUser, lbMessage);
		// vb.getChildren().addAll(hb, lbDate);
		// vbChatBox.getChildren().add(vb);
		// tempDate = currentDate;
		// }
	}

	// metoda do poprawy (stara wersja dzialajaca)
	public void clearHistoryAtChatBox() {
		// vbChatBox = new VBox();
		// spChatBox.setContent(vbChatBox);
		// isChatBoxEmpty = true;
	}

	// metoda do poprawy (stara wersja dzialajaca)
	public void addMessage(boolean crypto) {
		// dbc = new DBConnection();
		// int count = dbc.checkRowTableDB();
		// currentDate = new Date();
		// if (isChatBoxEmpty || count == 0) {
		// addDate(currentDate);
		// }
		// else {
		// if (isPrintDate(tempDate, currentDate)) {
		// addDate(currentDate);
		// }
		// }
		// Format formatter = new SimpleDateFormat("HH:mm:ss");
		// String formatDate = formatter.format(currentDate);
		// vb = new VBox();
		// vb.setOnMousePressed(e -> encryptMessageMenu(e));
		// vb.setPadding(new Insets(5));
		// HBox hb = new HBox();
		// Label lbUser = new Label(Main.nickname + ": ");
		// lbUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
		// lbUser.setTextFill(Color.BLUE);
		// Label lbMessage = new Label(messageBox.getText());
		// lbMessage.setFont(Font.font("Helvetica", 13));
		// lbMessage.setWrapText(true);
		// lbMessage.setMaxWidth(CHAT_WIDTH - 50 - lbUser.getWidth());
		// Label lbDate = new Label(formatDate);
		// lbDate.setFont(Font.font("Helvetica", 8));
		// lbDate.setTextFill(COLOR_TIME);
		// lbDate.setMinWidth(CHAT_WIDTH - 50);
		// lbDate.setAlignment(Pos.TOP_RIGHT);
		// hb.getChildren().addAll(lbUser, lbMessage);
		// vb.getChildren().addAll(hb, lbDate);
		// vbChatBox.getChildren().add(vb);
		// DBConnection dbc = new DBConnection();
		// dbc.addMessageToDB(Main.nickname, messageBox.getText(), currentDate,
		// crypto);
		// messageBox.setText("");
		// tempDate = currentDate;
	}

	public void chooseFile() {
		FileChooser fc = new FileChooser();
		ExtensionFilter extFilterJPG = new ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		ExtensionFilter extFilterPNG = new ExtensionFilter("PNG files (*.png)", "*.PNG");
		fc.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		File selectedFile = fc.showOpenDialog(SwitchScene.getStage());
		String fileName = selectedFile.getName();
		if (selectedFile != null) {
			view.btnChooseFile.setText(fileName);
			view.lvAttachments.getItems().add(selectedFile.getAbsolutePath());
			System.out.println(view.lvAttachments);
		} else {
			AlertBox.showAndWait(AlertType.ERROR, "", "File selection error!");
		}
	};

}
