package com.bao;

public class UserManager {
	UrlConnectionToServer urlConnectionToServer = new UrlConnectionToServer();
	
	HttpClientToServer httpClientToServer = new HttpClientToServer();
	
	public boolean isRightUser(String username,String password){
		
		//ʹ��urlConnection POST���ӷ�����
		//String response = urlConnectionToServer.doPost(username, password);
		
		//ʹ��urlConnection Get���ӷ�����
		//String response = urlConnectionToServer.doGet(username, password);
		
		//ʹ��HttpClientGet���ӷ�����
		//String response = httpClientToServer.doGet(username, password);
		
		//ʹ��HttpClientPOST���ӷ�����
		String response = httpClientToServer.doPost(username, password);
		if("true".equals(response)){
			return true;
		}else{
			return false;
		}
		
		
	}

}
