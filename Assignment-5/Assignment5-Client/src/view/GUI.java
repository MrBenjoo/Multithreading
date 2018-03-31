package view;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.Controller;

/**
 * @author Benjamin Sejdic
 */
public class GUI implements ActionListener {
	private JFrame frame;
	private JTextField sendField;
	private JButton btnSend;
	private JTextArea txtAreaLog;
	private Controller controller;

	/**
	 * Constructor
	 */
	public GUI(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Show the gui
	 */
	public void Start() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("CLIENT");
		InitializeGUI();
		frame.setVisible(true);
		frame.setResizable(false);
		btnSend.addActionListener(this);
	}

	/**
	 * Initialize the gui
	 */
	private void InitializeGUI() {
		sendField = new JTextField();
		sendField.setBounds(13, 13, 177, 23);
		frame.add(sendField);
		btnSend = new JButton("Send");
		btnSend.setBounds(197, 13, 75, 23);
		frame.add(btnSend);
		txtAreaLog = new JTextArea();
		txtAreaLog.setEditable(false);
		JScrollPane pane = new JScrollPane(txtAreaLog);
		pane.setBounds(12, 51, 260, 199);
		pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.add(pane);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Closing");
				controller.closing();
				e.getWindow().dispose();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSend) {
			String str = sendField.getText();
			if (str != null && !str.isEmpty()) {
				controller.send(str);
				sendField.setText("");
			}
		}
	}

	/**
	 * Add text to the log (received message from server)
	 * 
	 * @param message
	 *            received message
	 */
	public void setLogText(String message) {
		txtAreaLog.setText(txtAreaLog.getText() + "\n" + message);
	}

	public void SetEnable(boolean b) {
		btnSend.setEnabled(b);
	}

}
