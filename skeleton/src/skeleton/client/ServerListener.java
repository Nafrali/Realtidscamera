package skeleton.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerListener extends Thread {

	private Socket s;
	private InputStream is;
	private ClientMonitor monitor;
	private String address;
	private int port;

	public ServerListener(ClientMonitor monitor, String address, int port) {
		super();
		this.address = address;
		this.port = port;
		this.monitor = monitor;
		connect();
	}

	private void connect() {
		boolean connected = false;
		while (!connected) {
			try {
				this.s = new Socket(address, port);
//				System.out.println("ServerListener connected to server.");
				this.is = s.getInputStream();
				connected = true;
			} catch (IOException e) {
//				System.out.println("Waiting for server at addr: " + address
//						+ " port: " + port + " to come online.");
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
				int read = 0;
				byte[] packetLength = new byte[4];
				if (is.read(packetLength, 0, 4) == -1) {
					is = null;
					s.close();
					connect();
				} else {

					int length = 0;
					for (int i = 0; i < packetLength.length; i++) {
						length = (length << 8) + (packetLength[i] & 0xff);
					}
					byte[] data = new byte[length];

					while (read < length) {
						int n = is.read(data, read, length - read); // Blocking
						if (n == -1)
							throw new IOException("Corrupted data.");
						read += n;
					}
					monitor.newPackage(data, 1);
				}

				//TODO Camera 0 || 1
//				monitor.newPackage(data, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
