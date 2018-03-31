package model;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * A shared resource for the Reader, Writer and Encrypter. It's responsible for
 * adding and returning either encrypted or decrypted strings.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Buffer {
	private ArrayBlockingQueue<String> decryptedList, encryptedList;
	private Semaphores semaphores;

	public Buffer() {
		decryptedList = new ArrayBlockingQueue<>(3);
		encryptedList = new ArrayBlockingQueue<>(3);
		semaphores = new Semaphores();
	}

	/**
	 * Adds the string to the decrypted list and notifies the encrypter by releasing
	 * the encrypter lock.
	 * 
	 * @param decryptedString
	 *            string to be places in the buffer.
	 */
	public void putDecryptedString(String decryptedString) {
		System.out.println("()WRITER PUT trying to add decrypted text to the list | METHOD: putDecryptedString");
		try {
			semaphores.writer().acquire();
			semaphores.mutex().acquire();
			System.out.println("WRITER -> adding decrypted text to the list...");
			decryptedList.add(decryptedString);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphores.mutex().release();
			semaphores.encrypter().release();
		}
	}

	/**
	 * Takes a string from the decrypted list and notifies the reader that it can
	 * start reading from the buffer.
	 * 
	 * @return decrypted string
	 */
	public String getDecryptedString() {
		System.out.println("()ENCRYPTER GET trying to take decrypted text from the list | METHOD: getDecryptedString");
		String decryptedString = null;
		try {
			semaphores.encrypter().acquire();
			semaphores.mutex().acquire();
			System.out.println("ENCRYPTER -> taking decrypted text from the list...");
			decryptedString = decryptedList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphores.mutex().release();
			semaphores.encrypter().release();
		}
		return decryptedString;
	}

	/**
	 * Adds the string to the encrypted list.
	 * 
	 * @param encryptedString
	 *            to be added to the list.
	 */
	public void putEncryptedString(String encryptedString) {
		System.out.println("()ENCRYPTER PUT trying to add encrypted text to the list | METHOD: putEncryptedString");
		try {
			semaphores.encrypter().acquire();
			semaphores.mutex().acquire();
			System.out.println("ENCRYPTER -> adding encrypted text to the list...");
			encryptedList.add(encryptedString);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphores.mutex().release();
			semaphores.reader().release();
			semaphores.writer().release();
		}
	}

	/**
	 * Takes a string from the encrypted list.
	 * 
	 * @return encrypted string.
	 */
	public String getEncryptedString() {
		System.out.println("()READER trying to take encrypted text from the list | METHOD: getEncryptedString");
		String encryptedString = null;
		try {
			semaphores.reader().acquire();
			semaphores.mutex().acquire();
			System.out.println("READER -> taking encrypted text from the list...");
			encryptedString = encryptedList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphores.mutex().release();
		}
		return encryptedString;
	}

}
