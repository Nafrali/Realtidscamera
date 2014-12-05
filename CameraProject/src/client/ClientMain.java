package client;

import GUI.GUI;
import GUI.GuiThread;

public class ClientMain {

	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		GUI gui = new GUI(monitor);
	}
}
