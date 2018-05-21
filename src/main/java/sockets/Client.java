package sockets;

import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import main.Main;

public class Client {
	public static Socket socket;
	private static PrintWriter pw;

	/**
	 * Sends message to others via sockets.
	 * 
	 * @param message
	 */
	public static void send(String message) {
		if (socket == null)
			createClient();
		if (message != null)
			pw.println(message);
	}
	
	/**
	 * Creates client sending on given socket.
	 */
	public static void createClient() {
		try {
			socket = new Socket("localhost", 8006);
			pw = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends information about user who just joined the chat.
	 */
	public static void userLoggedIn() {
		if (socket == null)
			createClient();
		if (Main.nickname != null)
			try {
				pw.println("user " + Main.nickname + " logged in to cryptochat");
			} catch (Exception exc) {

			}
	}
	
	public static void userLoggedOut() {
		if (socket == null)
			createClient();
		if (Main.nickname != null)
			try {
				pw.println("user " + Main.nickname + " logged out from cryptochat");
			} catch (Exception exc) {

			}
	}

}
