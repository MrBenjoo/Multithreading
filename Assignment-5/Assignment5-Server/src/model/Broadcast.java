package model;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Broadcast message from the server to all connected clients.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Broadcast implements Runnable {
	private ArrayList<Socket> sockets;
	private String message;

	/**
	 * 
	 * @param list
	 *            contain sockets that are connected to the clients
	 * @param message
	 *            to be broadcasted to the clients
	 */
	public Broadcast(ArrayList<Socket> list, String message) {
		this.message = message;
		this.sockets = list;
	}

	@Override
	public void run() {
		for (Socket client : sockets) {
			PrintWriter output;
			try {
				output = new PrintWriter(client.getOutputStream());
				output.println(message); 
				output.flush();
				System.out.println("Message broadcasted to " + sockets.size() + " clients.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
