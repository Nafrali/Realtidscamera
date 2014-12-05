package server;

import se.lth.cs.eda040.proxycamera.AxisM3006V;

public class CameraReader extends Thread {
	private ServerMonitor m;
	private AxisM3006V myCamera;
	private String camera;
	private int port;
	private final byte DELIMITER = (byte) '\n';
	private final byte ENDBYTE = (byte) '\r';

	/**
	 * Creates a new CameraReader object
	 * 
	 * @param m ServerMonitor 
	 * @param camera The ip address or hostname of the camera 
	 * @param portString The listening port for the camera server
	 */
	public CameraReader(ServerMonitor m, String cameraAddress, String portString) {
		super();
		this.m = m;
		this.camera=cameraAddress;
		this.port = Integer.parseInt(portString);
		myCamera = new AxisM3006V();

	}

	private void initialize() {
		myCamera.init();
		myCamera.setProxy(camera, port);
		myCamera.connect();
	}

	public void run() {
		initialize();

		while (true) {
			// Paket enl. formen packeLength(4)motion(1)timestamp(8)image(len)
			byte[] target = new byte[AxisM3006V.IMAGE_BUFFER_SIZE + 13];
			int len = myCamera.getJPEG(target, 13) + 9;

			byte[] image = new byte[len + 4];
			byte[] packetLength = new byte[4];

			for (int i = 0; i < 4; i++) {
				packetLength[i] = (byte) ((len >> (24 - 8 * i) & 0xFF));
			}

			System.arraycopy(packetLength, 0, target, 0, 4);
			myCamera.getTime(target, 5);
			target[4] = myCamera.motionDetected() ? (byte) 1 : (byte) 0;
			System.arraycopy(target, 0, image, 0, len + 4);
			m.storeImage(image);

		}
	}

}
