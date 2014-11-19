package testSocket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) throws IOException {
		Socket clientConnectionSocket;
		ServerSocket serverSocket = new ServerSocket(4446); // Välj en port ni
															// läser ifrånm
															// denna port kommer
															// clienten ansluta
															// mot
		System.out.println("Server waiting for client to connect");
		clientConnectionSocket = serverSocket.accept();
		System.out.println("Client connected");
		// Nu hur vi en ansluting till clienten.
		InputStream inputStreamFromClient = clientConnectionSocket
				.getInputStream();
		for (int i = 0; i < 5; i++) {
			int receivedNbr = inputStreamFromClient.read(); // Läser en byte och
															// gör om till int
			System.out.println("Received from client: " + receivedNbr);
		}
	}
}