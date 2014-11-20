package skeleton.client;

public class ClientMain {

	public static void main(String[] args) {
		
		ClientMonitor monitor = new ClientMonitor();
		GUIt gui = new GUIt(monitor);
		ServerListener sl = new ServerListener(monitor, "localhost", 5555);
		ServerWriter sw = new ServerWriter(monitor, "localhost", 5555);
		sl.start();
		sw.start();

	}
}
