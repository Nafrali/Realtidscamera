package skeleton.client;

public class ClientMonitor {
	private boolean movieMode;

	public ClientMonitor() {
		movieMode = false;
	}

	public synchronized void initMovieMode() throws InterruptedException {
		while (!movieMode) {
			wait();
		}
	}

	public synchronized void newPackage(byte[] data) {
		
	}

}
