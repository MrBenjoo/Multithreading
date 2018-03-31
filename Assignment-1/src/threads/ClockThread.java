package threads;

import controller.Controller;

public class ClockThread implements Runnable {
	private boolean stopClicked = false;
	private Controller controller;

	/**
	 * Constructor that takes a Controller reference as a parameter in order to
	 * modify the GUI
	 */
	public ClockThread(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		System.out.println("ClockThread started...");
		while (!stopClicked) {
			controller.updateClock();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stops ClockThread from running
	 */
	public void stopThread() {
		System.out.println("ClockThread stopped.");
		stopClicked = true;

	}

}
