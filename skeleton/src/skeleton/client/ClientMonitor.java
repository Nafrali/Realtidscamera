package skeleton.client;

public class ClientMonitor {
//	private boolean movieMode;
	private boolean cameraMovie[];
	private byte[] currentPackage;
	private byte[] currentImage;
	private boolean newPicture = false;
	

	public ClientMonitor() {
		currentPackage = new byte[131084];
		cameraMovie = new boolean[2];
		
	}

	private void changeMode(boolean newMode, int cameraNbr) {
		cameraMovie[cameraNbr] = newMode;
	}
	
	private boolean isMovie() {
		return (cameraMovie[0] || cameraMovie[1]);
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
		byte[] image = new byte[currentPackage.length - 9];

		// System.out.println(currentPackage.toString().indexOf('\r'));
		System.arraycopy(currentPackage, 0, motion, 0, 1);
		// System.out.println("MotionDetected 2: " + motion[0]);
		System.arraycopy(currentPackage, 1, timestamp, 0, 8);
		System.arraycopy(currentPackage, 9, image, 0, currentPackage.length - 9);


		boolean tmp = (motion[0] == 1 ? true : false);
		changeMode(tmp, cameraNbr);
		
		currentImage = image;
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
		return currentImage;
	}

	public void uppdateMovieMode(boolean Movie) {

	}

	public void uppdateSynchMode(boolean b) {
		
	}

}
