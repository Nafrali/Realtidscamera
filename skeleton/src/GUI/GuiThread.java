package GUI;

import skeleton.client.ClientMonitor;
import skeleton.client.ImageClass;

public class GuiThread extends Thread {

	ClientMonitor m;
	GUI gui;
	private ImageClass currentImage;

	public GuiThread(ClientMonitor m, GUI gui) {
		super();
		this.m = m;
		this.gui = gui;
	}

	public void run() {
		while (true) {
			currentImage = m.getLatestImage();
			System.out.println(currentImage.getTravelTime());
			gui.refreshImage(currentImage.getImage(), m.cameraInMovie(), m.getCameraNbr());
//			gui.refreshImage(m.getLatestImage(), m.cameraInMovie(), m.getCameraNbr());
		}
	}

}
