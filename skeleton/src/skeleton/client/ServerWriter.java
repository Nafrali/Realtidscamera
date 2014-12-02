package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor; 
	
	public ServerWriter(ClientMonitor m, Socket socket) {
		super();
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
			} catch (InterruptedException e) {
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
