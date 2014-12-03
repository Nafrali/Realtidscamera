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
	public TCPIPBuilder(ServerMonitor monitor, Socket s) {
		super();
		try {
			this.os = s.getOutputStream();
		} catch (IOException e) {
			System.out.println("Fel i TCPIPBuilder");;
		}
		socket = s;
		this.monitor = monitor;
		serverSocket = monitor.getServerSocket();
	}

	public void run() {

		while (!socket.isClosed()) {
			byte[] data = monitor.getImage();
			try {
				os.write(data); 
				
			} catch (IOException e) {
				System.out.println("Could not transmit.");
			}
		}
	}
}
