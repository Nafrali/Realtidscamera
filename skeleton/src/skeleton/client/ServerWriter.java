package skeleton.client;

import java.io.*;
import java.net.Socket;

public class ServerWriter extends Thread {
	private Socket s;
	private OutputStream os;

	public ServerWriter(Socket s) {
			try {
				os = s.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void Run() {
		
	}
}
