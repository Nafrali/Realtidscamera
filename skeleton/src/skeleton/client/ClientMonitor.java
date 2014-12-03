package skeleton.client;

public class ClientMonitor {
	private boolean systemMovie;
	private byte[] currentPackage;
	private byte[][] currentImage;
	private int lastImageNbr;
	private boolean newPicture = false;
	private boolean guiSynch = false;

	public ClientMonitor() {
		currentImage = new byte[4][];
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


		currentImage[cameraNbr] = image;
		lastImageNbr = cameraNbr;
		newPicture = true;
		notifyAll();

	}

	public synchronized byte[] getLatestImage() {
		while (!newPicture) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(("Något gick fel i getLatestImage"));
			}
		}
		newPicture = false;
		return currentImage[lastImageNbr];
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
