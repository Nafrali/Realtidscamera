package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {

		ServerMonitor m = new ServerMonitor(5559);
		CameraReader c = new CameraReader(m);
		c.start();
		JPEGHTTPServer jpeghttp = new JPEGHTTPServer(8010, m);
		jpeghttp.start();
		ServerSocketHandler ssh = new ServerSocketHandler(m);
		ssh.start();


	}
}
