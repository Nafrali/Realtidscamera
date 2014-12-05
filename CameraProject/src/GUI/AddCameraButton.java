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

	private ClientMonitor m;
	private ArrayList<ClientSocket> camList;
	private GUI gui;

	public AddCameraButton(GUI gui, ClientMonitor m,
			ArrayList<ClientSocket> camList) {
		addActionListener(this);
		this.m = m;
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

			} catch (Exception e1) {
			}
		}
	}
}