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
import skeleton.client.Constants;

public class GUI extends JFrame implements ItemListener {

	private ClientMonitor m;
	private ArrayList<ClientSocket> camList;
	private ArrayList<ImagePanel> imagePanels;
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

	public GUI(ClientMonitor m) {
		super();
		this.m = m;
		getContentPane().setLayout(new BorderLayout());
		camList = new ArrayList<ClientSocket>();
		imagePanels = new ArrayList<ImagePanel>();
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
		modeSelectionPanel.add(new CameraModeRadioButtonPane(this, m),
				BorderLayout.WEST);
		modeSelectionPanel.add(synch, BorderLayout.EAST);
		modeSelectionPanel.add(systemMode, BorderLayout.NORTH);
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
			if (m.triggeredBy() == imgNbr) {
				modeChange = "Triggered movie mode!";
			}
			imagePanels.get(imgNbr).refresh(jpeg, traveltime, modeChange);
			modeChange = "";
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

	public void addToLog(String string) {
		// String text = actionLogArea.getText();
		// String[] lines = text.split("\n");
		// if (lines.length > 5) {
		// for (int i = 0; i > 4; i++) {
		// text = lines[i + 1];
		// }
		// }
		// actionLogArea.setText(text + string + "\n");
		actionLogArea.append(string + "\n");

	}

	public void setWaitImage(int camNbr) {
		camDisplay.remove(camNbr);
		camDisplay.add(new JLabel(new ImageIcon(waitingforconnect)), camNbr);
	}

	public void uncheckSynch() {
		synch.setSelected(false);
		addToLog("Network travel time too long, synchronous mode deactivated");
	}

	public Thread getThread(int i) {
		return threadList.get(i);
	}

	public void addCamera(String host, int port) {
		setWaitImage(camList.size());
		GuiThread newThread = new GuiThread(m, this, camList.size());
		ClientSocket newSocket = new ClientSocket(m, host, port, camList.size());
		threadList.add(newThread);
		camList.add(newSocket);
		newSocket.start();
		newThread.start();
	}

	public void removeCamera(int camNbr) {
		camDisplay.remove(camNbr);
		camDisplay.add(new JLabel(new ImageIcon(nocamfeed)), camNbr);
		GuiThread currentThread = threadList.get(camNbr);
		ClientSocket currentSocket = camList.get(camNbr);
		try {
			currentThread.interrupt();
			currentSocket.interrupt();
			currentThread.join();
			currentSocket.join();
			threadList.remove(camNbr);
			camList.remove(camNbr);
		} catch (InterruptedException e) {
			addToLog("Internal error: Camera could not be successfully removed.");
		}
	}

	public void showCamera() {
		ImagePanel newCam = new ImagePanel(camList.size() - 1);
		imagePanels.add(newCam);
		camDisplay.remove(camList.size() - 1);
		camDisplay.add(newCam, camList.size() - 1);

	}
}
