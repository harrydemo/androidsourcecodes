package fank.android.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

public class HttpUtils {

	/**�ر�URLConnection��InputStreame��OutputStream*/
	public static void close(URLConnection conn) {
		try {
			if (conn != null) {
				InputStream is = conn.getInputStream();
				OutputStream out = conn.getOutputStream();
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**�ر�InputStreame*/
	public static void close(InputStream is) {
		try {
			if(is!=null){
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**�ر�OutputStream*/
	public static void close(OutputStream out) {
		try {
			if(out!=null){
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
