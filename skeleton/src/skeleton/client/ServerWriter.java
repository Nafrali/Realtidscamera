package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private Monitor monitor;

	public ServerWriter(Socket s, Monitor m) {
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
			monitor.initMovieMode();
			try {
				os.write((byte) 1 % 255);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
