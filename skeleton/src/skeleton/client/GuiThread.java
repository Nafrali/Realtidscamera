package skeleton.client;

import GUI.GUI;

public class GuiThread extends Thread {

	ClientMonitor m;
	GUI gui;

	public GuiThread(ClientMonitor m, GUI gui) {
		super();
		this.m = m;
		this.gui = gui;
	}

	public void run() {
		while (true) {
			gui.refreshImage(m.getLatestImage(), m.cameraInMovie(), m.getCameraNbr());
		}
	}

}
