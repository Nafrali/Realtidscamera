package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import skeleton.client.ClientMonitor;

public class RemoveCameraButton extends JMenuItem implements ActionListener {

	private GUI gui;

	public RemoveCameraButton(GUI gui, ClientMonitor m) {
		addActionListener(this);
		this.gui = gui;
		setText("Remove camera");

	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame frame = new JFrame("Enter id-number of camera to be removed.");
		String id = JOptionPane.showInputDialog(frame, "Camera to be removed:");
		if (id == null) {
			return;
		}
		int camNbr = Integer.parseInt(id);
		try {
			gui.removeCamera(camNbr - 1);
			gui.addToLog("Camera " + camNbr + " has been removed.");
		} catch (Exception e) {
			gui.addToLog("Camera " + camNbr + " does not exist.");
		}

	}
}
