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

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import skeleton.client.ClientMonitor;
import skeleton.client.ClientSocket;

public class GUI extends JFrame implements ItemListener {

	private ClientMonitor m;
	private ArrayList<ClientSocket> camList;
	private ArrayList<ImagePanel> imagePanels;
	private JPanel displayPanel;
	private BufferedImage nocamfeed, waitingforconnect;
	private JCheckBox synch;
	private JPanel camDisplay;
	private JLabel systemMode;
	private JTextArea actionLogArea = new JTextArea();
	private byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
	public static final int MAXCAMERAS = 2;

	public GUI(ClientMonitor m) {
		super();
		this.m = m;
		getContentPane().setLayout(new BorderLayout());
		camList = new ArrayList<ClientSocket>();
		imagePanels = new ArrayList<ImagePanel>();
		addSyncCheckbox();
		addMenu();
		camDisplay = new JPanel();
		camDisplay.setLayout(new GridLayout(0, 2));

		systemMode = new JLabel("System in idle mode.");
		displayPanel = new JPanel();
		JPanel modeSelectionPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(0, 2));
		modeSelectionPanel.setLayout(new BorderLayout());
		modeSelectionPanel.add(new CameraModeRadioButtonPane(this, m),
				BorderLayout.WEST);
		modeSelectionPanel.add(synch, BorderLayout.EAST);
		modeSelectionPanel.add(systemMode, BorderLayout.CENTER);
		displayPanel.add(actionLogArea);
		displayPanel.add(modeSelectionPanel);
		try {
			nocamfeed = ImageIO.read(new File("../files/nocamfeed.jpg"));
			waitingforconnect = ImageIO
					.read(new File("../files/waitforcon.jpg"));
			for (int i = 0; i < 2; i++)
				camDisplay.add(new JLabel(new ImageIcon(nocamfeed)));
		} catch (IOException ex) {
			System.out.println("\"No camera feed\" image not found.");
		}
		getContentPane().add(camDisplay, BorderLayout.CENTER);
		getContentPane().add(displayPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		pack();
		setVisible(true);
		setResizable(false);

	}

	public void refreshImage(byte[] newPicture, boolean movieMode, int imgNbr,
			long traveltime) {
		jpeg = newPicture;
		try {
			imagePanels.get(imgNbr).refresh(jpeg, traveltime);
			if (movieMode)
				systemMode.setText("System in movie mode");
			else
				systemMode.setText("System in idle mode");
		} catch (Exception e) {
			addToLog("Could not refresh image: " + imgNbr
					+ ". Camera not connected.");
		}
	}

	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (e.getStateChange() == ItemEvent.SELECTED)
			m.uppdateSynchMode(true);
		else if (e.getStateChange() == ItemEvent.DESELECTED)
			m.uppdateSynchMode(false);

	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		JMenuItem addCam = new AddCameraButton(this, m, camList);
		JMenuItem remCam = new RemoveCameraButton(this, m, camList);
		menu.add(addCam);
		menu.add(remCam);
		menuBar.add(menu);
		add(menuBar, BorderLayout.NORTH);
	}

	private void addSyncCheckbox() {
		synch = new JCheckBox("Synchronized mode");
		synch.setMnemonic(KeyEvent.VK_S);
		synch.setSelected(true);
		synch.addItemListener(this);
	}

	public void addCamera() {
		ImagePanel newCam = new ImagePanel(camList.size() - 1);
		imagePanels.add(newCam);
		camDisplay.remove(camList.size() - 1);
		camDisplay.add(newCam, camList.size() - 1);
	}

	public void addToLog(String string) {
		actionLogArea.append(string);

	}

	public void removeCamera(int camNbr) {
		camDisplay.remove(camNbr - 1);
		camDisplay.add(new JLabel(new ImageIcon(nocamfeed)), camNbr - 1);
	}

	public void setWaitImage(int camNbr) {
		camDisplay.remove(camNbr);
		camDisplay.add(new JLabel(new ImageIcon(waitingforconnect)), camNbr);

	}
}
