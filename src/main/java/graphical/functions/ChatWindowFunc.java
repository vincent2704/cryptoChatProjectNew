package graphical.functions;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import graphical.views.AlertBox;
import graphical.views.ChatWindow;
import graphical.views.SwitchScene;
import hibernate.dao.DBConnection;
import hibernate.dao.Message;
import javafx.scene.control.ContextMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * class representing the functions of the application window
 */
public class ChatWindowFunc {

	private static ChatWindow view;
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

	/**
	 * method for the button that selects the photo file
	 * method adds the selected file to the list of files waiting to be sent
	 * @author Marcin Lesniewski
	 */
	public void chooseFile() {
		FileChooser fc = new FileChooser();
		ExtensionFilter extFilterJPG = new ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		ExtensionFilter extFilterPNG = new ExtensionFilter("PNG files (*.png)", "*.PNG");
		fc.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		File selectedFile = fc.showOpenDialog(SwitchScene.getStage());
		String fileName = selectedFile.getName();
		if (selectedFile != null) {
			view.getBtnChooseFile().setText(fileName);
			view.getLvAttachments().getItems().add(selectedFile.getAbsolutePath());
			System.out.println(view.getLvAttachments());
		} else {
			AlertBox.showAndWait(AlertType.ERROR, "", "File selection error!");
		}
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

	/**
	 * method specifying how to send a message after clicking enter in the text field
	 * method determines the action when the message is encrypted and when it is not encrypted
	 * @author Marcin Lesniewski
	 * @param e KeyEvent
	 */
	public void pressEnterToSendMessage(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			if (view.getEncryptionCheckbox())
				sendMessage(Main.nickname + "+" + view.getTfMessage() + "$" + view.getEncryptionKey());
			else
				sendMessage(Main.nickname + "+" + view.getTfMessage());
				printingDate();
				addMessageToChatBox(Main.nickname, view.getTfMessage(), false);
				view.setTfMessage("");
		}
	}
	
	/**
	 * method determining the operation of the return button
	 * @author Marcin Lesniewski
	 */
	public void clickBtnBack() {
		removeUserFromUserBox(Main.nickname);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Client.send("$2" + Main.nickname);
		SwitchScene sc = Main.getSwitchScene();
		sc.goToLogin();
	}
	
	public void sendMessage(String message) {
		Client.send(message);
	}

	/**
	 * the method that adds a sent or received message to the chat window
	 * the method calls a method that prints the date above the message if necessary
	 * method has the option of opening by double-clicking the modal window to decrypt the message
	 * @author Marcin Lesniewski
	 * @param username nickname of the person sending the message
	 * @param message the content of the message that someone sends
	 * @param isEncrypted parameter describing whether the message is encrypted or not
	 */
	public void addMessageToChatBox(String username, String message, boolean isEncrypted) {
		printingDate();
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
		if (isEncrypted) createEncryptedMessageWindow(lbMessage);
		Label lbDate = new Label(formatDate);
		lbDate.setId("lbDate");
		lbDate.setMinWidth(ChatWindow.CHAT_WIDTH - 60);
		lbDate.setAlignment(Pos.TOP_RIGHT);
		hb.getChildren().addAll(lbUser, lbMessage);
		vbMessage.getChildren().addAll(hb, lbDate);
		view.getVbChatBox().getChildren().add(vbMessage);
		view.setIsChatBoxEmpty(false);
	}

	/**
	 * method that creates a new window to decrypt messages by double-clicking the mouse
	 * @author Marcin Lesniewski
	 * @param lbMessage label containing an encrypted message to decrypt
	 */
	private void createEncryptedMessageWindow(Label lbMessage) {
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

	/**
	 * method creating a new window when you want to decrypt the message in the chat window
	 * a message key is entered in the window
	 * @author Marcin Lesniewski
	 * @return flowPane new window for an encrypted message to decrypt it
	 */
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

	/**
	 * method that displays an alert when an attempt to decrypt a message occurs in the chat window
	 * if the message key is correct, it displays the encrypted message and if it is not, it displays an error alert
	 * @author Marcin Lesniewski
	 */
	private void showEncryptedMessageAlert() {
		newStage.close();
		if (pfEncryptedKey.getText().equals(view.alKey.get(index)))
			AlertBox.showAndWait(AlertType.INFORMATION, "decrypt message: ", view.alMessage.get(index));
		else
			AlertBox.showAndWait(AlertType.ERROR, "invalid key", "the message has not been decrypted");
	}

	/**
	 * method to check whether to print the date in the chat window
	 * the method calls the method that prints the date if the date is different from the last one printed or the chat window is empty
	 * @author Marcin Lesniewski
	 */
	public void printingDate() {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String d1 = formatter.format(ChatWindow.dateOfJoinToChat);
		String d2 = formatter.format(new Date());
		boolean compareDates = d1.equals(d2);
		if (view.getIsChatBoxEmpty() || !compareDates) {
			if (!compareDates) {
				ChatWindow.dateOfJoinToChat = new Date();
			}
			addDateToChatBox(new Date());
		}
	}

	/**
	 * method printing the formatted date in the chat window
	 * if the printed date is equal to today or yesterday, the words today or yesterday are printed instead of dates
	 * @author Marcin Lesniewski
	 * @param date date to be printed
	 */
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
		view.getVbChatBox().getChildren().add(printDate);
		view.setIsChatBoxEmpty(false);
	}

