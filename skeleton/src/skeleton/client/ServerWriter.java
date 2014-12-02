package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor; 
	
	public ServerWriter(ClientMonitor m, OutputStream os) {
		super();
		monitor = m;
		this.os = os;
	}

	
	public void run() {
		while (true) {
			try {
				boolean tmp = monitor.initMovieMode();
				
				int bit = (tmp == true ? 1 : 0);
				os.write((byte) bit % 255);
			} catch (InterruptedException e) {
				System.out
						.println("[ServerWriter] (run) ClientMonitor initMovieMode(); wait() has been interrupted.");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
