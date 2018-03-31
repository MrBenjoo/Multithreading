package model;

import controller.Controller;

/**
 * Responsible for placing either encrypted or decrypted strings in to the buffer.
 * @author Benjamin Sejdic
 *
 */
public class Writer implements Runnable {
	private Buffer buffer;
	private Controller controller;
	private boolean stopThread, decrypt, encrypt;

	/**
	 * 
	 * @param buffer to which we want to write to.
	 * @param controller callback to handle view updates and logic.
	 */
	public Writer(Buffer buffer, Controller controller) {
		this.buffer = buffer;
		this.controller = controller;
	}

	@Override
	public void run() {
		stopThread = false;
		while (!stopThread) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tryWriteToBuffer();
		}
	}

	/**
	 * Tries to write to the buffer with the encrypted or decrypted string. If all
	 * the strings have been processed, stop the threads.
	 */
	private void tryWriteToBuffer() {
		String string = getText();
		if (string != null) {
			buffer.putDecryptedString(string);
		} else {
			controller.workFinished();
		}
	}

	/**
	 * Get either the encrypted string or decrypted depending on which mode
	 * (encrypt, decrypt) is active.
	 * 
	 * @return decrypted or encrypted string.
	 */
	private String getText() {
		String string = null;
		if (encrypt) {
			string = controller.getSourceString();
		} else if (decrypt) {
			string = controller.getEncryptedString();
		}
		return string;
	}

	/**
	 * Decrypting mode.
	 */
	public void setModeDecrypt() {
		decrypt = true;
		encrypt = false;
	}

	/**
	 * Encrypting mode.
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
		System.out.println("Writer thread stopped.");
	}

}
