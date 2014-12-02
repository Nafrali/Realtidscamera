package skeleton.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerSocketHandler extends Thread {

	private ServerMonitor monitor;
	private ServerSocket serverSocket;
	private Socket socket;
	private TCPIPBuilder builder;

	// Det är fult namn, men classen ServerSocket används redan
	public ServerSocketHandler(ServerMonitor monitor) {
		super();
		this.monitor = monitor;
		serverSocket = monitor.getServerSocket();
	}

	private void destroyBuilder() {
		builder.interrupt();
		try {
			builder.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createBuilder() {
		builder = new TCPIPBuilder(monitor, socket);
		builder.start();
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client to connect");
				socket = serverSocket.accept();
				System.out.println("Client connected");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createBuilder();
			while (!socket.isClosed()) {
				byte[] data = new byte[1];
				try {
					InputStream is = socket.getInputStream();
					int n = is.read(data);
					if (data[0] == 1) {
						monitor.setMovieMode(true);
					} else if (n == -1) {
						destroyBuilder();
						socket.close();
					} else
						monitor.setMovieMode(false);
				} catch (SocketException e) {
					System.out.println("Client disconnected");					
					try {
						socket.close();
						if (socket.isClosed())
						destroyBuilder();
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

}