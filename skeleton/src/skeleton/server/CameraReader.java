package skeleton.server;

import java.nio.ByteBuffer;

import se.lth.cs.eda040.fakecamera.*;

public class CameraReader extends Thread {
	private ServerMonitor m;
	private AxisM3006V myCamera;
	private final byte DELIMITER = (byte) '\n';
	private final byte ENDBYTE = (byte) '\r';

	public CameraReader(ServerMonitor m) {
		super();
		this.m = m;
		myCamera = new AxisM3006V();

	}

	private void initialize() {

		myCamera.init();
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
