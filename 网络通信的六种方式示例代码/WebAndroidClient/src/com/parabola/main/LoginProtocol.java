package com.parabola.main; 

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;  

import android.util.Log;
     
public class LoginProtocol {  
	 public static final String TAG = "LoginProtocol";
	 private StringBuilder sb = new StringBuilder();  
	 String string;
     private HttpClient httpClient;  
     private HttpPost httpRequest;  
     private HttpResponse response;  
   
     private List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();  
     
     private final static String URL = "http://182.201.61.175:8080/WebAndroid/saveUserAction";  
     private final static String URL1 = "http://182.201.61.175:8080/ServletTest1/data";
     private final static String URL2 = "http://182.201.61.175:8080/StrAnd/login.action?username=123456";
     
     public boolean checkLogin(User user){
    	 
    	 nameValuePair.add(new BasicNameValuePair("format", "JSON"));  
    	 nameValuePair.add(new BasicNameValuePair("name", user.getName()));  
    	 nameValuePair.add(new BasicNameValuePair("sex", user.getSex()));  
    	 nameValuePair.add(new BasicNameValuePair("age", user.getAge()));  
    	 nameValuePair.add(new BasicNameValuePair("address", user.getAddress()));  
    
         try {   
             
             httpClient = new DefaultHttpClient();  
             httpRequest = new HttpPost(URL);  
             UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8);
             httpRequest.setEntity(entity);  
             response = httpClient.execute(httpRequest);  
              
             //String strResult = EntityUtils.toString(response.getEntity());
             
             if (response.getStatusLine().getStatusCode() == 200) {   
            	 Log.i(TAG, "response code = " + response.getStatusLine().getStatusCode());
//            	 BufferedReader bufferedReader2 = new BufferedReader(  
//                         new InputStreamReader(response.getEntity().getContent()));  
//                 for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2  
//                         .readLine()) {  
//                     sb.append(s);  
//                 }  
            	 
            	 try {
    				InputStream is = response.getEntity().getContent();
    				Reader reader = new BufferedReader(new InputStreamReader(is), 4000);
    				StringBuilder buffer = new StringBuilder((int) response.getEntity().getContentLength());
    				try {
    					char[] tmp = new char[1024];
    					int l;
    					while ((l = reader.read(tmp)) != -1) {
    						buffer.append(tmp, 0, l);
    					}
    				} finally {
    					reader.close();
    				}
    				String string = buffer.toString();
    				Log.i(TAG, string);
    				response.getEntity().consumeContent(); // 释放资源，使这个链接回到连接池管理器中
            	 }finally{
            		 
            	 }
    		 
             //  Log.i(TAG, ""+new JSONObject(sb.toString())); 
             }else{
            	 Log.e(TAG, "error code = " + response.getStatusLine().getStatusCode());
             }
             
             if(string == null){  
                 return false;  
             }else{  
                 return true;  
             }  
         } catch (Exception e) {  
             e.printStackTrace();  
             return false;  
         }  
     }  
   
}  