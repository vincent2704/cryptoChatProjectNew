package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cryptography.StringMessageEncryption;
import graphical.functions.ChatWindowFunc;
import graphical.views.ChatWindow;
import javafx.application.Platform;
import main.Main;

public class Server {

	private ChatWindowFunc func;

	// needed for lambda scope
	private String inputLine;

	public Server(ChatWindowFunc func) {
		this.func = func;
	}

	private static int portNumber = 8006;

	public void receive() {
		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			System.out.println("Client connected on port " + portNumber + ". Receiving requests.");
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Received message: " + inputLine + " from " + clientSocket.toString());
				pw.println(inputLine);
				setKindOfMessage();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method specifying the type of message transmission
	 * method determines what the program should do when it receives a message
	 * @author Marcin Lesniewski
	 */
	private void setKindOfMessage() {
		Platform.runLater(() -> {
			if (inputLine.contains("+")) {
				String receiveNick = inputLine.substring(0, inputLine.indexOf("+"));
				if (inputLine.contains("$")) {
					String receiveEncryptedMessage = inputLine.substring(inputLine.indexOf("+") + 1,
							inputLine.indexOf("$"));
					String receiveEncryptedMessageKey = inputLine.substring(inputLine.indexOf("$") + 1);
					ChatWindow.alMessage.add(receiveEncryptedMessage);
					ChatWindow.alKey.add(receiveEncryptedMessageKey);
					func.addMessageToChatBox(
							receiveNick, "[encrypted message]:\n " + StringMessageEncryption
									.encode(receiveEncryptedMessage, Integer.parseInt(receiveEncryptedMessageKey)),
							true);

				} else {
					String receiveMessage = inputLine.substring(inputLine.indexOf("+") + 1);
					func.addMessageToChatBox(receiveNick, receiveMessage, false);
				}
			} else {
				if (inputLine.substring(0, 2).equals("$1")) {
					func.addUserToUserBox(inputLine.substring(2));
					func.addUserLoggedToChatBox("user " + inputLine.substring(2) + " logged in to cryptochat");
				}
				if (inputLine.substring(0, 2).equals("$2")) {
					func.removeUserFromUserBox(inputLine.substring(2));
					func.addUserLoggedToChatBox("user " + inputLine.substring(2) + " logged out from cryptochat");
				}
				if (inputLine.substring(0, 2).equals("$3")) {
					Client.send("$4" + Main.nickname);
					System.out.println("$3");
				}
				if (inputLine.substring(0, 2).equals("$4")) {
					func.addUserToUserBox(inputLine.substring(2));
					System.out.println("$4");
				}
			}
		});
	}

	public void concurrentReceive() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			receive();
		});
	}

}