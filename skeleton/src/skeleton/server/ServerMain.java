package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {
//		CameraReader c = new CameraReader();
//		c.start();
		TCPIPBuilder tcpip = new TCPIPBuilder();
		tcpip.start();
	}
}
