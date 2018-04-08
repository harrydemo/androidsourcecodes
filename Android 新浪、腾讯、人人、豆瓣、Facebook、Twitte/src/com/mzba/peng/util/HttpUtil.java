package com.mzba.peng.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/**
 * Http连接的相关方法
 * @author 林乐鹏
 * @version 2011-05-26
 * 
 */
public class HttpUtil {

	/**
	 * 发送http请求
	 * @param urlPath 请求路径
	 * @param requestType 请求类型
	 * @param requestParams 请求参数，如果没有参数，则为null
	 * @return
	 */
	public static String sendRequest(String urlPath, String requestType, String requestParams) {
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
		String result = null;
		try {
			url = new URL(urlPath);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(requestType);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(10 * 1000);
			conn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			conn.setRequestProperty("Connection", "keep-alive");
			os = conn.getOutputStream();
			if (requestParams != null) {
				os.write(requestParams.getBytes());
				os.flush();
			}
			if (200 == conn.getResponseCode()) {
				is = conn.getInputStream();
				byte[] temp = readStream(is);
				result = new String(temp);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static byte[] readStream(InputStream is) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while((len = is.read(buffer)) != -1){
			os.write(buffer,0,len);
		}
		is.close();
		return os.toByteArray();
	}

	/**
	 * 通过URL获取网络位图
	 * 
	 * @param url
	 * @return
	 */
	public static BitmapDrawable getImage(String path) {
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BitmapDrawable bmpDrawable = null;
		HttpURLConnection conn = null;
		
		try {
			conn = (HttpURLConnection) url.openConnection();
			bmpDrawable = new BitmapDrawable(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) conn.disconnect();
		}
		
		return bmpDrawable;
	}
	
	/**
	 * 根据图片路径下载图片并转化成Bitmap
	 * @param path
	 * @return
	 */
	public static Bitmap downImage(String path){
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
	 * 下载图片到SD卡
	 * @param imagePath
	 */
	public static void downLoadImageToSD(String imagePath){
		URL url;
		try {
			url = new URL(imagePath);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5 * 1000);
			if(conn.getResponseCode() == 200){
				InputStream is = conn.getInputStream();
				byte[] data = readStream(is);
				File file = getFile(imagePath);
				FileOutputStream fs = new FileOutputStream(file);
				fs.write(data);
				fs.close();
			}else{
				System.out.println("请求失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文件名在SD卡创建目录
	 * @param path
	 * @return
	 */
	public static File getFile(String path){
		String sdPath =  Environment.getExternalStorageDirectory() + "/";
		File dir = new File(sdPath + "mobi/download");
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(dir + File.separator + path.substring(path.lastIndexOf("/") + 1));
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * 判断是否存在网络问题
	 * @param activity
	 * @return
	 */
	public static boolean isConnectInternet(Activity activity) {
        ConnectivityManager conManager=(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null){  
        	return networkInfo.isAvailable();
        }
        return false;
	 }
	
	public static interface HttpRequestErrorHandler {
		 void handleError(int actionId, Exception e);
	}
}