	/**
	 * method that adds information about the newly logged user to the chat window
	 * @author Marcin Lesniewski
	 * @param message the content of the message that someone sends
	 */
	public void addUserLoggedToChatBox(String message) {
		printingDate();
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
		view.getVbChatBox().getChildren().add(hb);
		view.setIsChatBoxEmpty(false);
	}

	/**
	 * method that adds the logged user to the mini window of logged users in the chat window
	 * @author Marcin Lesniewski
	 * @param username nickname of the person sending the message
	 */
	public void addUserToUserBox(String username) {
		if (!Main.olNames.contains(username)) {
			Main.olNames.add(username);
		}
	}

	/**
	 * method that deletes the logged user from the mini window of logged users in the chat window
	 * @author Marcin Lesniewski
	 * @param username nickname of the person sending the message
	 */
	public void removeUserFromUserBox(String username) {
		if (Main.olNames.contains(username)) {
			Main.olNames.remove(username);
		}
	}
	
	/**
	 * method that removes all chat contents, ie messages, information about logged in and logged out users, and printed dates
	 * @author Marcin Lesniewski
	 */
	public static void clearChatBox() {
		view.getVbChatBox().getChildren().clear();
		view.setIsChatBoxEmpty(true);
	}

	
	
	
	
	
	
	

	/**
	 * method that reads the history of messages from the jdbc database
	 * the method is out of date because in the current version of chat there is no such feature
	 * @author Marcin Lesniewski
	 * @throws SQLException 
	 * @throws ParseException 
	 * @deprecated
	 */
	public void loadHistory() throws SQLException, ParseException {
		 clearChatBox();
		 DBConnection dbc = new DBConnection();
		 int count = dbc.checkRowTableDB();
		 if (count == 0) {
		view.setIsChatBoxEmpty(true);
		 }
		 for (int i = 1; i <= count; i++) {
		 Message tempmessage = dbc.loadMessageFromDB(i);
		 Date currentDate = tempmessage.getDate();
		 Date tempDate = null; // pole
		 if (tempDate == null) {addDate(currentDate);}
		 else {
		 if (isPrintDate(tempDate, currentDate)) {
		 addDate(currentDate);
		 }
		 }
		 Format formatter = new SimpleDateFormat("HH:mm:ss");
		 String formatDate = formatter.format(currentDate);
		 vb = new VBox();
		 vb.setOnMousePressed(e -> encryptMessageMenu(e));
		 vb.setPadding(new Insets(5));
		 HBox hb = new HBox();
		 Label lbUser = new Label(tempmessage.getUser() + ": ");
		 lbUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
		 lbUser.setTextFill(Color.BLUE);
		 Label lbMessage = new Label(tempmessage.getMessage());
		 lbMessage.setFont(Font.font("Helvetica", 13));
		 lbMessage.setWrapText(true);
		 lbMessage.setMaxWidth(ChatWindow.CHAT_WIDTH - 50 - lbUser.getWidth());
		 Label lbDate = new Label(formatDate);
		 lbDate.setFont(Font.font("Helvetica", 8));
		 //lbDate.setTextFill(COLOR_TIME);
		 lbDate.setMinWidth(ChatWindow.CHAT_WIDTH - 50);
		 lbDate.setAlignment(Pos.TOP_RIGHT);
		 hb.getChildren().addAll(lbUser, lbMessage);
		 vb.getChildren().addAll(hb, lbDate);
		 view.getVbChatBox().getChildren().add(vb);
		 tempDate = currentDate;
		 }
	}

