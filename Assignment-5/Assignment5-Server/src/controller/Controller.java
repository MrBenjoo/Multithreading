package controller;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Broadcast;
import model.Client;
import model.Server;
import view.GUI;

/**
 * 
 * @author Benjamin Sejdic
 *
 */
public class Controller {
	private GUI gui;
	private Server server;
	private ArrayList<Client> clients;
	private ArrayList<Socket> sockets;
	private ExecutorService executor;

	public Controller() {
		gui = new GUI(this);
		gui.Start();
		clients = new ArrayList<Client>(); // List for clients only for listening
		sockets = new ArrayList<Socket>(); // List for clients socket for send message
		server = new Server(this);
		server.start();
		executor = Executors.newCachedThreadPool(); // a thread pool that creates a new thread when needed
	}

	/**
	 * Adds a new client to the list
	 * 
	 * @param client
	 */
	public void addNewClient(Client client) {
		clients.add(client);
	}

	/**
	 * Search for the client with the given id and remove it from the list.
	 * 
	 * @param id
	 *            client
	 */
	public void findAndDelete(int id) {
		for (Client client : clients) {
			if (client.getID() == id) {
				deleteClient(client);
				break;
			}
		}
	}

	/**
	 * Removes the client and its socket from the lists.
	 * 
	 * @param client
	 *            to be removed.
	 */
	private void deleteClient(Client client) {
		boolean clientRemoved = clients.remove(client);
		boolean socketRemoved = sockets.remove(client.getSocket());
		if (clientRemoved && socketRemoved) {
			System.out.println("Client and socket deleted.");
		} else {
			System.out.println("socket was not deleted");
		}
	}

	/**
	 * Broadcast message to all the connected clients.
	 * 
	 * @param msg
	 *            to be broadcasted.
	 */
	public void broadcast(String msg) {
		executor.submit(new Broadcast(sockets, msg));
	}

	/**
	 * Display message in the GUI.
	 * 
	 * @param clientMsg
	 *            message from the client.
	 */
	public void onMsgReceived(String clientMsg) {
		gui.setLogText(clientMsg);
	}

	/**
	 * Broadcast and update the gui with the message.
	 * 
	 * @param msg
	 *            message to be sent.
	 */
	public void send(String msg) {
		broadcast("Server : " + msg);
		gui.setLogText("Server : " + msg);
	}

	/**
	 * Adds a new client socket to the socket list. The list is used to send to all
	 * clients.
	 * 
	 * @param socket
	 */
	public void addClientSocket(Socket socket) {
		System.out.println("A new client socket was added to the list.");
		sockets.add(socket);
	}

	/**
	 * Application closing
	 */
	public void onAppClosing() {
		server.close();
		executor.shutdown();
	}

}
