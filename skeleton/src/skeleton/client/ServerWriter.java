package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor;
	private int[] cameraModes = new int[3];

	private boolean cameraState;

	public ServerWriter(ClientMonitor m, Socket socket) {
		super();
		// B�rjar i movie
		cameraState = false;
		monitor = m;
		this.s = socket;
		try {
			os = socket.getOutputStream();
		} catch (IOException e) {
			System.out.println("Fel i SW");
		}
	}

	public void run() {
		while (!s.isClosed()) {
			try {
				boolean tmp;
				tmp = monitor.initMovieMode();
				if (cameraState != tmp) {
					int bit = (tmp == true ? 1 : 0);
					os.write((byte) bit % 255);
					cameraState = tmp;
				}

			} catch (Exception e) {
				System.out.println("Fel i SW");
			}

		}
	}
}
