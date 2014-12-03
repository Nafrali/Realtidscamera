package GUI;

import skeleton.client.ClientMonitor;
import skeleton.client.ImageClass;

public class GuiThread extends Thread {

	private ClientMonitor m;
	private GUI gui;
	private ImageClass currentImage;
	private int threadID;

	public GuiThread(ClientMonitor m, GUI gui, int threadID) {
		super();
		this.threadID=threadID;
		this.m = m;
		this.gui = gui;
	}

	public void run() {
		while (true) {
			currentImage = m.getLatestImage();
			gui.refreshImage(currentImage.getImage(), m.cameraInMovie(),
					m.getCameraNbr(), currentImage.getTravelTime());
		}
	}

}
