package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {
//		CameraReader c = new CameraReader();
//		c.start();
		ServerMonitor monitor = new ServerMonitor(5555);
		TCPIPBuilder tcpip = new TCPIPBuilder(monitor);
		tcpip.start();
	}
}
