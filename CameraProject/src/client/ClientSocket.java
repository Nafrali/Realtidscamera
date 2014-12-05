package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Handles all communication with the server side of the project.
 *
 */
public class ClientSocket extends Thread {
	private ClientMonitor monitor;
	private String address;
	private int port;
	private Socket s;
	private InputStream is;
	private ServerWriter sw;
	private int cameraNbr;
	private boolean run = true;

	public ClientSocket(ClientMonitor m, String address, int port, int cameraNbr) {
		super();
		this.cameraNbr = cameraNbr;
		this.monitor = m;
		this.address = address;
		this.port = port;
	}

	public synchronized String killSocket() {
		try {
			s.close();
			run = false;
		} catch (IOException e) {
			return "Connection failed to close.";
		}
		destroyWriter();
		return "Connection successfully closed.";
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
				System.out.println("UnknownHostException");
			} catch (IOException e) {
				try {
					sleep(3000);
				} catch (InterruptedException e1) {
					System.out.println("Connection attempt failed");
				}
			}
		}
		System.out.println("Waiting for data...");
	}

	public void run() {
		connect();

		while (run) {
			try {
				int read = 0;
				byte[] packetLength = new byte[4];
				try {
					if (is.read(packetLength, 0, 4) == -1) {
						is = null;
						s.close();
						destroyWriter();

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
				} catch (NullPointerException e) {
					s.close();
					destroyWriter();
					connect();
				}

			} catch (SocketException e) {
				try {
					s.close();
					destroyWriter();
				} catch (IOException e1) {
					System.out.println("Kunde inte stänga socketen");
				}

			} catch (IOException e) {
				System.out.println("Kunde inte stänga socketen");
			}

		}
	}
}