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

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import client.ClientMonitor;
import client.ClientSocket;

public class GUI extends JFrame implements ItemListener {

	private ClientMonitor m;
	private ArrayList<ClientSocket> camList;
	private ImagePanel[] imagePanels;
	private ArrayList<GuiThread> threadList;
	private JPanel displayPanel;
	private BufferedImage nocamfeed, waitingforconnect;
	private JCheckBox synch;
	private JPanel camDisplay;
	private JLabel systemMode;
	private MessageArea actionLogArea = new MessageArea();
	private byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
	public static final int MAXCAMERAS = 2;
	private String modeChange = "";
	private int nextFreeSlot = 0;

	/**
	 * Creates a GUI object and renders an interface
	 * 
	 * @param m
	 *            A ClientMonitor object that will be used by the GUI
	 */
	public GUI(ClientMonitor m) {
		super();
		this.m = m;
		getContentPane().setLayout(new BorderLayout());
		camList = new ArrayList<ClientSocket>();
		imagePanels = new ImagePanel[MAXCAMERAS];
		threadList = new ArrayList<GuiThread>();
		addSyncCheckbox();
		addMenu();
		camDisplay = new JPanel();
		camDisplay.setLayout(new GridLayout(0, 2));

		systemMode = new JLabel("System in idle mode.");
		displayPanel = new JPanel();
		JPanel modeSelectionPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(0, 2));
		modeSelectionPanel.setLayout(new BorderLayout());
		modeSelectionPanel.add(new CameraModePane(this, m), BorderLayout.WEST);
		modeSelectionPanel.add(synch, BorderLayout.EAST);
		modeSelectionPanel.add(systemMode, BorderLayout.NORTH);
		displayPanel.add(actionLogArea);
		displayPanel.add(modeSelectionPanel);
		try {
			nocamfeed = ImageIO.read(new File("../files/nocamfeed.jpg"));
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

	/**
	 * Updates an image
	 * 
	 * @param newPicture
	 *            The new image
	 * @param movieMode
	 *            If the system is in movie mode or not
	 * @param cameraNbr
	 *            The id of the camera
	 * @param traveltime
	 *            The network delay for the image
	 */
	public synchronized void refreshImage(byte[] newPicture, boolean movieMode,
			int cameraNbr, long traveltime) {
		jpeg = newPicture;
		try {
			if (m.triggeredBy() == cameraNbr) {
				modeChange = "Triggered movie mode!";
				imagePanels[cameraNbr].refresh(jpeg, traveltime, modeChange);
				modeChange = "";
			} else {
				imagePanels[cameraNbr].refresh(jpeg, traveltime, modeChange);
			}
			if (movieMode)
				systemMode.setText("System in movie mode");
			else
				systemMode.setText("System in idle mode");
		} catch (NullPointerException e) {
			addToLog("Could not refresh image: " + cameraNbr
					+ ". Camera not found.");
		}
	}

	public void itemStateChanged(ItemEvent e) {

		if (e.getStateChange() == ItemEvent.SELECTED)
			m.uppdateSynchMode(true);
		else if (e.getStateChange() == ItemEvent.DESELECTED)
			m.uppdateSynchMode(false);

	}

	/**
	 * Creates a menu and adds it to the GUI
	 */
	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		JMenuItem addCam = new AddCameraButton(this, camList);
		JMenuItem remCam = new RemoveCameraButton(this, m);
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

	/**
	 * Appends text to the log area
	 * 
	 * @param string
	 *            The string to be appended
	 */
	public void addToLog(String string) {
		actionLogArea.append(string + "\n");
	}

	/**
	 * Replaces the old image with a waiting image.
	 * 
	 * @param camNbr
	 *            The the camera that has been removed.
	 */
	public void setWaitImage(int camNbr) {
		try {
			waitingforconnect = ImageIO
					.read(new File("../files/waitforcon.jpg"));
			camDisplay.remove(camNbr);
			camDisplay
					.add(new JLabel(new ImageIcon(waitingforconnect)), camNbr);
			pack();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Replaces the old image with a no cam image.
	 * 
	 * @param camNbr
	 *            The the camera that has been removed.
	 */
	public void setNoCamFeedImage(int camNbr) {
		try {
			nocamfeed = ImageIO.read(new File("../files/nocamfeed.jpg"));
			camDisplay.remove(camNbr);
			camDisplay.add(new JLabel(new ImageIcon(nocamfeed)), camNbr);
			pack();
		} catch (IOException ex) {
			System.out.println("\"No camera feed\" image not found.");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("No component at index " + camNbr);
		}
	}

	/**
	 * Set synchronized to false
	 */
	public void uncheckSynch() {
		synch.setSelected(false);
		addToLog("Synchronous mode deactivated");
	}

	/**
	 * Adds a camera and connects it to a server
	 * 
	 * @param host
	 *            The ip address or hostname of the server
	 * @param port
	 *            The port that the server is using
	 */
	public void addCamera(String host, int port) {
		setWaitImage(nextFreeSlot);
		GuiThread newThread = new GuiThread(m, this, nextFreeSlot);
		ClientSocket newSocket = new ClientSocket(m, host, port, nextFreeSlot);
		threadList.add(nextFreeSlot, newThread);
		camList.add(nextFreeSlot, newSocket);
		newSocket.start();
		newThread.start();
		nextFreeSlot = (nextFreeSlot + 1) % MAXCAMERAS;
	}

	/**
	 * Removes a camera
	 * 
	 * @param camNbr
	 *            The camera number that is to be removed
	 */
	public void removeCamera(int camNbr) {
		System.out.println(camNbr);
		setNoCamFeedImage(camNbr);
		nextFreeSlot = camNbr;
		try {
			threadList.get(camNbr).killThread();
			threadList.get(camNbr).join();
			addToLog(camList.get(camNbr).killSocket());
			camList.get(camNbr).join();
		} catch (InterruptedException e) {
			addToLog("Failed to stop camera threads.");
		}
		threadList.remove(camNbr);
		camList.remove(camNbr);
	}

	/**
	 * To be commented
	 */
	public void showCamera(int camNbr) {
		ImagePanel newCam = new ImagePanel(camNbr);
		imagePanels[camNbr] = newCam;
		camDisplay.remove(camNbr);
		camDisplay.add(newCam, camNbr);
		pack();
	}
}