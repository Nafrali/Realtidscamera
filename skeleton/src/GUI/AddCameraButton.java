package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import skeleton.client.ClientMonitor;
import skeleton.client.ClientSocket;

public class AddCameraButton extends JMenuItem implements ActionListener {

	ClientMonitor m;
	ArrayList<ClientSocket> camList;
	GUI gui;
	ArrayList<ImagePanel> imagePanels;

	public AddCameraButton(GUI gui, ClientMonitor m,
			ArrayList<ClientSocket> camList, ArrayList<ImagePanel> imagePanels) {
		addActionListener(this);
		this.m = m;
		this.camList = camList;
		this.gui = gui;
		this.imagePanels = imagePanels;
		setText("Add camera");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JFrame frame = new JFrame("Enter address of the new camera");
		String address = JOptionPane.showInputDialog(frame, "New camera ip:");
		String stringPort = JOptionPane.showInputDialog(frame,
				"New camera port:");
		int port = 0;
		try {
			port = Integer.parseInt(stringPort);
			gui.addToLog("Camera at " + address + " " + port);
			ClientSocket tmp = new ClientSocket(m, address, port,
					camList.size());
			camList.add(tmp);
			tmp.start();
			gui.addCamera();

		} catch (Exception e1) {
		}

	}
}