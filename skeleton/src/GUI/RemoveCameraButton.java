package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import skeleton.client.ClientMonitor;
import skeleton.client.ClientSocket;

public class RemoveCameraButton extends JMenuItem implements ActionListener {

	private ArrayList<ClientSocket> camList;
	private GUI gui;

	public RemoveCameraButton(GUI gui, ClientMonitor m,
			ArrayList<ClientSocket> camList) {
		addActionListener(this);
		this.gui = gui;
		this.camList = camList;
		setText("Remove camera");

	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame frame = new JFrame("Enter id-number of camera to be removed.");
		String id = JOptionPane.showInputDialog(frame, "Camera to be removed:");
		int camNbr = Integer.parseInt(id);
		try {
			ClientSocket cam = camList.remove(camNbr - 1);
			gui.removeCamera(camNbr);
			gui.addToLog("Camera " + camNbr + " has been removed.");
			cam.interrupt();
		} catch (NullPointerException e) {
			gui.addToLog("Camera " + camNbr + " does not exist.");
		}

	}
}
