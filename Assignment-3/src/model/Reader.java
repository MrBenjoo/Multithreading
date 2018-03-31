package model;

import controller.Controller;

/**
 * Reads strings (encrypted or decrypted) from the buffer and display them in
 * the GUI.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Reader implements Runnable {
	private Buffer buffer;
	private boolean stopThread, decrypt, encrypt;
	private Controller controller;

	/**
	 * 
	 * @param buffer
	 *            where we will read the strings from.
	 * @param controller
	 *            callback to update the gui.
	 */
	public Reader(Buffer buffer, Controller controller) {
		this.buffer = buffer;
		this.controller = controller;
	}

	@Override
	public void run() {
		stopThread = false;
		while (!stopThread) {
			try {
				Thread.sleep(500);
				read();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Read text from buffer and display in the gui.
	 */
	private void read() {
		if (encrypt) {
			controller.showEncTxt(buffer.getEncryptedString());
		} else if (decrypt) {
			controller.showDecTxt(buffer.getEncryptedString());
		}
	}

	/**
	 * Encrypt mode
	 */
	public void setModeDecrypt() {
		decrypt = true;
		encrypt = false;
	}

	/**
	 * Decrypt mode
	 */
	public void setModeEncrypt() {
		decrypt = false;
		encrypt = true;
	}

	/**
	 * Stop the thread.
	 */
	public void stopThread() {
		stopThread = true;
		System.out.println("stopping Reader thread");
	}

}
