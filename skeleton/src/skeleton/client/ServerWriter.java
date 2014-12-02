package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor;
	private int[] cameraModes = new int[3];

	private boolean cameraState;
	private int cameraNbr;

	public ServerWriter(ClientMonitor m, Socket socket) {
		super();
		// B�rjar i movie
		cameraState = true;
		this.cameraNbr = cameraNbr;
		monitor = m;
		this.s = socket;
		try {
			os = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (!s.isClosed()) {
			try {
				boolean tmp = monitor.initMovieMode();
				int bit = (tmp == true ? 1 : 0);
				os.write((byte) bit % 255);

			} catch (Exception e) {

				boolean tmp;
				try {
					tmp = monitor.initMovieMode();
					if (cameraState != tmp) {
						int bit = (tmp == true ? 1 : 0);
						os.write((byte) bit % 255);
						cameraState = tmp;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}
	}
}
