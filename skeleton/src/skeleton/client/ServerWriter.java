package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor; 

	public ServerWriter(ClientMonitor m, String address, int port) {
		super();
		try {
			s = new Socket(address, port);
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
				boolean tmp = monitor.initMovieMode();
				int bit = (tmp == true ? 1 : 0);
				os.write((byte) bit % 255);
			} catch (IOException e) {
				System.out.println("[ServerWriter] (run) transmition error");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out
						.println("[ServerWriter] (run) ClientMonitor initMovieMode(); wait() has been interrupted.");
				e.printStackTrace();
			}
		}
	}
}
