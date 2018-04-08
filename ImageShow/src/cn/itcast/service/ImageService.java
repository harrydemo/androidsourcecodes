package cn.itcast.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageService {
	/**
	 * ��ָ����·���л�ȡͼƬ
	 * @param path ͼƬ·��
	 * @return
	 * @throws Exception
	 */
	public static byte[] getImage(String path) throws Exception{
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
		byte[] data = outStream.toByteArray();//ͼƬ�Ķ���������
		outStream.close();
		inStream.close();
		return data;
	}
}
