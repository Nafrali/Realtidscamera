package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import client.ClientMonitor;
import client.ClientSocket;

public class AddCameraButton extends JMenuItem implements ActionListener {

	private ArrayList<ClientSocket> camList;
	private GUI gui;

	/**
	 * Creates a camera button
	 * 
	 * @param gui
	 *            A GUI object
	 * @param m
	 *            A ServerMonitor object
	 * @param camList
	 *            A list with ClientSocket
	 */
	public AddCameraButton(GUI gui, ArrayList<ClientSocket> camList) {
		addActionListener(this);
		this.camList = camList;
		this.gui = gui;
		setText("Add camera");
	}

	public void actionPerformed(ActionEvent e) {
		if (camList.size() >= GUI.MAXCAMERAS) {
			gui.addToLog("Maximum amount of cameras (" + GUI.MAXCAMERAS
					+ ") already added.");

		} else {
			JFrame frame = new JFrame("Enter address of the new camera");
			String host = JOptionPane.showInputDialog(frame, "New camera ip:");
			String stringPort = JOptionPane.showInputDialog(frame,
					"New camera port:");
			int port = 0;
			try {
				port = Integer.parseInt(stringPort);
				gui.addToLog("Trying to connect to camera @" + host + ":"
						+ port + "...");
				gui.addCamera(host, port);

			} catch (NumberFormatException nEx) {
				gui.addToLog(stringPort + " could not be resolved to a digit.");
			} catch (Exception e1) {
				gui.addToLog("Failed to add camera @" + host + ":" + port
						+ "...");
			}
		}
	}
}
