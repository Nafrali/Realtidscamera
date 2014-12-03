package skeleton.client;

public class ClientMonitor {
	// private boolean movieMode;
	private boolean systemMovie;
	private byte[] currentPackage;
	private byte[][] currentImage;
	private int lastImageNbr;
	private boolean newPicture = false;
	private boolean guiSynch = false;
	private boolean newMovieSetting = false;

	private boolean GuiMovieMode =false;

	public ClientMonitor() {
		currentImage = new byte[2][];
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
		// System.out.println("packageLength: " +data.length);
		// System.out.println(" MotionDetected: " + data[0]);
		// System.out.println("new pic added");
		currentPackage = data;
		handlePackage(cameraNbr);

	}

	private void handlePackage(int cameraNbr) {
		byte[] motion = new byte[1];
		byte[] timestamp = new byte[8];
		if(currentPackage.length - 9 < 0)
			return;
		byte[] image = new byte[currentPackage.length - 9];

		// System.out.println(currentPackage.toString().indexOf('\r'));
		System.arraycopy(currentPackage, 0, motion, 0, 1);
		// System.out.println("MotionDetected 2: " + motion[0]);
		System.arraycopy(currentPackage, 1, timestamp, 0, 8);
		System.arraycopy(currentPackage, 9, image, 0, currentPackage.length - 9);

		if(motion[0]==1){
		changeMotion(true);
		}

		// TODO 2 kameror

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
				// TODO Auto-generated catch block
				e.printStackTrace();
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
