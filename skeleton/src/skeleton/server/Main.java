package skeleton.server;

public class Main {
	public static void main(String[] args) {
		CameraReader c = new CameraReader(new Monitor());
		c.start();
	}
}

