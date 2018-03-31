package threads;

import controller.Controller;

public class DisplayThread implements Runnable {
	private boolean stopClicked = false;
	private Controller controller;

	/**
	 * Constructor that takes a Controller as a parameter in order to modify the GUI
	 */
	public DisplayThread(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		System.out.println("DisplayThread started...");
		while (!stopClicked) {
			controller.updateDisplay();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stops DisplayThread from running
	 */
	public void stopThread() {
		System.out.println("DisplayThread stopped.");
		stopClicked = true;
	}

}
