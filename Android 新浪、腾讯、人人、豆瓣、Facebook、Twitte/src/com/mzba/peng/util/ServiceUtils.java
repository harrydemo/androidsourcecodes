package com.mzba.peng.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
/**
 * 
 * @author 06peng
 *
 */
public class ServiceUtils {
	
	public static Drawable drawable = null; 
	public static URL url;
	
	public static boolean debugFlag = true;

	/**
	 * 根据图片路径下载图片并转化成Bitmap
	 * @param path
	 * @return
	 */
	public static Bitmap downloadImage(String path){
		 Bitmap bitmap = null;
		 try {
			URL uri = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)uri.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5 * 1000);
			if(conn.getResponseCode() == 200){
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	 }
	
	
	/**
	 * 读取输入流数据
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream is) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		is.close();
		return os.toByteArray();
	}
	
	/**
	 * 根据图片途径下载图片转化成二进制数据
	 * @param urlPath
	 * @return
	 */
	public static byte[] getImageFromUrlPath(String urlPath) {
		byte[] data = null;
		URL url;
		try {
			url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5 * 1000);
			if(conn.getResponseCode() == 200){
				InputStream is = conn.getInputStream();
				data = readStream(is);
			}else{
				System.out.println("请求失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	/**
	 * 将Drawable转化为Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * 下载图片到SD卡
	 * @param imagePath
	 */
	public static void downLoadImage(String imageUrl, String path){
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		File dir = new File(Environment.getExternalStorageDirectory() + "/" + path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir + File.separator + fileName);
		if (!file.exists()) {
			System.out.println("**********************>>>" + "download image");
			URL url;
			try {
				url = new URL(imageUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setReadTimeout(5 * 1000);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					byte[] data = readStream(is);

					FileOutputStream fs = new FileOutputStream(file);
					fs.write(data);
					fs.close();
				} else {
					System.out.println("请求失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取用户头像
	 * @param imagePath
	 */
	public static Bitmap getUserHeadImage(String imageUrl, String path){
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		File dir = new File(Environment.getExternalStorageDirectory() + "/" + path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir + File.separator + fileName);
		if (!file.exists()) {
			URL url;
			try {
				url = new URL(imageUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setReadTimeout(5 * 1000);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					byte[] data = readStream(is);

					FileOutputStream fs = new FileOutputStream(file);
					fs.write(data);
					fs.close();
				} else {
					System.out.println("请求失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return BitmapFactory.decodeFile(file.getPath());
	}
	
	/**
	 * 获取文件
	 * @param imageUrl
	 * @param path
	 * @return
	 */
	public static byte[] getImageFile(String imageUrl, String path) {
		String imagePath = Environment.getExternalStorageDirectory() + "/" + path;
		File file = new File(imagePath + File.separator + imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
		if (file == null || !file.exists()) {
			return null;
		}
		int length = (int) file.length();
		byte[] b = new byte[length];
		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(b, 0, length);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return length != 0 ? b : null;
	}
	
    public static boolean isConnectInternet(Activity activity) {
		ConnectivityManager conManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
        return false;
	 }
    public static boolean isConnectInternet(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
        return false;
	 }
    
    public static String getContentByStream(InputStream is, long contentLength) {
    	String content = null;
    	Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder((int)contentLength);
			char[] temp = new char[4000];
			int len = 0;
			while((len = reader.read(temp)) != -1){
				builder.append(temp, 0, len);
			}
			content = builder.toString();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				is.close();
				reader = null;
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
    }
    
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
    public static Bitmap bytesToBitmap(byte[] data) {
		if (data != null) {
			ByteArrayInputStream is = new ByteArrayInputStream(data);
			try {
				BitmapFactory.Options options1 = new BitmapFactory.Options();
				options1.inPurgeable = true;
			    options1.inInputShareable = true;
			    options1.inSampleSize = 1;
			    try {
					BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options1, true);
			    }catch(Exception ex) {
			        ex.printStackTrace();
			    }
				Bitmap bitmap = (new WeakReference<Bitmap>(
						BitmapFactory.decodeStream(is, null, options1))).get();
				Bitmap temp = Bitmap.createBitmap(bitmap);
				return temp;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} 
		return null;
	}
    
	public static void dout(String str) {
		if (debugFlag) {
			Log.d("[dout]", "str>>>>>>>>>>>>>" + str);
		}
	}

	public static void dout(String str, String str2) {
		if (debugFlag) {
			Log.d("[dout]", "str>>>>>>>>>>>>>" + str + " " + str2);
		}
	}
    
}
