package wpf;
import java.net.ServerSocket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class MyServletContextListener implements ServletContextListener{
	ServerSocket ss = null;//声明ServerSocket对象
	ServerThread st = null;//声明ServerThread对象
	public void contextInitialized(ServletContextEvent sce){
		try{
			ss = new ServerSocket(8888);
			st = new ServerThread(ss);
			st.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void contextDestroyed(ServletContextEvent sce){
		try{
			st.flag = false;   
			ss.close();
			ss = null;
			st = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}