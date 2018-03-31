package controller;

import gui.GUIAssignment1;
import threads.ClockThread;
import threads.DisplayThread;

public class Controller {
	private GUIAssignment1 gui;
	private DisplayThread dT;
	private ClockThread cT;
	
	private boolean dTRunning = false;
	private boolean cTRunning = false;
	

	/**
	 * Constructor that shows the GUI.
	 */
	public Controller() {
		gui = new GUIAssignment1();
		gui.setController(this);
		gui.Start();
	}

	/**
	 * Start clock thread.
	 */
	public void startClockThread() {
		if(!cTRunning) {
			cT = new ClockThread(this);
			new Thread(cT).start();
			cTRunning = true;
		}
	}

	/**
	 * Start display thread.
	 */
	public void startDisplayThread() {
		if(!dTRunning) {
			dT = new DisplayThread(this);
			new Thread(dT).start();
			dTRunning = true;
		}
	}

	/**
	 * Stop clock thread.
	 */
	public void stopClockThread() {
		if(cTRunning) {
			cT.stopThread();
			cTRunning = false;
		}
	}

	/**
	 * Stop display thread.
	 */
	public void stopDisplayThread() {
		if(dTRunning) {
			dT.stopThread();
			dTRunning = false;
		}
	}

	/**
	 * Update clock time.
	 */
	public void updateClock() {
		gui.updateClock();
	}

	/**
	 * Set new text location for "display text".
	 */
	public void updateDisplay() {
		gui.updateDisplay();

	}

}
