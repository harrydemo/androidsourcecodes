package weibo4andriod.examples;

import java.io.*;
import java.net.*;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.http.ImageItem;


public class Upload {
	public static void main(String args[]){
	try{
	
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	Weibo weibo = new Weibo();
        
        weibo.setToken(args[0], args[1]);
        
        try{
        	byte[] content= readFileImage(args[2]);
        	System.out.println("content length:" + content.length);
        	ImageItem pic=new ImageItem("pic",content);
        	String url1=java.net.URLEncoder.encode("穿梭，时光。回到，最初。然后，穿越。在穿越，穿越，回到500年前，大闹天宫。 ","UTF-8");
        	System.out.println(""+url1);
        	Status status=weibo.uploadStatus(url1, pic);
        	System.out.println("Successfully upload the status to ["
        			+status.getText()+"].");
            }
             catch(Exception e1){
             	e1.printStackTrace();
            }
	    }catch(Exception ioe){
        	System.out.println("Failed to read the system input.");
	    }
	}
public static byte[] readFileImage(String filename)throws IOException{
		BufferedInputStream bufferedInputStream=new BufferedInputStream(
				new FileInputStream(filename));
		int len =bufferedInputStream.available();
		byte[] bytes=new byte[len];
		int r=bufferedInputStream.read(bytes);
		if(len !=r){
			bytes=null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}
}
     
        
        	
        
