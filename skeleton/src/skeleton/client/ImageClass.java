package skeleton.client;

public class ImageClass {
	private byte[] image;
	private long travelTime;
	
	public ImageClass(byte[] image, long travelTime){
		this.image=image;
	}
	
	public byte[] getImage(){
		return image;
	}
	
	public long getTravelTime(){
		return travelTime;
	}

}
