package skeleton.client;

public class GuiThread extends Thread {
	
	ClientMonitor m;
	GUIt gui;
	
	public GuiThread(ClientMonitor m,GUIt gui) {
		super();
		this.m=m;
		this.gui=gui;
	}
	
	public void run(){
		gui.refreshImage(m.getLatestImage());
		
	}
	
}
