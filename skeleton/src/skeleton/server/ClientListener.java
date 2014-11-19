package skeleton.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener extends Thread {

	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream is;
	private ServerMonitor monitor;

	public ClientListener(ServerMonitor monitor) {
		super();
		this.monitor = monitor;
		serverSocket = monitor.getServerSocket();
		try {
			System.out.println("Listener waiting for client to connect...");
			socket = serverSocket.accept();
			System.out.println("Listener connection to client established.");
			is = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1];
			try {
				int n = is.read(data);
				if (data[0] == 1) {
					monitor.setMovieMode(true);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
