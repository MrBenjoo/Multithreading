package model;

import java.util.concurrent.Semaphore;

/**
 * Used in the Buffer, which is a shared resource, for synchronization between
 * reading and writing.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Semaphores {
	private Semaphore mutex, writer, encrypter, reader;

	public Semaphores() {
		mutex = new Semaphore(1);
		writer = new Semaphore(3, true);
		encrypter = new Semaphore(0, true);
		reader = new Semaphore(0, true);
	}

	/**
	 * @return Mutex semaphore
	 */
	public Semaphore mutex() {
		return this.mutex;
	}

	/***
	 * 
	 * @return Writer semaphore
	 */
	public Semaphore writer() {
		return this.writer;
	}

	/**
	 * 
	 * @return Encrypt semaphore
	 */
	public Semaphore encrypter() {
		return this.encrypter;
	}

	/**
	 * 
	 * @return Reader semaphore
	 */
	public Semaphore reader() {
		return this.reader;
	}

}
