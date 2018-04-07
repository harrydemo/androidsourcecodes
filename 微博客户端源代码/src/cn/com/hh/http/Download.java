package cn.com.hh.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Download {
	/**
	 *  从网络上下载图片资源
	 * @param imgPath
	 * @return
	 */
	public Bitmap DownloadImg(String imgPath){
		Bitmap bmp = null;
		try {
			URL imgUrl = new URL(imgPath);
			//打开连接
			URLConnection con = imgUrl.openConnection();
			InputStream in = con.getInputStream();
			bmp = BitmapFactory.decodeStream(in);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}
}
