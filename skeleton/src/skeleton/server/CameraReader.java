package skeleton.server;

import se.lth.cs.eda040.fakecamera.*;

public class CameraReader extends Thread {
	Monitor m;
	
	public CameraReader() {
		super();
	}
	
	

	public void run() {
		AxisM3006V myCamera = new AxisM3006V();
		myCamera.init();
		myCamera.connect();
		byte[] target = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		myCamera.getJPEG(target, 0);
		byte[] time = new byte[8];
		myCamera.getTime(time, 0);
		for (int i = 0; i < time.length; i++)
			System.out.println(time[i]);
		myCamera.destroy();
	}

}