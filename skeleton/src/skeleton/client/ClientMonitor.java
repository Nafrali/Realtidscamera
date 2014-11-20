package skeleton.client;

public class ClientMonitor {
	private boolean movieMode;
	private byte[] currentPackage;
	private byte[] currentImage;

	public ClientMonitor() {
		movieMode = false;
		currentPackage = new byte[131084];
	}

	public synchronized void initMovieMode() throws InterruptedException {
		while (!movieMode) {
			wait();
		}
	}

	public synchronized void newPackage(byte[] data, int cameraNbr) {
		System.out.println("packageLength: " +data.length);
		System.out.println(" MotionDetected: " + data[0]);
		System.out.println("new pic added");
		currentPackage = data;
		handlePackage(cameraNbr);

	}

	private void handlePackage(int cameraNbr) {
		byte[] motion = new byte[1];
		byte[] timestamp = new byte[8];
		byte[] image = new byte[currentPackage.length - 12];
		System.out.println(currentPackage.toString().indexOf('\r'));
		System.arraycopy(currentPackage, 0, motion, 0, 1);
		System.out.println("MotionDetected 2: " + motion[0]);
		System.arraycopy(currentPackage, 2, timestamp, 0, 8);
		System.arraycopy(currentPackage, 11, image, 0,
				currentPackage.length - 12);
		currentImage = image;

	}

	public synchronized void updateGUI() {

	}

}
