package model;

/**
 * Encrypts the source strings. (it's also used for decrypting but the encrypted
 * string acts as an input instead of the source text),
 * 
 * @author Benjamin Sejdic
 *
 */
public class Encrypter implements Runnable {
	private Buffer buffer;
	private boolean stopThread;

	/**
	 * 
	 * @param buffer
	 *            where the encrypted string will be placed.
	 */
	public Encrypter(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		stopThread = false;
		while (!stopThread) {
			try {
				Thread.sleep(500);
				reverseText();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reversing the text (used for both encrypting and decrypting).
	 */
	private void reverseText() {
		String text;
		text = reverse(buffer.getDecryptedString());
		buffer.putEncryptedString(text);
	}

	/**
	 * @param text
	 *            to be reveresed (encrypted or decrypted).
	 * @return reveresed text
	 */
	private String reverse(String text) {
		return new StringBuilder(text).reverse().toString();
	}

	/**
	 * Stop the thread.
	 */
	public void stopThread() {
		stopThread = true;
		System.out.println("stopping Encrypter thread");
	}

}
