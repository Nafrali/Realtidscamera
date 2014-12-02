package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;
	private ClientMonitor monitor; 
	private String address;
	private int port;
	private PrintWriter pw;
	
	public ServerWriter(ClientMonitor m, String address, int port) {
		super();
		monitor = m;
		this.address = address;
		this.port = port;
		connect();
	}

	private void connect() {
		boolean connected = false;
		while (!connected) {
			try {
				this.s = new Socket(address, port);
				System.out.println("ServerWriter connected to server.");
				os = s.getOutputStream();
				pw = new PrintWriter(os);
				connected = true;
			} catch (IOException e) {
				System.out.println("Waiting for server at addr: " + address
						+ " port: " + port + " to come online.");
				try {
					sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void run() {
		while (true) {
			try {
				boolean tmp = monitor.initMovieMode();
				
				int bit = (tmp == true ? 1 : 0);
				pw.write(bit);
//				os.write((byte) bit % 255);
			} catch (InterruptedException e) {
				System.out
						.println("[ServerWriter] (run) ClientMonitor initMovieMode(); wait() has been interrupted.");
				e.printStackTrace();
			}
		}
	}
}
