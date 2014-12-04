package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {

		int port = 5555;
		if (args.length == 0) {
			System.out.println("No port given, using standard port: " + port);
			return;
		} else {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Invalid port number");
				return;
			}
		}
		System.out.println("Starting server");
		System.out.println("Listening at port " + port);
		ServerMonitor m = new ServerMonitor(port);

		CameraReader c = new CameraReader(m, args[1], args[2]);
//		CameraReader c = new CameraReader(m);
		c.start();		
		ServerSocketHandler ssh = new ServerSocketHandler(m);
		ssh.start();
		JPEGHTTPServer jpeghttp = new JPEGHTTPServer(8010, m);
		jpeghttp.start();


	}
}
