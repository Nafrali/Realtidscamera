package skeleton.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {

	private Socket s;
	private InputStream is;

	public ServerListener(ServerSocket ss) {
		try {
			System.out.println("Waiting for client to connect...");
			s = ss.accept();
			System.out.println("Connection to client established.");
			is = s.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			byte[] data = new byte[100];
			int read = 0;
			while (read < 100) {
				int n = is.read(data, read, 100 - read); // Blocking
				if (n == -1)
					throw new IOException();
				read += n;
			}
			for (int i = 0; i < data.length; i++)
				System.out.println("msgnr" + i + ": " + data[i]);
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
