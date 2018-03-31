package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import controller.Controller;

/**
 * Reads the messages from the server
 * @author Benjamin Sejdic
 *
 */
public class Client implements Runnable {
	private Controller controller;
	private Socket socket;
	private int id;
	private InputStream input;
	private InputStreamReader reader;
	private BufferedReader buffReader;
	private boolean connected;

	/**
	 * Constructor 
	 * @param controller handles logic 
	 * @param socket communciation socket
	 * @param id cient id
	 * @throws IOException
	 */
	public Client(Controller controller, Socket socket, int id) {
		this.controller = controller;
		this.socket = socket;
		this.id = id;
		this.input = createInputStream();
		this.reader = new InputStreamReader(input);
		this.buffReader = new BufferedReader(reader);
	}

	/**
	 * Create the inputstream which will be used to read the messages from the
	 * client.
	 * 
	 * @return client's inputstream.
	 */
	private InputStream createInputStream() {
		InputStream input = null;
		try {
			input = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}

	@Override
	public void run() {
		connected = true;
		while (connected) {
			String clientMsg = readMessage();
			if (clientMsg != null) {
				System.out.println("new message from client : " + id);
				if (clientMsg.equals("QUIT")) {
					closeConnection();
				} else {
					controller.onMsgReceived(id + " : " + clientMsg); // New message from client
				}
			}
		}
		controller.findAndDelete(id); // Disconnect client
		System.out.println("Client with id " + id + " disconnected.");
	}

	/**
	 * Read the message that was sent from the client.
	 * 
	 * @return sent message from client.
	 */
	private String readMessage() {
		String clientMsg = null;
		try {
			clientMsg = buffReader.readLine();
		} catch (IOException e) {
			if (!socket.isClosed()) { // Something is wrong with client, close the socket
				closeConnection();
			}
		}
		return clientMsg;
	}

	/**
	 * 
	 * @param isLive
	 */
	public void setOffline(boolean isLive) {
		this.connected = isLive;
	}

	/**
	 * @return client id
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return client socket
	 */
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * Close connection to server
	 */
	private void closeConnection() {
		connected = false;
		try {
			controller.findAndDelete(id);
			input.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
