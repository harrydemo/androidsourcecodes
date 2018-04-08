package cn.itcast.net.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String path = "http://i6.17173.itc.cn/2010/news/chinajoy/2010/mm/small01/s0722cyfs01.jpg";
		URL url = new URL(path);
		//get //post
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5*1000);
		InputStream inStream = conn.getInputStream();
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = inStream.read(buffer)) !=-1 ){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();//图片的二进制数据
		
		File file = new File("s0722cyfs01.jpg");
		FileOutputStream fileoutStream = new FileOutputStream(file);
		fileoutStream.write(data);
		fileoutStream.close();
	}

}
