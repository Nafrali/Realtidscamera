package skeleton.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {

	public static void main(String[] args) {
		
		ClientMonitor monitor = new ClientMonitor();

		GUIt gui = new GUIt(monitor);
		GuiThread guiThread = new GuiThread(monitor, gui);
		guiThread.start();
		Socket s1 = null;
		try {
			s1 = new Socket("localhost", 5555);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServerListener sl = new ServerListener(monitor, s1);
		ServerWriter sw = new ServerWriter(monitor, s1);

		sl.start();
		sw.start();

	}
}
