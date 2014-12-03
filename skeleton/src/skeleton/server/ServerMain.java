package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {

		int port = 10000;
		if (args.length == 0) {
			System.out
					.println("Requires a port number as argument, shutting down");
			return;
		}
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("Invalid port number");
			return;
		}
		System.out.println("Startinger server");
		System.out.println("Listening at port " + port);
		ServerMonitor m = new ServerMonitor(port);

		CameraReader c = new CameraReader(m);
		c.start();
		JPEGHTTPServer jpeghttp = new JPEGHTTPServer(8010, m);
		jpeghttp.start();
		ServerSocketHandler ssh = new ServerSocketHandler(m);
		ssh.start();

	}
}
