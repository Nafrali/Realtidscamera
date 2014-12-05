package GUI;

import client.ClientMonitor;
import client.Constants;
import client.ImageClass;

public class GuiThread extends Thread {

	private ClientMonitor m;
	private GUI gui;
	private ImageClass currentImage;
	private int threadID;
	boolean firsttimerun = true, firstTimeAsynch = true, run = true;

	public GuiThread(ClientMonitor m, GUI gui, int threadID) {
		super();
		this.threadID = threadID;
		this.m = m;
		this.gui = gui;
	}

	public synchronized void killThread() {
		run = false;
	}

	public void run() {
		while (run) {
			currentImage = m.getLatestImage(threadID);
			if (currentImage.getShowTime() != Constants.NO_SYNCH) {
				try {
					if (!firstTimeAsynch)
						firstTimeAsynch = true;
					long sleepTime = currentImage.getShowTime()
							- System.currentTimeMillis();
					if (sleepTime > 0)
						sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (firstTimeAsynch) {
				gui.uncheckSynch();
				firstTimeAsynch = false;
			}
			gui.refreshImage(currentImage.getImage(), m.systemInMovie(),
					threadID, currentImage.getTravelTime());
			if (firsttimerun) {
				gui.showCamera();
				gui.addToLog("Connection established.");
				firsttimerun = false;
			}
		}
	}
}