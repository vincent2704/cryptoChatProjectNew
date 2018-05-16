package graphical.functions;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import cryptography.StringMessageEncryption;
import graphical.views.AlertBox;
import graphical.views.ChatWindow;
import graphical.views.SwitchScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import sockets.Client;
import sockets.Server;

public class ChatWindowFunc {

	private ChatWindow view;
	private Server server;
	
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
		if(view.getEncryptionCheckbox()) {
			message = StringMessageEncryption.encode(message, view.getEncryptionKey());
		}
		Client.send(message);
	}
	
	public void addMessageToChatBox(String username, String message) {
		Date currentDate = new Date();

		Format formatter = new SimpleDateFormat("HH:mm");
		String formatDate = formatter.format(currentDate);

		VBox vbMessage = new VBox();
		//vbMessage.setOnMousePressed(e -> encryptMessageMenu(e));
		vbMessage.setPadding(new Insets(5));
		vbMessage.setId("vbMessage");

		HBox hb = new HBox();
		Label lbUser = new Label(username + ": ");
		lbUser.setId("lbUser");

		Label lbMessage = new Label(message);
		lbMessage.setId("lbMessage");
		lbMessage.setMaxWidth(ChatWindow.CHAT_WIDTH - 75 - lbUser.getWidth());

		Label lbDate = new Label(formatDate);
		lbDate.setId("lbDate");
		lbDate.setMinWidth(ChatWindow.CHAT_WIDTH - 60);
		lbDate.setAlignment(Pos.TOP_RIGHT);

		hb.getChildren().addAll(lbUser, lbMessage);
		vbMessage.getChildren().addAll(hb, lbDate);
		ChatWindow.vbChatBox.getChildren().add(vbMessage);
		
		view.setIsChatBoxEmpty(false);
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
		Instant now = Instant.now(); //current date
		String tempFormatDate = formatter.format(date);
		String tempFormatToday = formatter.format(new Date());
		String tempFormatYesterday = formatter.format(Date.from(now.minus(Duration.ofDays(1))));
		String lbDate;
		if (tempFormatDate.equals(tempFormatToday)) {
			lbDate = "today";
		}
		else if (tempFormatDate.equals(tempFormatYesterday)) {
			lbDate = "yesterday";
		}
		else {
			lbDate = tempFormatDate;
		}
		Label printDate = new Label(lbDate);
		printDate.setId("printDate");
		printDate.setMinWidth(ChatWindow.CHAT_WIDTH - 40);
		printDate.setAlignment(Pos.CENTER);
		ChatWindow.vbChatBox.getChildren().add(printDate);
		view.setIsChatBoxEmpty(false);
	}
	
	public void addUserLoggedToChatBox(String message) {
		this.printingDate();
		Label userLogged = new Label(message);
		userLogged.setId("userLogged");
		userLogged.setMinWidth(ChatWindow.CHAT_WIDTH - 40);
		userLogged.setAlignment(Pos.CENTER_LEFT);
		userLogged.setPadding(new Insets(10, 20, 10, 20));
		ChatWindow.vbChatBox.getChildren().add(userLogged);
		view.setIsChatBoxEmpty(false);
	}

	
	// metoda do poprawy (stara wersja dzialajaca)
	public void loadHistory() {
//		clearHistoryAtChatBox();
//		dbc = new DBConnection();
//		int count = dbc.checkRowTableDB();
//		if (count == 0) {
//			isChatBoxEmpty = true;
//		}
//		for (int i = 1; i <= count; i++) {
//			Message tempmessage = dbc.loadMessageFromDB(i);
//			currentDate = tempmessage.getDate();
//			if (tempDate == null) {addDate(currentDate);}
//			else {
//				if (isPrintDate(tempDate, currentDate)) {
//					addDate(currentDate);
//				}
//			}
//			Format formatter = new SimpleDateFormat("HH:mm:ss");
//			String formatDate = formatter.format(currentDate);
//			vb = new VBox();
//			vb.setOnMousePressed(e -> encryptMessageMenu(e));
//			vb.setPadding(new Insets(5));
//			HBox hb = new HBox();
//			Label lbUser = new Label(tempmessage.getUser() + ": ");
//			lbUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
//			lbUser.setTextFill(Color.BLUE);
//			Label lbMessage = new Label(tempmessage.getMessage());
//			lbMessage.setFont(Font.font("Helvetica", 13));
//			lbMessage.setWrapText(true);
//			lbMessage.setMaxWidth(CHAT_WIDTH - 50 - lbUser.getWidth());
//			Label lbDate = new Label(formatDate);
//			lbDate.setFont(Font.font("Helvetica", 8));
//			lbDate.setTextFill(COLOR_TIME);
//			lbDate.setMinWidth(CHAT_WIDTH - 50);
//			lbDate.setAlignment(Pos.TOP_RIGHT);
//			hb.getChildren().addAll(lbUser, lbMessage);
//			vb.getChildren().addAll(hb, lbDate);
//			vbChatBox.getChildren().add(vb);
//			tempDate = currentDate;
//		}
	}
	
	// metoda do poprawy (stara wersja dzialajaca)
	public void clearHistoryAtChatBox() {
//		vbChatBox = new VBox();
//		spChatBox.setContent(vbChatBox);
//		isChatBoxEmpty = true;
	}
	
	// metoda do poprawy (stara wersja dzialajaca)
	public void addMessage(boolean crypto) {
//		dbc = new DBConnection();
//		int count = dbc.checkRowTableDB();
//		currentDate = new Date();
//		if (isChatBoxEmpty || count == 0) {
//			addDate(currentDate);
//		}
//		else {
//			if (isPrintDate(tempDate, currentDate)) {
//				addDate(currentDate);
//			}
//		}
//		Format formatter = new SimpleDateFormat("HH:mm:ss");
//		String formatDate = formatter.format(currentDate);
//		vb = new VBox();
//		vb.setOnMousePressed(e -> encryptMessageMenu(e));
//		vb.setPadding(new Insets(5));
//		HBox hb = new HBox();
//		Label lbUser = new Label(Main.nickname + ": ");
//		lbUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
//		lbUser.setTextFill(Color.BLUE);
//		Label lbMessage = new Label(messageBox.getText());
//		lbMessage.setFont(Font.font("Helvetica", 13));
//		lbMessage.setWrapText(true);
//		lbMessage.setMaxWidth(CHAT_WIDTH - 50 - lbUser.getWidth());
//		Label lbDate = new Label(formatDate);
//		lbDate.setFont(Font.font("Helvetica", 8));
//		lbDate.setTextFill(COLOR_TIME);
//		lbDate.setMinWidth(CHAT_WIDTH - 50);
//		lbDate.setAlignment(Pos.TOP_RIGHT);
//		hb.getChildren().addAll(lbUser, lbMessage);
//		vb.getChildren().addAll(hb, lbDate);
//		vbChatBox.getChildren().add(vb);
//		DBConnection dbc = new DBConnection();
//		dbc.addMessageToDB(Main.nickname, messageBox.getText(), currentDate, crypto);
//		messageBox.setText("");
//		tempDate = currentDate;
	};
	
}
