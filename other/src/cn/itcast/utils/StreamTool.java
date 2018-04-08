package cn.itcast.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {

	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		inStream.close();
		outStream.close();
		return data;
	}
}
