package gl.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageService {
	
	public static byte[] getImage(String path) throws Exception{
		
		URL url = new URL(path);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		
		conn.setConnectTimeout(10*1000);
		
		InputStream inStream =conn.getInputStream();
		
		
		
		return readInputStream(inStream);
		
	}

	private static byte[] readInputStream(InputStream inStream) throws Exception {
		// TODO Auto-generated method stub
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024];
		
		int len = 0;
		
		while((len = inStream.read(buffer)) != -1){
			
			outStream.write(buffer, 0, len);
			
		}
		
		inStream.close();
		return  outStream.toByteArray();
	}

}
