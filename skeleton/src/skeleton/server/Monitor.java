package skeleton.server;

import java.io.InputStream;
import java.io.OutputStream;

public class Monitor {
	private byte[] image;
	private boolean movieMode;
	private int a;
	private InputStream is;
	private OutputStream os;
	private boolean socketReadImage;
	private int imgNbr;
	
	
	public Monitor() {
		imgNbr = 0;
		movieMode = false;
	}
	
	public synchronized void setMovieMode(boolean movie) {
		movieMode = movie;
	}

	public synchronized void storeImage(byte[] image) {
		this.image=image;
		if(!movieMode&&image[0]==(byte)1){
			setMovieMode(true);
		}
		socketReadImage = false;
		imgNbr = (imgNbr+1)%125;
		notifyAll();
	}
	
	public synchronized byte[] getImage() {
		
		while ( (!movieMode || imgNbr != 0) && socketReadImage) {			
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		socketReadImage = true;
		return image;
	}

	
}

