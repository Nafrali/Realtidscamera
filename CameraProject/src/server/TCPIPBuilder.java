package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPIPBuilder extends Thread { // RENAME!!
	private OutputStream os;
	private Socket socket;
	private ServerMonitor monitor;

	/**
	 * TCPIPBuilder sends the images from the server to the client
	 * @param monitor A ServerMonitor object
	 * @param s Socket object 
	 */
	public TCPIPBuilder(ServerMonitor monitor, Socket s) {
			super();
			try {
				this.os = s.getOutputStream();
			} catch (IOException e) {
				System.out.println("TCPIPBuilder error, no output stream found.");;
			}
			socket = s;
			this.monitor = monitor;
		}

		public void run() {

			while (!socket.isClosed()) {
				byte[] data = monitor.getImage();
				try {
					os.write(data); 
					
				} catch (IOException e) {
					try {
						socket.close();
					} catch (IOException e1) {
					}
					System.out.println("Could not transmit.");
				}
			}
		}
	}
