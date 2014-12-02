package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import skeleton.client.ClientMonitor;
import skeleton.client.ClientSocket;

public class GUI extends JFrame implements ItemListener {

	ClientMonitor m;
	ArrayList<ClientSocket> camList;
	ArrayList<ImagePanel> imagePanels;
	JPanel displayPanel;
	JCheckBox movie;
	JCheckBox synch;
	JPanel camDisplay;
	JLabel[] delays = new JLabel[4];
	JTextField actionLogArea = new JTextField();
	boolean firstCall = true;
	byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];

	public GUI(ClientMonitor m) {
		super();
		this.m = m;
		getContentPane().setLayout(new BorderLayout());
		camList = new ArrayList<ClientSocket>();
		imagePanels = new ArrayList<ImagePanel>();
		delays[0] = new JLabel("Camera 1");
		delays[1] = new JLabel("Camera 2");
		addMovieCheckbox();
		addSyncCheckbox();
		addMenu();
		camDisplay = new JPanel();
		camDisplay.setLayout(new GridLayout(2, 2));

		displayPanel = new JPanel();
		JPanel checkBoxPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(0, 2));
		checkBoxPanel.setLayout(new BorderLayout());
		checkBoxPanel.add(movie, BorderLayout.WEST);
		checkBoxPanel.add(synch, BorderLayout.EAST);
		displayPanel.add(delays[0]);
		displayPanel.add(delays[1]);
		displayPanel.add(actionLogArea);
		displayPanel.add(checkBoxPanel);
		try {
			BufferedImage nocamfeed = ImageIO.read(new File(
					"../files/nocamfeed.jpg"));
			for (int i = 0; i < 4; i++)
				camDisplay.add(new JLabel(new ImageIcon(nocamfeed)));
		} catch (IOException ex) {
			System.out.println("\"No camera feed\" image not found.");
		}
		// camDisplay.add(imagePanelL);
		// camDisplay.add(imagePanelR);
		getContentPane().add(camDisplay, BorderLayout.CENTER);
		getContentPane().add(displayPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		pack();
		setVisible(true);
		setResizable(false);

	}

	public void refreshImage(byte[] newPicture, boolean movieMode, int imgNbr) {
		jpeg = newPicture;
		imagePanels.get(imgNbr).refresh(jpeg);
		if (movieMode) {
			delays[0].setText("Camera 1: Movie mode active.");
			delays[1].setText("Camera 2: Movie mode active.");
			movie.setSelected(true);
		} else if (!movieMode) {
			delays[0].setText("Camera 1: Movie mode inactive.");
			delays[1].setText("Camera 2: Movie mode inactive.");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == movie) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				m.uppdateMovieMode(true);
				delays[0].setText("Camera 1: Movie mode active.");
				delays[1].setText("Camera 2: Movie mode active.");
			} else if (e.getStateChange() == ItemEvent.DESELECTED)
				m.uppdateMovieMode(false);
			delays[0].setText("Camera 1: Movie mode inactive.");
			delays[1].setText("Camera 2: Movie mode inactive.");

		} else if (source == synch) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				m.uppdateSynchMode(true);
			else if (e.getStateChange() == ItemEvent.DESELECTED)
				m.uppdateSynchMode(false);
		}

	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		JMenuItem addCam = new AddCameraButton(this, m, camList, imagePanels);
		menu.add(addCam);
		menuBar.add(menu);
		add(menuBar, BorderLayout.NORTH);
	}

	private void addMovieCheckbox() {
		movie = new JCheckBox("Force movie mode");
		movie.setMnemonic(KeyEvent.VK_M);
		movie.setSelected(false);
		movie.addItemListener(this);
	}

	private void addSyncCheckbox() {
		synch = new JCheckBox("Synchronized mode");
		synch.setMnemonic(KeyEvent.VK_S);
		synch.setSelected(true);
		synch.addItemListener(this);
	}

	public void addCamera() {
		ImagePanel newCam = new ImagePanel();
		imagePanels.add(newCam);
		camDisplay.remove(camList.size() - 1);
		camDisplay.add(newCam, camList.size() - 1);
		pack();
	}

	public void addToLog(String string) {
		actionLogArea.setText(string);

	}
}
