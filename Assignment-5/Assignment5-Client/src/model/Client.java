package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import controller.Controller;

/**
 * Sends messages to server and reads messages from the server.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Client implements Runnable {
	private Controller controller;
	private Socket socket;
	private BufferedReader buffReader;;
	private InputStreamReader input;
	private boolean online;

	/**
	 * @param controller
	 *            handle logic
	 * @param socket
	 */
	public Client(Controller controller, Socket socket) {
		this.controller = controller;
		this.socket = socket;
		System.out.println("Connected to Server");
	}

	@Override
	public void run() {
		init();
		while (online) {
			String msgFromServer = readServerMessage();
			if (msgFromServer != null) {
				processMessage(msgFromServer);
			}
		}
	}

	/**
	 * Initialize inputstreamreader and bufferedreader to be able to read messages.
	 */
	private void init() {
		online = true;
		try {
			input = new InputStreamReader(socket.getInputStream());
			buffReader = new BufferedReader(input);
		} catch (IOException e1) {
			e1.printStackTrace();
			close();
		}
	}

	/**
	 * Read the message from the server
	 * 
	 * @return server message
	 */
	private String readServerMessage() {
		String message = null;
		try {
			message = buffReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * Either close the socket connection ("Server is down" received) or update the GUI log with the received message.
	 * 
	 * @param msgFromServer received message
	 */
	private void processMessage(String msgFromServer) {
		if (msgFromServer.equals("Server is down")) {
			close();
		} else {
			controller.receivedMessage(msgFromServer);
		}
	}

	/**
	 * Set client offline
	 */
	public void disconnect() {
		this.online = false;
	}

	/**
	 * Close the socket connection
	 */
	private void close() {
		try {
			input.close();
			socket.close();
			online = false;
			controller.serverDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send message to the server
	 * 
	 * @param message
	 *            to be sent
	 */
	public void sendToServer(String message) {
		PrintWriter output;
		try {
			output = new PrintWriter(socket.getOutputStream());
			output.println(message); // Send message to server
			output.flush();
			System.out.println("message has been sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
