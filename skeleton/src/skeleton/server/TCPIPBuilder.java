package skeleton.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPIPBuilder extends Thread {
	private Socket s;
	private OutputStream os;

	public TCPIPBuilder() {
		try {
			System.out.println("Client connecting to server...");
			s = new Socket("localhost", 6667);
			System.out.println("Client connected.");
			os = s.getOutputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		byte[] data = new byte[100]; // Arraylängden kommer vara 131084
										// (AxisBufferLength+12)
		for (int i = 0; i < 100; i++) {
			data[i] = (byte) (i % 255);
		}
		try {
			os.write(data, 0, 100);
			System.out.println("Transmitting data.");
			s.close();
		} catch (IOException e) {
			System.out.println("Could not transmit.");
		}
	}
}
