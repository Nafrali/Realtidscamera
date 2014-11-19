package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {
		ServerMonitor m = new ServerMonitor();
		CameraReader c = new CameraReader(m);
		c.start();
		new JPEGHTTPServer(8080, m);
//		TCPIPBuilder tcpip = new TCPIPBuilder(m);
//		tcpip.start();
	}
}
