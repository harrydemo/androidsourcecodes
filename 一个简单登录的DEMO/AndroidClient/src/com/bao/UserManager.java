package com.bao;

public class UserManager {
	UrlConnectionToServer urlConnectionToServer = new UrlConnectionToServer();
	
	HttpClientToServer httpClientToServer = new HttpClientToServer();
	
	public boolean isRightUser(String username,String password){
		
		//使用urlConnection POST连接服务器
		//String response = urlConnectionToServer.doPost(username, password);
		
		//使用urlConnection Get连接服务器
		//String response = urlConnectionToServer.doGet(username, password);
		
		//使用HttpClientGet连接服务器
		//String response = httpClientToServer.doGet(username, password);
		
		//使用HttpClientPOST连接服务器
		String response = httpClientToServer.doPost(username, password);
		if("true".equals(response)){
			return true;
		}else{
			return false;
		}
		
		
	}

}
