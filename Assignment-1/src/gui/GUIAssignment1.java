package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The GUI for assignment 1, DualThreads
 */
public class GUIAssignment1 implements ActionListener {
	private JFrame frame; // The Main window
	private JButton btnDisplay; // Start thread moving display
	private JButton btnDStop; // Stop moving display thread
	private JButton btnStartClock;// Start clock thread
	private JButton btnStopClock; // Stop clock thread
	private JButton btnOpen; // Open audio file
	private JButton btnPlay; // Start playing audio
	private JButton btnStop; // Stop playing
	private JButton btnGo; // Start game catch me
	private JPanel pnlMove; // The panel to move display in
	private JPanel pnlRotate; // The panel to move graphics in
	private JPanel pnlGame; // The panel to play in
	private JLabel lblPlaying; // Playing text
	private JLabel lblAudio; // Audio file
	private JTextArea txtHits; // Display hits
	private JComboBox cmbSkill; // Skill combo box, needs to be filled in
	private Controller controller; // Handles the logic
	private JLabel displayText; // The text to be shown in the display thread panel
	private JLabel clock; // label that shows the time

	/**
	 * Constructor
	 */
	public GUIAssignment1() {
	}

	/**
	 * Starts the application
	 */
	public void Start() {
		frame = new JFrame();
		frame.setBounds(0, 0, 819, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Multiple Thread Demonstrator");
		InitializeGUI(); // Fill in components
		attachListeners();
		frame.setVisible(true);
		frame.setResizable(false); // Prevent user from change size
		frame.setLocationRelativeTo(null); // Start middle screen
	}

	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI() {

		// The music player outer panel
		// new JFXPanel();
		JPanel pnlSound = new JPanel();
		Border b1 = BorderFactory.createTitledBorder("Music Player");
		pnlSound.setBorder(b1);
		pnlSound.setBounds(12, 12, 450, 100);
		pnlSound.setLayout(null);

		// Add labels and buttons to this panel
		lblPlaying = new JLabel("Now Playing: "); // Needs to be alteraed
		lblPlaying.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblPlaying.setBounds(128, 16, 300, 20);
		pnlSound.add(lblPlaying);
		JLabel lbl1 = new JLabel("Loaded Audio File: ");
		lbl1.setBounds(10, 44, 130, 13);
		pnlSound.add(lbl1);
		lblAudio = new JLabel("..."); // Needs to be altered
		lblAudio.setBounds(115, 44, 300, 13);
		pnlSound.add(lblAudio);
		btnOpen = new JButton("Open");
		btnOpen.setBounds(6, 71, 75, 23);
		pnlSound.add(btnOpen);
		btnPlay = new JButton("Play");
		btnPlay.setBounds(88, 71, 75, 23);
		pnlSound.add(btnPlay);
		btnStop = new JButton("Stop");
		btnStop.setBounds(169, 71, 75, 23);
		pnlSound.add(btnStop);
		frame.add(pnlSound);

		// The moving display outer panel
		JPanel pnlDisplay = new JPanel();
		Border b2 = BorderFactory.createTitledBorder("Display Thread");
		pnlDisplay.setBorder(b2);
		pnlDisplay.setBounds(12, 118, 222, 269);
		pnlDisplay.setLayout(null);

		// Add buttons and drawing panel to this panel
		btnDisplay = new JButton("Start Display");
		btnDisplay.setBounds(10, 226, 121, 23);
		pnlDisplay.add(btnDisplay);
		btnDStop = new JButton("Stop");
		btnDStop.setBounds(135, 226, 75, 23);
		pnlDisplay.add(btnDStop);
		pnlMove = new JPanel();
		pnlMove.setBounds(10, 19, 200, 200);
		Border b21 = BorderFactory.createLineBorder(Color.black);
		pnlMove.setBorder(b21);
		displayText = new JLabel();
		pnlMove.add(displayText);
		pnlDisplay.add(pnlMove);
		frame.add(pnlDisplay);

		// The clock outer panel
		JPanel pnlTriangle = new JPanel();
		Border b3 = BorderFactory.createTitledBorder("Clock Thread");
		pnlTriangle.setBorder(b3);
		pnlTriangle.setBounds(240, 118, 222, 269);
		pnlTriangle.setLayout(null);

		// Add buttons and drawing panel to this panel
		btnStartClock = new JButton("Start clock");
		btnStartClock.setBounds(10, 226, 121, 23);
		pnlTriangle.add(btnStartClock);
		btnStopClock = new JButton("Stop");
		btnStopClock.setBounds(135, 226, 75, 23);
		pnlTriangle.add(btnStopClock);
		pnlRotate = new JPanel();
		pnlRotate.setBounds(10, 19, 200, 200);
		Border b31 = BorderFactory.createLineBorder(Color.BLACK);

		clock = new JLabel();
		clock.setFont(new Font("Serif", Font.BOLD, 36));

		pnlRotate.add(clock);
		pnlRotate.setBorder(b31);

		pnlTriangle.add(pnlRotate);
		// Add this to main window
		frame.add(pnlTriangle);

		// The game outer panel
		JPanel pnlCatchme = new JPanel();
		Border b4 = BorderFactory.createTitledBorder("Catch Me");
		pnlCatchme.setBorder(b4);
		pnlCatchme.setBounds(468, 12, 323, 375);
		pnlCatchme.setLayout(null);

		// Add controls to this panel
		JLabel lblSkill = new JLabel("Skill:");
		lblSkill.setBounds(26, 20, 50, 13);
		pnlCatchme.add(lblSkill);
		JLabel lblInfo = new JLabel("Hit Image with Mouse");
		lblInfo.setBounds(107, 13, 150, 13);
		pnlCatchme.add(lblInfo);
		JLabel lblHits = new JLabel("Hits:");
		lblHits.setBounds(240, 20, 50, 13);
		pnlCatchme.add(lblHits);
		cmbSkill = new JComboBox(); // Need to be filled in with data
		cmbSkill.setBounds(19, 41, 61, 23);
		pnlCatchme.add(cmbSkill);
		btnGo = new JButton("GO");
		btnGo.setBounds(129, 41, 75, 23);
		pnlCatchme.add(btnGo);
		txtHits = new JTextArea(); // Needs to be updated
		txtHits.setBounds(233, 41, 71, 23);
		Border b40 = BorderFactory.createLineBorder(Color.black);
		txtHits.setBorder(b40);
		pnlCatchme.add(txtHits);
		pnlGame = new JPanel();
		pnlGame.setBounds(19, 71, 285, 283);
		Border b41 = BorderFactory.createLineBorder(Color.black);
		pnlGame.setBorder(b41);
		pnlCatchme.add(pnlGame);
		frame.add(pnlCatchme);
	}

	/**
	 * Attaches listeners to the buttons
	 */
	private void attachListeners() {
		btnOpen.addActionListener(this);
		btnStartClock.addActionListener(this);
		btnDisplay.addActionListener(this);
		btnDStop.addActionListener(this);
		btnStopClock.addActionListener(this);
	}

	/**
	 * Listens for button clicks
	 */
	@Override
	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == btnOpen) {
			showFileDialog();
		} else if (object.getSource() == btnStartClock) {
			controller.startClockThread();
		} else if (object.getSource() == btnDisplay) {
			controller.startDisplayThread();
		} else if (object.getSource() == btnDStop) {
			controller.stopDisplayThread();
		} else if (object.getSource() == btnStopClock) {
			controller.stopClockThread();
		}
	}

	/**
	 * Sets a reference to the controller class
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Updates the clock time
	 */
	public void updateClock() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String currentTime = sdf.format(new Date().getTime());
		clock.setText(currentTime);
	}

	/**
	 * Randomly places the text "Display Thread" in the panel
	 */
	public void updateDisplay() {
		Random rand = new Random();
		displayText.setText("Display Thread");
		int widthRand = rand.nextInt( pnlMove.getWidth() - displayText.getWidth() ) + 1;
		int heightRand = rand.nextInt( pnlMove.getHeight() - displayText.getHeight() ) + 1;
		displayText.setLocation( widthRand, heightRand );
	}

	private void showFileDialog() {
		String fileName = null;

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = chooser.getSelectedFile().toURI().toString(); // .toString();
			System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
		}
		MediaPlayer mediaPlayer = new MediaPlayer(new Media(fileName));
		mediaPlayer.play();
		System.out.println("mediaPlayer.play()");
	}

}
