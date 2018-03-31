package model;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.Controller;

/**
 * Listen for incoming communication requests (clients).
 * @author Benjamin Sejdic
 *
 */
public class Server extends Thread {
	private Controller controller;
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private int id;
	private Socket socket;
	private boolean online = true;

	/**
	 * Constructor
	 * @param controller handling the logic
	 */
	public Server(Controller controller) {
		this.controller = controller;
		this.serverSocket = createServerSocket();
		this.executor = Executors.newCachedThreadPool();
		this.id = 1;
	}

	/**
	 * Create the serversocket.
	 * 
	 * @return serversocket.
	 */
	private ServerSocket createServerSocket() {
		ServerSocket tempSocket = null;
		try {
			tempSocket = new ServerSocket(6666);
		} catch (IOException e) {
			System.out.println("Server could not open the port");
		}
		return tempSocket;
	}

	@Override
	public void run() {
		System.out.println("Server created.");
		while (online) {
			try {
				// New client connected to the server
				socket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("Could not connect to a new client.");
			}
			processSocket(socket);
		}
		// Shutdown all tasks
		executor.shutdown();
		System.out.println("Server is down");
	}

	/**
	 * Start the client task after it has been created and both client and socket
	 * has been saved to the list.
	 * 
	 * @param socket
	 *            that belongs to a specific client.
	 */
	private void processSocket(Socket socket2) {
		Client client = new Client(controller, socket, id);
		controller.addClientSocket(socket);
		controller.addNewClient(client);
		executor.submit(client);
		id++;
		System.out.println("A new client is connected.");
	}

	/**
	 * Close the server connection.
	 */
	public void close() {
		online = false;
	}

}
