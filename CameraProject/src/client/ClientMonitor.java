package client;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Monitor for the client side, used to protects common variables.
 *
 */
public class ClientMonitor {

	private boolean systemMovie = false;
	private byte[] currentPackage;
	private ArrayList<LinkedList<ImageClass>> buffer;
	private boolean synch = true;
	private int mode = Constants.AUTO;
	private int triggerCam = -1;

	
	/**
	 * Constructor
	 */
	public ClientMonitor() {
		buffer = new ArrayList<LinkedList<ImageClass>>();
		for (int i = 0; i < 2; i++) {
			buffer.add(new LinkedList<ImageClass>());
		}
		currentPackage = new byte[131084];

	}


	/**
	 * @return Mode of the system. 3 = Automatic mode with cammeras in idle, 4 = Automatic mode with cammeras in movie.
	 * @throws InterruptedException 
	 */
	public synchronized int initMovieMode() throws InterruptedException {
		wait();
		if(mode==Constants.AUTO){
			return(systemMovie ? 4:3);
		}
		return mode;
	}

	/**
	 * Recieves Data from the server side and stores it as currentPackage.
	 * 
	 * @param data Byta array recieved from the serverside.
	 * @param cameraNbr ID for the camera.
	 */
	public synchronized void newPackage(byte[] data, int cameraNbr) {
		currentPackage = data;
		handlePackage(cameraNbr);

	}

	
	/**
	 * Parses the latest image for the camera with ID=cameraNbr.
	 * Acts acordingly if motion is detected or if detection of Synchronized mode cant be maintained.
	 * Stores image in buffers if synchronized mode.
	 * @param cameraNbr ID for the camera with the package to be handeled.
	 */
	private void handlePackage(int cameraNbr) {
		byte[] motion = new byte[1];
		byte[] timestamp = new byte[8];
		if (currentPackage.length - 9 < 0)
			return;
		byte[] image = new byte[currentPackage.length - 9];
		//Copies parsed information to variables.
		System.arraycopy(currentPackage, 0, motion, 0, 1);
		System.arraycopy(currentPackage, 1, timestamp, 0, 8);
		System.arraycopy(currentPackage, 9, image, 0, currentPackage.length - 9);
		//Controlls motion
		if (motion[0] == 1 && mode != 2 && !systemMovie) {
			triggerCam = cameraNbr;
			systemMovie = true;
		}
		//Calculates Synchronized and delays
		long travelTime = networkTravelTime(timestamp);
		long showTime = System.currentTimeMillis()
				+ (Constants.DELAY_MODIFIER - travelTime);

		// villkoret för att gå ur synchronized, dvs negativ väntetid (-1)
		if (travelTime > Constants.DELAY_MODIFIER)
			synch = false;

		if (synch) {
			buffer.get(cameraNbr).add(
					new ImageClass(image, travelTime, showTime));
		} else {
			buffer.get(cameraNbr).add(
					new ImageClass(image, travelTime, Constants.NO_SYNCH));
		}
		notifyAll();

	}

	/**
	 * Calculates time traveled on network in ms
	 * @param timestamp Parsed time from the image	
	 * @return Actuall time traveled on network in MS
	 */
	private long networkTravelTime(byte[] timestamp) {
		long timestampLong = 0;
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < timestamp.length; i++) {
			timestampLong = (timestampLong << 8) + (timestamp[i] & 0xff);
		}

		return currentTime - timestampLong;
	}

	/**
	 * Returns number of the latest image from the buffer of camera cameraNbr.
	 * @param cameraID for requested camera
	 * @return Id of the image poped from the buffer
	 */
	public synchronized ImageClass getLatestImage(int cameraID) {
		while (buffer.get(cameraID).isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(("getLatestImage failure"));
			}
		}
		return buffer.get(cameraID).pop();
	}
	
	//Uppdates moviemode to movie
	public synchronized void uppdateMovieMode(boolean movie) {
		systemMovie = movie;
		notifyAll();
	}
	//Uppdates SynchMode to guiSynch
	public synchronized void uppdateSynchMode(boolean guiSynch) {
		synch = guiSynch;
	}
	
	//Updates mode and lastTrigger of the system. 1=ForceMovie, 2=ForceIdle, 3=Auto
	public synchronized void setMode(int mode) {
		triggerCam = -1;
		this.mode = mode;
		if (mode == Constants.IDLE || mode == Constants.AUTO)
			systemMovie = false;
		else
			systemMovie = true;
		notifyAll();
	}

	/**
	 * @return Current system mode
	 */
	public synchronized int getMode() {
		return mode;
	}

	/**
	 * @return current systemMovieMode Move||Idle
	 */
	public synchronized boolean systemInMovie() {
		return systemMovie;
	}

	/**
	 * @return Last camera to trigger motion while in Automatic mode
	 */
	public synchronized int triggeredBy() {
		return triggerCam;
	}
}
