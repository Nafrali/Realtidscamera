package GUI;

import skeleton.client.ClientMonitor;
import skeleton.client.ImageClass;

public class GuiThread extends Thread {

	private ClientMonitor m;
	private GUI gui;
	private ImageClass currentImage;
	private int threadID;
	boolean firsttime = true;

	public GuiThread(ClientMonitor m, GUI gui, int threadID) {
		super();
		System.out.println("tråden initieras");
		this.threadID = threadID;
		this.m = m;
		this.gui = gui;
	}

	public void run() {
		while (true) {
			currentImage = m.getLatestImage(threadID);
			try {
				sleep(currentImage.getShowTime() - System.currentTimeMillis());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gui.refreshImage(currentImage.getImage(), m.getMode(), threadID,
					currentImage.getTravelTime());
			if (firsttime) {
				gui.addCamera();
				firsttime = false;
			}
		}
	}

}
