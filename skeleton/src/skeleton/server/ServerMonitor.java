package skeleton.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMonitor {
	private ServerSocket serverSocket;
	private byte[] image;
	private boolean movieMode;
	private boolean socketReadImage;
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
	}

	public synchronized void storeImage(byte[] image) {
		this.image = image;
		if (!movieMode && image[0] == (byte) 1) {
			setMovieMode(true);
		}
		socketReadImage = false;
		imgNbr = (imgNbr + 1) % 125;
		notifyAll();
	}

	public synchronized byte[] getImage() {

		while ((!movieMode || imgNbr != 0) && socketReadImage) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		socketReadImage = true;

		return image;
	}

}
