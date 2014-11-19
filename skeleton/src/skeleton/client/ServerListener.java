package skeleton.client;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{
	
	private ServerSocket ss;
	private Socket s;
	private InputStream is;
	private OutputStream os;
	
	public ServerListener(ServerSocket ss){
		this.ss=ss;
		try {
			s = ss.accept();
			is = s.getInputStream();
			os = s.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		
	}

}