	/**
	 * the method that adds a sent message to the chat window and to the jdbc database
	 * the method is out of date because in the current version of chat there is no such feature
	 * @author Marcin Lesniewski
	 * @param crypto parameter describing whether the message is encrypted or not
	 * @throws SQLException 
	 * @deprecated
	 */
	public void addMessage(boolean crypto) throws SQLException {
		 DBConnection dbc = new DBConnection();
		 int count = dbc.checkRowTableDB();
		 Date currentDate = new Date();
		 Date tempDate = null; //pole
		 if (view.getIsChatBoxEmpty() || count == 0) {
		 addDate(currentDate);
		 }
		 else {
		 if (isPrintDate(tempDate, currentDate)) {
		 addDate(currentDate);
		 }
		 }
		 Format formatter = new SimpleDateFormat("HH:mm:ss");
		 String formatDate = formatter.format(currentDate);
		 vb = new VBox();
		 vb.setOnMousePressed(e -> encryptMessageMenu(e));
		 vb.setPadding(new Insets(5));
		 HBox hb = new HBox();
		 Label lbUser = new Label(Main.nickname + ": ");
		 lbUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 13));
		 lbUser.setTextFill(Color.BLUE);
		 Label lbMessage = new Label(view.getTfMessage());
		 lbMessage.setFont(Font.font("Helvetica", 13));
		 lbMessage.setWrapText(true);
		 lbMessage.setMaxWidth(ChatWindow.CHAT_WIDTH - 50 - lbUser.getWidth());
		 Label lbDate = new Label(formatDate);
		 lbDate.setFont(Font.font("Helvetica", 8));
		 //lbDate.setTextFill(COLOR_TIME);
		 lbDate.setMinWidth(ChatWindow.CHAT_WIDTH - 50);
		 lbDate.setAlignment(Pos.TOP_RIGHT);
		 hb.getChildren().addAll(lbUser, lbMessage);
		 vb.getChildren().addAll(hb, lbDate);
		 view.getVbChatBox().getChildren().add(vb);
		 DBConnection dbc2 = new DBConnection();
		 dbc2.addMessageToDB(Main.nickname, view.getTfMessage(), currentDate, crypto);
		 //messageBox.setText("");
		 tempDate = currentDate;
	}
	
	/**
	 * method compares two dates and checks if the second date should be printed
	 * @author Marcin Lesniewski
	 * @param date1 the first older date to compare
	 * @param date2 the second newer date to compare
	 * @return boolean whether the newer date should be printed or not
	 * @deprecated
	 */
	public boolean isPrintDate(Date date1, Date date2) {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formatDate1 = formatter.format(date1);
		String formatDate2 = formatter.format(date2);
		if (formatDate1.equals(formatDate2))
			return false;
		else
			return true;
	}
	
	/**
	 * method printing the formatted date in the chat window
	 * if the printed date is equal to today or yesterday, the words today or yesterday are printed instead of dates
	 * @author Marcin Lesniewski
	 * @param date date to be printed
	 * @deprecated
	 */
	public void addDate(Date date) {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date tempToday = new Date();
		Instant now = Instant.now(); //current date
		Instant before = now.minus(Duration.ofDays(1));
		Date tempYesterday = Date.from(before);
		String tempFormatDate = formatter.format(date);
		String tempFormatToday = formatter.format(tempToday);
		String tempFormatYesterday = formatter.format(tempYesterday);
		String dateLabel;
		if (tempFormatDate.equals(tempFormatToday)) {
			dateLabel = "today";
		}
		else if (tempFormatDate.equals(tempFormatYesterday)) {
			dateLabel = "yesterday";
		}
		else {
			dateLabel = tempFormatDate;
		}
		Label printDate = new Label(dateLabel);
		printDate.setMinWidth(ChatWindow.CHAT_WIDTH - 40);
		printDate.setFont(Font.font("Helvetica", 11));
		printDate.setTextFill(Color.GREY);
		printDate.setAlignment(Pos.CENTER);
		view.getVbChatBox().getChildren().add(printDate);
	}
	
	/**
	 * method displaying options for decrypting messages after clicking the right mouse button
	 * @author Marcin Lesniewski
	 * @param e event of clicking the right mouse button
	 * @deprecated
	 */
	private void encryptMessageMenu(javafx.scene.input.MouseEvent e) {
		if (e.getButton() == MouseButton.SECONDARY) {
			final ContextMenu contextMenu = new ContextMenu();
			MenuItem decryptMenuItem = new MenuItem("decrypt this message");
			MenuItem notdecryptMenuItem = new MenuItem("not decrypt this message");
			contextMenu.getItems().addAll(decryptMenuItem, notdecryptMenuItem);
			contextMenu.show(view.getVbChatBox(), e.getScreenX(), e.getScreenY());
		}
	}

}
