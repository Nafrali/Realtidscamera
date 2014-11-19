package skeleton.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener extends Thread {

	private Socket s;
	private InputStream is;

	public ClientListener(int port) {
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Waiting for client to connect...");
			s = ss.accept();
			System.out.println("Connection to client established.");
			is = s.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
