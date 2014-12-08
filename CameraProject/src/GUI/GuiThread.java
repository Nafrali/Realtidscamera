package GUI;

import client.ClientMonitor;
import client.Constants;
import client.ImageClass;

/**
 * Handles uppdating of images stored in the monitor. Grabs images from the
 * monitor as soon as a new image is available, then tells the GUI to draw it.
 */
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
		System.out.println("thread with id " +threadID + " has been killed.");
		run = false;
	}

	public void run() {
		System.out.println("thread with id " +threadID + " has been started.");
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
					gui.addToLog("Camera " + threadID
							+ " was interrupted during sleep");
				}
			} else if (firstTimeAsynch) {
				gui.uncheckSynch();
				firstTimeAsynch = false;
			}
			gui.refreshImage(currentImage.getImage(), m.systemInMovie(),
					threadID, currentImage.getTravelTime());
			if (firsttimerun) {
				gui.showCamera(threadID);
				gui.addToLog("Connection established.");
				firsttimerun = false;
			}
		}
	}
}