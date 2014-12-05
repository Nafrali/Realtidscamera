package client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor;
	private int lastMode = 234;

	public ServerWriter(ClientMonitor m, Socket socket) {
		super();
		// B�rjar i movie
		monitor = m;
		this.s = socket;
		try {
			os = socket.getOutputStream();
		} catch (IOException e) {
			System.out.println("ServerWriter creation error, no output stream available.");
		}
	}

	public void run() {
		while (!s.isClosed()) {
			try {
				int tmp;
				tmp = monitor.initMovieMode();
				if (tmp != lastMode) {				
					os.write((byte) tmp % 255);
					lastMode = tmp;
				}			

			} catch (Exception e) {
				System.out.println("Connection severed");
			}

		}
	}
}
