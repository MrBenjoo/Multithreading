package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import model.Buffer;
import model.Encrypter;
import model.Reader;
import model.Writer;
import view.GUIEncrypt;

/**
 * Handles the logic.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Controller {
	private GUIEncrypt gui;
	private Writer writer;
	private Encrypter encrypter;
	private Reader reader;
	private LinkedList<String> sourceTxt, encryptedTxt, sourceTxtCopy;

	/**
	 * Constructor
	 */
	public Controller() {
		initWorkers();
		showGUI();
	}

	/**
	 * Initialize the threads and buffer.
	 */
	private void initWorkers() {
		Buffer buffer = new Buffer();
		writer = new Writer(buffer, this);
		encrypter = new Encrypter(buffer);
		reader = new Reader(buffer, this);
	}

	/**
	 * Display GUI.
	 */
	private void showGUI() {
		gui = new GUIEncrypt(this);
		gui.show();
	}

	/**
	 * Tries to load and read the selected file.
	 * 
	 * @param fileName
	 *            path to where the file is located.
	 */
	public void loadText(String fileName) {
		boolean error = false;
		try {
			readFromTextFile(fileName);
		} catch (FileNotFoundException ex) {
			error = true;
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			error = true;
			System.out.println("Error reading file '" + fileName + "'");
		} finally {
			if (!error) {
				gui.setBtnEncEnabled(true);
				gui.setBtnDecEnabled(false);
			}
		}
	}

	/**
	 * Read each line from the source text and add it to the list.
	 * 
	 * @param fileName
	 *            path to where the file is located.
	 * @throws IOException
	 */
	private void readFromTextFile(String fileName) throws IOException {
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		sourceTxt = new LinkedList<String>();
		sourceTxtCopy = new LinkedList<String>();
		while ((line = bufferedReader.readLine()) != null) {
			sourceTxt.add(line);
			gui.setDecryptedText(line);
		}
		sourceTxtCopy = sourceTxt;
		bufferedReader.close();
	}

	/**
	 * Start encrypting.
	 */
	public void encrypt() {
		initWorkers();
		writer.setModeEncrypt();
		reader.setModeEncrypt();
		startWorkers();
	}

	/**
	 * Start decrypting.
	 */
	public void decrypt() {
		encryptedTxt = getEncryptedText();
		gui.getEncryptedText();
		initWorkers();
		writer.setModeDecrypt();
		reader.setModeDecrypt();
		startWorkers();
	}

	private LinkedList<String> getEncryptedText() {
		String encTxt = gui.getEncryptedText();
		LinkedList<String> list = new LinkedList<>();
		if (encTxt != null) {
			for (String line : encTxt.split("\\n")) {
				list.add(line);
			}
		}
		return list;
	}

	private void startWorkers() {
		new Thread(writer).start();
		new Thread(encrypter).start();
		new Thread(reader).start();
	}

	/**
	 * This method is called from the writer thread when it's encrypting. It will
	 * add the strings from the source text to the buffer.
	 * 
	 * @return string (line) from the source text.
	 */
	public String getSourceString() {
		if (sourceTxt.size() > 0) {
			return sourceTxt.removeFirst();
		} else {
			return null;
		}
	}

	/**
	 * This method is called from the writer thread when it's decrypting. It will
	 * add the strings from the encrypted text to the buffer.
	 * 
	 * @return string (line) from the encrypted text.
	 */
	public String getEncryptedString() {
		if (encryptedTxt.size() > 0) {
			return encryptedTxt.removeFirst();
		} else {
			return null;
		}
	}

	/**
	 * Called when the writer thread has processed all of the strings in the text.
	 * Stop the threads and reset the source text.
	 */
	public void workFinished() {
		writer.stopThread();
		reader.stopThread();
		encrypter.stopThread();
		gui.enableButtons();
		sourceTxt = sourceTxtCopy;
	}

	/**
	 * Shows the decrypted text in the GUI.
	 * 
	 * @param decTxt
	 *            decrypted text
	 */
	public void showDecTxt(String decTxt) {
		gui.setDecryptedText(decTxt);
	}

	/**
	 * Shows the encrypted text in the GUI.
	 * 
	 * @param encTxt
	 *            encrypted text
	 */
	public void showEncTxt(String encTxt) {
		gui.setEncryptedText(encTxt);
	}

}
