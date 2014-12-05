package skeleton.client;

public class ImageClass {
	private byte[] image;
	private long travelTime, showTime;
	
	public ImageClass(byte[] image, long travelTime, long showTime){
		this.image=image;
		this.travelTime = travelTime;
		this.showTime = showTime;
	}
	
	public byte[] getImage(){
		return image;
	}
	
	public long getTravelTime(){
		return travelTime;
	}
	public long getShowTime(){
		return showTime;
	}

}
