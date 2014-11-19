package skeleton.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import skeleton.server.TCPIPBuilder;

public class ClientMain {

	public static void main(String[] args) {

		try {
			ServerSocket ss = new ServerSocket(6667);
			ServerListener sl = new ServerListener(ss);
			sl.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
