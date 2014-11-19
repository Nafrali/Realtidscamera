package testSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws IOException {
		System.out.println("Client connecting to server");
		Socket s = new Socket("localhost", 4446); // localhost är datorn egen
													// ip, när ni connectar till
													// cameras använder ni deras
													// ip. 4446 är porten som
													// serversocket läser ifrån
													// på servern.
		System.out.println("Client connected to server");
		OutputStream outputStreamToServer = s.getOutputStream();
		// Konstruera fem bytes, med talen 1...5
		byte[] nbrsToSend = new byte[5];
		nbrsToSend[0] = (byte) (1 % 255);
		nbrsToSend[1] = (byte) (2 % 255);
		nbrsToSend[2] = (byte) (3 % 255);
		nbrsToSend[3] = (byte) (4 % 255);
		nbrsToSend[4] = (byte) (5 % 255);
		for (int i = 0; i < nbrsToSend.length; i++) {
			// Skickar en byte i taget till Servern
			outputStreamToServer.write(nbrsToSend[i]);
		}
	}
}