package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import client.ClientMonitor;

public class RemoveCameraButton extends JMenuItem implements ActionListener {

	private GUI gui;

	/**
	 * Creates a listener for the remove camera button in the GUI
	 * 
	 * @param gui
	 *            The GUI
	 * @param m
	 *            The ClientMonitor
	 */
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
		try {
			int camNbr = Integer.parseInt(id);
			gui.setNoCamFeedImage(camNbr - 1);
			gui.removeCamera(camNbr - 1);
			gui.addToLog("Camera " + camNbr + " has been removed.");
		} catch (NumberFormatException e) {
			gui.addToLog(id + " could not be resolved to a digit.");
		} catch (Exception e) {
			gui.addToLog("Camera " + id + " does not exist.");
		}

	}
}
