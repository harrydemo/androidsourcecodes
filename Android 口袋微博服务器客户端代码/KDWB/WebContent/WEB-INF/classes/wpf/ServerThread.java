package wpf;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread{
	public ServerSocket ss;		//声明ServerSocket对象
	public boolean flag = false;
	
	public ServerThread(ServerSocket ss){	//构造器
		this.ss = ss;	
		flag = true;
	}
	public void run(){
		while(flag){
			try{
				Socket socket = ss.accept();
				ServerAgent sa = new ServerAgent(socket);
				sa.start();
			}
			catch(SocketException se){
				try{
					ss.close();
					ss = null;
					System.out.println("ServerSocket closed");
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
