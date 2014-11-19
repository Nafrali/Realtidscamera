package skeleton.client;

public class ClientMonitor {
	private boolean movieMode;

	public ClientMonitor() {
		movieMode = false;
	}

	synchronized public void initMovieMode() throws InterruptedException {
		while (!movieMode) {
			wait();
		}
	}

}
