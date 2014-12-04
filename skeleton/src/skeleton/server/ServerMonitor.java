package skeleton.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMonitor {
	private ServerSocket serverSocket;
	private byte[] image;
	private boolean movieMode;
	private boolean socketReadImage, idle;
	private int imgNbr;

	public ServerMonitor(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imgNbr = 0;
		movieMode = false;
	}

	public synchronized ServerSocket getServerSocket() {
		return serverSocket;
	}

	public synchronized void setMovieMode(boolean movie) {
		movieMode = movie;
		idle = false;
	}

	public synchronized void storeImage(byte[] image) {
		this.image = image;
		System.out.println(movieMode + " "+ idle + " motion: " + image[4]);
		if (!movieMode && image[4] == (byte) 1 && !idle) {
			System.out.println("Fick motion!");
			setMovieMode(true);
		}
		socketReadImage = false;
		imgNbr = (imgNbr + 1) % 125;
		notifyAll();
	}

	public synchronized byte[] getImage() {
		// För att hoppa över while-loopen ska alltid socketReadImage vara
		// false. Går vidare i movie mode,
		// eller om idle mode OCH det är var 125 bild. Eller om motion har
		// blivit detectat.
		while (!(!socketReadImage && ((movieMode) || (!movieMode && imgNbr == 0)))) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("ServerMonitor error");
			}
		}
		socketReadImage = true;
		return image;
	}

	public synchronized void forceIdle(boolean mode) {
		idle = mode;
		movieMode=false;
	}

}
