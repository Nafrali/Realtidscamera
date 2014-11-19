package skeleton.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPIPBuilder extends Thread { // RENAME!!
	private ServerSocket serverSocket;
	private OutputStream os;
	private Socket socket;
	private ServerMonitor monitor;

	// Denna klass connectar till ServerListener
	public TCPIPBuilder(ServerMonitor monitor) {
		this.monitor = monitor;
		serverSocket = monitor.getServerSocket();

		try {
			System.out.println("Waiting for client to connect...");
			socket = serverSocket.accept();
			System.out.println("Connection to client established.");
			os = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		byte[] data = monitor.getImage();// Arraylängden kommer vara 131084 //
											// (AxisBufferLength+12)
		try {
			os.write(data, 0, 131084);
			System.out.println("Transmitting data.");
			socket.close();
		} catch (IOException e) {
			System.out.println("Could not transmit.");
		}
	}
}
