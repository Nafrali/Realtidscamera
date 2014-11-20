package skeleton.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPIPBuilder extends Thread { // RENAME!!
	private ServerSocket serverSocket;
	private OutputStream os;
	private Socket socket;
	private ServerMonitor monitor;

	// Denna klass connectar till ServerListener
	public TCPIPBuilder(ServerMonitor monitor) {
		super();
		this.monitor = monitor;
		serverSocket = monitor.getServerSocket();

		try {
			System.out.println("Writer waiting for client to connect...");
			socket = serverSocket.accept();
			System.out.println("Writer connection to client established.");
			os = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

		while (true) {

			byte[] data = monitor.getImage();
			try {
				os.write(data);
				System.out.println("Transmitting data.");
			} catch (IOException e) {
				System.out.println("Could not transmit.");
			}
		}
	}
}
