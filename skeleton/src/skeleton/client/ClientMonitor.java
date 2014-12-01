package skeleton.client;

public class ClientMonitor {
	private boolean movieMode;
	private boolean cameraMode[];
	private byte[] currentPackage;
	private byte[] currentImage;
	private boolean newPicture = false;

	public ClientMonitor() {
		movieMode = false;
		currentPackage = new byte[131084];
		cameraMode = new boolean[2];
	}

	public void changeMode(boolean newMode, int cameraNbr) {
		
	}
	
	public synchronized void initMovieMode() throws InterruptedException {
		while (!movieMode) {
			wait();
		}
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

//		boolean tmp = 
		
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

}
