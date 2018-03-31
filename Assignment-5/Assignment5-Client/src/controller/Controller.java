package controller;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Client;
import view.GUI;

/**
 * @author Benjamin Sejdic
 *
 */
public class Controller {
	private GUI gui;
	private final String ip = "localhost";
	private final int port = 6666;
	private Client client;
	private ExecutorService executor;
	private Socket socket;

	public Controller() {
		socket = createSocket();
		if (socket != null) {
			executor = Executors.newCachedThreadPool(); // thread pool that creates a thread when needed
			gui = new GUI(this);
			gui.Start();
			client = new Client(this, socket);
			executor.submit(client);
		}
	}

	/**
	 * Create the socket
	 * 
	 * @return socket
	 */
	private Socket createSocket() {
		Socket tempSocket = null;
		try {
			tempSocket = new Socket(ip, port);
		} catch (IOException e) {
			System.out.println("Unable to establish a connection to the server.");
		}
		return tempSocket;
	}

	/**
	 * Send message to server.
	 * 
	 * @param message
	 *            to be sent.
	 */
	public void send(String message) {
		client.sendToServer(message);
	}

	/**
	 * Displays the message from the server.
	 * 
	 * @param msgFromClient
	 */
	public void receivedMessage(String msgFromClient) {
		gui.setLogText(msgFromClient);
	}

	/**
	 * Update GUI when the server goes offline.
	 */
	public void serverDown() {
		gui.setLogText("Server is down");
		gui.SetEnable(false);
	}

	/**
	 * Close the client
	 */
	public void closing() {
		client.disconnect();
		executor.shutdown();
	}

}
