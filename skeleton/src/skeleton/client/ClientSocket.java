package skeleton.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientSocket extends Thread {
	private ClientMonitor monitor;
	private String address;
	private int port;
	private Socket s;
	private InputStream is;
	private ServerWriter sw;
	private int cameraNbr;

	public ClientSocket(ClientMonitor m, String address, int port, int cameraNbr) {
		super();
		this.cameraNbr = cameraNbr;
		this.monitor = m;
		this.address = address;
		this.port = port;
	}

	private void createWriter() {
		sw = new ServerWriter(monitor, s);
		System.out.println("ServerWriter created");
		sw.start();

	}

	private void destroyWriter() {
		sw.interrupt();
		try {
			sw.join();
		} catch (InterruptedException e) {
			System.out.println("Could not do ServerWriter.join()");
		}
	}

	private void connect() {
		boolean connected = false;
		while (!connected) {
			try {
				s = new Socket(address, port);
				is = s.getInputStream();
				System.out.println("Connected to server @" + address + ":"
						+ port);
				createWriter();
				connected = true;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
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
		connect();
		System.out.println("Waiting for data...");
		while (true) {
			try {
				int read = 0;
				byte[] packetLength = new byte[4];
				if (is.read(packetLength, 0, 4) == -1) {
					is = null;
					s.close();
					destroyWriter();
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
					monitor.newPackage(data, cameraNbr);
				}

			} catch (SocketException e) {
				try {
					s.close();
					destroyWriter();
					connect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}