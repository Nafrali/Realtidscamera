package skeleton.client;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClientMonitor {
	private boolean systemMovie;
	private byte[] currentPackage;
	private byte[][] currentImage;
	private ArrayList<LinkedList<ImageClass>> buffer;
	private int lastImageNbr;
	private boolean newPicture = false;
	private boolean guiSynch = false;
	private boolean newMovieSetting = false;
	private long offset = 0;
	private boolean firstPic = true;
	private ImageClass[] imageClassArray;

	private boolean GuiMovieMode =false;

	public ClientMonitor() {
		currentImage = new byte[4][];
		imageClassArray = new ImageClass[2];
		buffer = new ArrayList<LinkedList<ImageClass>>();
		for(int i = 0; i < 2; i++){
			buffer.add(new LinkedList<ImageClass>());
		}

		currentPackage = new byte[131084];

	}
	
	public synchronized int getCameraNbr() {
		return lastImageNbr;
	}

	private void changeMotion(boolean newMode) {
		systemMovie = newMode;
	}

	private boolean isMovie() {
		return (systemMovie);
	}

	public synchronized boolean initMovieMode() throws InterruptedException {
		wait();
		return isMovie();
	}

	public synchronized void newPackage(byte[] data, int cameraNbr) {
		currentPackage = data;
		handlePackage(cameraNbr);

	}

	private void handlePackage(int cameraNbr) {
		byte[] motion = new byte[1];
		byte[] timestamp = new byte[8];
		if(currentPackage.length - 9 < 0)
			return;
		byte[] image = new byte[currentPackage.length - 9];
		System.arraycopy(currentPackage, 0, motion, 0, 1);
		System.arraycopy(currentPackage, 1, timestamp, 0, 8);
		System.arraycopy(currentPackage, 9, image, 0, currentPackage.length - 9);

		if(motion[0]==1){
		changeMotion(true);
		}


//		currentImage[cameraNbr] = image;
//		buffer.get(cameraNbr).add(new ImageClass(image, networkTravelTime(timestamp)));
		imageClassArray[cameraNbr] = new ImageClass(image, networkTravelTime(timestamp));
		lastImageNbr = cameraNbr;
		newPicture = true;
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
	

	public synchronized ImageClass getLatestImage() {
		//Just nu kan den bara hämta till kamera 0
		while (!newPicture) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(("Något gick fel i getLatestImage"));
			}
		}
		return imageClassArray[lastImageNbr];
		
//		while (!newPicture) {
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return currentImage[lastImageNbr];
	}

	public synchronized void uppdateMovieMode(boolean movie) {
		systemMovie = movie;
		notifyAll();
	}

	public synchronized void uppdateSynchMode(boolean synch) {
		guiSynch = synch;
		notifyAll();
	}

	public boolean cameraInMovie() {
		return isMovie();
	}
}
