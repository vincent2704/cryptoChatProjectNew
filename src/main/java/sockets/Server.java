package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import graphical.functions.ChatWindowFunc;
import javafx.application.Platform;
import main.Main;

public class Server {

	private ChatWindowFunc func;

	// needed for lambda scope
	private String inputLine;

	public Server(ChatWindowFunc func) {
		this.func = func;
	}

	private static int portNumber = 8007;

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

	private void setKindOfMessage() {
		Platform.runLater(() -> {
			if (inputLine.contains("+")) {
				String receiveNick = inputLine.substring(0, inputLine.indexOf("+"));
				String receiveMessage = inputLine.substring(inputLine.indexOf("+")+1);
				func.addMessageToChatBox(receiveNick, receiveMessage);
			}
			else{
				if (inputLine.substring(0, 2).equals("$1")) {System.out.println("$1");}
				if (inputLine.substring(0, 2).equals("$2")) ;
				else func.addUserLoggedToChatBox(inputLine);
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