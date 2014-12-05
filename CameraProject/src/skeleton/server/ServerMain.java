package skeleton.server;

public class ServerMain {
	public static void main(String[] args) {

		int port = 5555;
		int serverPort = 0;
		if (args.length != 3) {
			System.out.println("Invalid arguments, use [server port] [camera address] [camera port]");
			return;
		} else {
			try {
				port = Integer.parseInt(args[0]);
				serverPort = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.out.println("Invalid port number or invalud camera port");
				return;
			}
		}
		System.out.println("Starting server");
		System.out.println("Listening at port " + port);
		ServerMonitor m = new ServerMonitor(port);

		// args[1] = camera t.ex. argus-1.student.lth.se args[2] = camera porten t.ex. 2425
		CameraReader c = new CameraReader(m, args[1], args[2]);
//		CameraReader c = new CameraReader(m);
		c.start();		
		ServerSocketHandler ssh = new ServerSocketHandler(m);
		ssh.start();
		JPEGHTTPServer jpeghttp = new JPEGHTTPServer(8010, m);
		jpeghttp.start();


	}
}
