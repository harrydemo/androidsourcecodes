package com.bao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlConnectionToServer {
	
	String urlAddress = "http://10.0.0.10:8080/AndroidServer/login.do";
	URL url;
	HttpURLConnection uRLConnection;
	public UrlConnectionToServer(){
		
	}
	
	public String doGet(String username,String password){
		
		String getUrl = urlAddress + "?username="+username+"&password="+password;
		try {
			url = new URL(getUrl);
			uRLConnection = (HttpURLConnection)url.openConnection();
			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while((readLine =br.readLine()) != null){
				//response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String doPost(String username,String password){
		try {
			url = new URL(urlAddress);
			uRLConnection = (HttpURLConnection)url.openConnection();
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			uRLConnection.connect();
			
			DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
			String content = "username="+username+"&password="+password;
			out.writeBytes(content);
			out.flush();
			out.close();
			
			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while((readLine =br.readLine()) != null){
				//response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
