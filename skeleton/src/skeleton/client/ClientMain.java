package skeleton.client;

import GUI.GUI;
import GUI.GuiThread;

public class ClientMain {

	public static void main(String[] args) {

		ClientMonitor monitor = new ClientMonitor();

		GUI gui = new GUI(monitor);
		GuiThread guiThread = new GuiThread(monitor, gui);
		guiThread.start();

		ClientSocket cs = new ClientSocket(monitor, "localhost", 5555, 0); // Nu
																			// börjar
																			// cameraNbr
																			// på
																			// 0
		cs.start();
		ClientSocket cs2 = new ClientSocket(monitor, "localhost", 5556, 1); // Nu
																			// börjar
																			// cameraNbr
																			// på
																			// 0
		cs2.start();
	}
}
