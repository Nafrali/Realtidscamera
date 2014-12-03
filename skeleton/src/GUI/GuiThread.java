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
		System.out.println("tråden initieras");
		this.threadID=threadID;
		this.m = m;
		this.gui = gui;
	}

	public void run() {
		while (true) {
			currentImage = m.getLatestImage(threadID);
			gui.refreshImage(currentImage.getImage(), m.cameraInMovie(),
					threadID, currentImage.getTravelTime());
		}
	}

}
