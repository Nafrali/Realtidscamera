package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMonitor {
	private ServerSocket serverSocket;
	private byte[] image;
	private boolean movieMode;
	private boolean socketReadImage, idle;
	private int imgNbr;

	/**
	 * Creates a ServerMonitor object which stores all shared resources
	 * @param port The port that the server shall listen on
	 */
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

	
	/**
	 * Returns the ServerSocket
	 * @return The ServerSocket
	 */
	public synchronized ServerSocket getServerSocket() {
		return serverSocket;
	}

	public synchronized void setMovieMode(boolean movie) {
		movieMode = movie;
		idle = false;
	}

	/**
	 * Stores the image and all of it's info in the monitor
	 * @param image A bytearray containing the image
	 */
	public synchronized void storeImage(byte[] image) {
		this.image = image;
		if (!movieMode && image[4] == (byte) 1 && !idle) {
			setMovieMode(true);
		}
		socketReadImage = false;
		imgNbr = (imgNbr + 1) % 125;
		notifyAll();
	}

	/**
	 * Returns the image
	 * @return A byte array containing the image
	 */
	public synchronized byte[] getImage() {
		// För att hoppa över while-loopen ska alltid socketReadImage vara
		// false. Går vidare i movie mode,
		// eller om idle mode OCH det är var 125 bild. Eller om motion har
		// blivit detectat.
		while (!(!socketReadImage && ((movieMode) || (!movieMode && imgNbr == 0)))) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Connection to server broken");
			}
		}
		socketReadImage = true;
		return image;
	}

	/**
	 * Enables the camera to be in force idle mode or not.
	 * @param mode If true the camera will be in force idle, otherwise it won't.
	 */
	public synchronized void forceIdle(boolean mode) {
		idle = mode;
		movieMode=false;
	}

}