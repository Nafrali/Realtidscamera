package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {

		ServerMonitor m = new ServerMonitor(5555);
		CameraReader c = new CameraReader(m);
		c.start();
		JPEGHTTPServer jpeghttp = new JPEGHTTPServer(8080, m);
		jpeghttp.start();
		TCPIPBuilder tcpip = new TCPIPBuilder(m);
		ClientListener cl = new ClientListener(m);
		cl.start();
		tcpip.start();
		cl.start();
	}
}
