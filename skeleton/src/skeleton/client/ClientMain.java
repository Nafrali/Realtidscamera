package skeleton.client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {

	public static void main(String[] args) {
		
		ClientMonitor monitor = new ClientMonitor();

		GUIt gui = new GUIt(monitor);
		GuiThread guiThread = new GuiThread(monitor, gui);
		guiThread.start();
		
		Socket s = null;
		boolean connected = false;
		while (!connected) {
			try {
				s = new Socket("130.235.35.238", 8080);
//				System.out.println("ServerListener connected to server.");
//				is = s.getInputStream();
				connected = true;
			} catch (IOException e) {
			}
		}
		
		ServerListener2 sl = null;
		ServerWriter2 sw = null;
		try {
			sl = new ServerListener2(monitor, s.getInputStream());
			sw = new ServerWriter2(monitor, s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		sl.start();
		sw.start();

	}
}