package skeleton.client;

public class ClientMain {

	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		ServerListener sl = new ServerListener(monitor, "127.0.0.1", 5555);
		ServerWriter sw = new ServerWriter(monitor, "127.0.0.1", 5555);
		sl.start();
		sw.start();

	}
}
