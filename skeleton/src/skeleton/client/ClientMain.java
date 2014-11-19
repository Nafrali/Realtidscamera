package skeleton.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import skeleton.server.TCPIPBuilder;

public class ClientMain {

	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		ServerListener sl = new ServerListener(monitor,"localhost", 5555);
		ServerWriter sw = new ServerWriter(monitor, "localhost",5555);
		sl.start();

	}
}
