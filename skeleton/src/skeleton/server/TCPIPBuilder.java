package skeleton.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPIPBuilder extends Thread {
	private Socket s;
	private InputStream is;
	private OutputStream os;

	public TCPIPBuilder() {
		try {
			s = new Socket("localhost", 6667);
			is = s.getInputStream();
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
		
	}
}
