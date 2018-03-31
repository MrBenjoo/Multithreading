package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;

import controller.Controller;

public class GUIEncrypt implements ActionListener {
	private JFrame frame; // The Main window
	private JLabel lblSrc; // The source text
	private JLabel lblDst; // The encrypted text

	private JTextArea txtSrc; // The input field for source text
	private JTextArea txtDst; // The input field for encrypted text
	private JButton btnEnc; // The Encrypt button
	private JButton btnDec; // The Decrypt button
	private JButton btnLoad; // Load file button

	private Controller controller;

	public GUIEncrypt(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Show GUI
	 */
	public void show() {
		frame = new JFrame();
		frame.setBounds(0, 0, 910, 421);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Simple Encryption");
		InitializeGUI();
		attachListeners();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.decode("#5a5a5a"));
		frame.setLocationRelativeTo(null);
	}

	private void attachListeners() {
		btnLoad.addActionListener(this);
		btnEnc.addActionListener(this);
		btnDec.addActionListener(this);
	}

	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI() {
		// First, create the static components
		JLabel lab1 = new JLabel("Plain Text");
		lab1.setForeground(Color.WHITE);
		lab1.setBounds(13, 13, 74, 13);
		frame.add(lab1);
		JLabel lab2 = new JLabel("Encrypted Text");
		lab2.setForeground(Color.WHITE);
		lab2.setBounds(483, 13, 99, 13);
		frame.add(lab2);

		// Then add the two lists (of string)
		txtSrc = new JTextArea();
		txtSrc.setEditable(false);
		JScrollPane s1 = new JScrollPane(txtSrc);
		s1.setBounds(12, 35, 356, 264);
		s1.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.add(s1);
		txtDst = new JTextArea();
		txtDst.setEditable(false);
		JScrollPane s2 = new JScrollPane(txtDst);
		s2.setBounds(486, 35, 393, 264);
		s2.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.add(s2);

		// The buttons
		btnEnc = new JButton("Encrypt ->");
		btnEnc.setBounds(374, 102, 106, 23);
		btnEnc.setEnabled(false);
		frame.add(btnEnc);
		btnDec = new JButton("<- Decrypt");
		btnDec.setBounds(374, 141, 106, 23);
		btnDec.setEnabled(false);
		frame.add(btnDec);
		btnLoad = new JButton("Load Working Text");
		btnLoad.setBounds(343, 319, 159, 23);
		frame.add(btnLoad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(btnLoad)) {
			loadText();
		} else if (event.getSource().equals(btnEnc)) {
			onBtnEncrypt();
		} else if (event.getSource().equals(btnDec)) {
			onBtnDecrypt();
		}
	}

	private void loadText() {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fileChooser.getSelectedFile().getAbsolutePath();
			txtSrc.setText(null);
			controller.loadText(fileName);
		}
	}

	private void onBtnEncrypt() {
		txtDst.setText(null);
		disableButtons();
		controller.encrypt();
	}

	private void onBtnDecrypt() {
		txtSrc.setText(null);
		disableButtons();
		controller.decrypt();
	}

	/**
	 * Set decrypted text in the left textarea.
	 * 
	 * @param decryptedTxt
	 */
	public void setDecryptedText(String decryptedTxt) {
		txtSrc.append(decryptedTxt + "\n");
	}

	/**
	 * Set encrypted text in the right textarea.
	 * 
	 * @param encryptedTxt
	 */
	public void setEncryptedText(String encryptedTxt) {
		txtDst.append(encryptedTxt + "\n");
	}

	public void clearSourceText() {
		txtSrc.setText("");
	}

	public void enableButtons() {
		btnDec.setEnabled(true);
		btnEnc.setEnabled(true);
	}

	public void disableButtons() {
		btnDec.setEnabled(false);
		btnEnc.setEnabled(false);
	}

	public void setBtnDecEnabled(boolean status) {
		btnDec.setEnabled(status);
	}

	public void setBtnEncEnabled(boolean status) {
		btnEnc.setEnabled(status);
	}

	public String getEncryptedText() {
		return txtDst.getText().toString();
	}

}
