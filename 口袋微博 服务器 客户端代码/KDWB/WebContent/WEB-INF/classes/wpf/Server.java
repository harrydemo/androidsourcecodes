package wpf;

import java.net.ServerSocket;

public class Server{
	public static void main(String args[]){
		try{
			ServerSocket ss = new ServerSocket(8888);
			ServerThread st = new ServerThread(ss);
			st.start();
			System.out.println("Listening...");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}