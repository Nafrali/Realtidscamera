package skeleton.server;

import java.nio.ByteBuffer;

import se.lth.cs.eda040.fakecamera.*;

public class CameraReader extends Thread {
	private ServerMonitor m;
	private AxisM3006V myCamera;
	private final byte delimiter = (byte)'\n';
	private final byte endbyte = (byte)'\r';
	
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
		
		while(true){
			
		byte[] target = new byte[AxisM3006V.IMAGE_BUFFER_SIZE+12];
		int len = myCamera.getJPEG(target, 11);
		byte[] image = new byte[len+12];
		
		myCamera.getTime(target, 2);
		target[0] = myCamera.motionDetected() ? (byte) 1 : (byte) 0;
		target[1]=delimiter;
		target[10]=delimiter;
		target[len+11]=endbyte;
		System.arraycopy(target, 0, image, 0, len+12);
		m.storeImage(image);
		System.out.println(image[len+11]);
		
		}
	}

}
