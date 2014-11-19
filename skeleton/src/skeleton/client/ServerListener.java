package skeleton.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerListener extends Thread {

	private Socket s;
	private InputStream is;
	private boolean connected = false;
	private ClientMonitor monitor;

	public ServerListener(ClientMonitor monitor, String address, int port) {
		super();
		this.monitor = monitor;
		while (!connected) {
			try {
				s = new Socket(address, port);
				System.out.println("Connected to server.");
				is = s.getInputStream();
				connected = true;
			} catch (IOException e) {
				System.out.println("Waiting for server at port: " + port
						+ " to come online.");
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
				byte[] data = new byte[131084];
				int read = 0;
				while (read < 131084) {
					int n = is.read(data, read, 131084 - read); // Blocking
					if (n == -1)
						throw new IOException("Corrupted data.");
					read += n;
				}
				monitor.newPackage(data);
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
