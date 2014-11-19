package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor;

	public ServerWriter(Socket s, ClientMonitor m) {
		try {
			monitor = m;
			os = s.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				monitor.initMovieMode();
				os.write((byte) 1 % 255);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out
						.println("ClientMonitor initMovieMode(); wait() has been interrupted.");
				e.printStackTrace();
			}
		}
	}
}
