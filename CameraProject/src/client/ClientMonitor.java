package client;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ClientMonitor {

	private boolean systemMovie = false;
	private byte[] currentPackage;
	private ArrayList<LinkedList<ImageClass>> buffer;
	private boolean synch = true;
	private int mode = Constants.AUTO;
	private int triggerCam = -1;

	public ClientMonitor() {
		buffer = new ArrayList<LinkedList<ImageClass>>();
		for (int i = 0; i < 2; i++) {
			buffer.add(new LinkedList<ImageClass>());
		}
		currentPackage = new byte[131084];

	}


	public synchronized int initMovieMode() throws InterruptedException {
		wait();
		if(mode==Constants.AUTO){
			return(systemMovie ? 4:3);
		}
		return mode;
	}

	public synchronized void newPackage(byte[] data, int cameraNbr) {
		currentPackage = data;
		handlePackage(cameraNbr);

	}

	private void handlePackage(int cameraNbr) {
		byte[] motion = new byte[1];
		byte[] timestamp = new byte[8];
		if (currentPackage.length - 9 < 0)
			return;
		byte[] image = new byte[currentPackage.length - 9];
		System.arraycopy(currentPackage, 0, motion, 0, 1);
		System.arraycopy(currentPackage, 1, timestamp, 0, 8);
		System.arraycopy(currentPackage, 9, image, 0, currentPackage.length - 9);

		if (motion[0] == 1 && mode != 2 && !systemMovie) {
			triggerCam = cameraNbr;
			systemMovie = true;
		}
		long travelTime = networkTravelTime(timestamp);
		long showTime = System.currentTimeMillis()
				+ (Constants.DELAY_MODIFIER - travelTime);

		// villkoret för att gå ur synchronized, dvs negativ väntetid
		if (travelTime > Constants.DELAY_MODIFIER)
			synch = false;

		if (synch) {
			buffer.get(cameraNbr).add(
					new ImageClass(image, travelTime, showTime));
		} else {
			buffer.get(cameraNbr).add(
					new ImageClass(image, travelTime, Constants.NO_SYNCH));
		}
		// imageClassArray[cameraNbr] = new ImageClass(image,
		// networkTravelTime(timestamp));
		notifyAll();

	}

	private long networkTravelTime(byte[] timestamp) {
		long timestampLong = 0;
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < timestamp.length; i++) {
			timestampLong = (timestampLong << 8) + (timestamp[i] & 0xff);
		}

		return currentTime - timestampLong;
	}

	public synchronized ImageClass getLatestImage(int cameraID) {
		// Just nu kan den bara hämta till kamera 0

		while (buffer.get(cameraID).isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(("getLatestImage failure"));
			}
		}
		// return imageClassArray[cameraID];
		return buffer.get(cameraID).pop();

		// Har att göra med buffert! Ta ej bort! // Munkenyo
		// while (!newPicture) {
		// try {
		// wait();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// return currentImage[lastImageNbr];
	}
	
	public synchronized void uppdateMovieMode(boolean movie) {
		systemMovie = movie;
		notifyAll();
	}

	public synchronized void uppdateSynchMode(boolean guiSynch) {
		synch = guiSynch;
	}

	public synchronized void setMode(int mode) {
		triggerCam = -1;
		this.mode = mode;
		if (mode == Constants.IDLE || mode == Constants.AUTO)
			systemMovie = false;
		else
			systemMovie = true;
		notifyAll();
	}

	public synchronized int getMode() {
		return mode;
	}

	public synchronized boolean systemInMovie() {
		return systemMovie;
	}

	public synchronized int triggeredBy() {
		return triggerCam;
	}
}
