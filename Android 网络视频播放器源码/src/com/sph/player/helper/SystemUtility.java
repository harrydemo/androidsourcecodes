package com.sph.player.helper;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import android.os.Environment;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.zip.GZIPInputStream;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SystemUtility
{

	private static int sArmArchitecture = -1;
	protected static String sTempPath = "/data/local/tmp";

	public SystemUtility()
	{
	}

	public static int getArmArchitecture(){
		if (sArmArchitecture == -1) {
			
			try {
				FileInputStream fileinputstream;
				InputStreamReader inputstreamreader;
				BufferedReader bufferedreader;
				fileinputstream = new FileInputStream("/proc/cpuinfo");
				inputstreamreader = new InputStreamReader(fileinputstream);
				bufferedreader = new BufferedReader(inputstreamreader);
				
				String s = "CPU architecture";
				String s1 = "";
				String s2 = "";
				if (s1.compareToIgnoreCase(s) != 0) {
					String as[];
					as = bufferedreader.readLine().split(":");
					if (as.length == 2) {
						s1 = as[0].trim();
						s2 = as[1].trim();
					}
				}
				sArmArchitecture = Integer.parseInt(s2.substring(0, 1));
				bufferedreader.close();
				inputstreamreader.close();
				fileinputstream.close();
				if (sArmArchitecture == -1) {
					sArmArchitecture = 6;
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}
		return sArmArchitecture;
	}

	//获取drawble的id
	public static int getDrawableId(String s){
		int i = -1;
		try {
			Field field = Class.forName(com.sph.player.R.drawable.class.getName())
					.getField(s);
			com.sph.player.R.drawable drawable = new com.sph.player.R.drawable();
			i = field.getInt(drawable);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return i;
	}

	public static String getExternalStoragePath()
	{
		String s;
		if (Environment.getExternalStorageState().equals("mounted"))
			s = Environment.getExternalStorageDirectory().getAbsolutePath();
		else
			s = "/";
		return s;
	}

	public static int getSDKVersionCode()
	{
		return android.os.Build.VERSION.SDK_INT;
	}

	public static int getStringHash(String s)
	{
		byte abyte0[] = s.getBytes();
		int i = 0x4e67c6a7;
		int j = 0;
		do
		{
			int k = abyte0.length;
			if (j >= k)
				return i & 0x7fffffff;
			byte byte0 = abyte0[j];
			int l = (i << 5) + byte0;
			int i1 = i >> 2;
			int j1 = l + i1;
			i ^= j1;
			j++;
		} while (true);
	}

	public static String getTempPath()
	{
		return sTempPath;
	}

	//获取时间字符串
	public static String getTimeString(int i){
		String s;
		if (i < 0){
			Object aobj[] = new Object[0];
			s = String.format("--:--:--", aobj);
		} else{
			int j = i / 1000;
			int k = j / 3600;
			int l = j % 3600;
			int i1 = l / 60;
			int j1 = l % 60;
			Object aobj1[] = new Object[3];
			Integer integer = Integer.valueOf(k);
			aobj1[0] = integer;
			Integer integer1 = Integer.valueOf(i1);
			aobj1[1] = integer1;
			Integer integer2 = Integer.valueOf(j1);
			aobj1[2] = integer2;
			s = String.format("%02d:%02d:%02d", aobj1);
		}
		return s;
	}

	public static Object realloc(Object obj, int i)
	{
		int j = Array.getLength(obj);
		Object obj1 = Array.newInstance(obj.getClass().getComponentType(), i);
		int k = Math.min(j, i);
		if (k > 0)
			System.arraycopy(obj, 0, obj1, 0, k);
		return obj1;
	}

	public static native int setenv(String s, String s1, boolean flag);

	public static boolean simpleHttpGet(String s, String s1){
	/*	HttpResponse httpresponse;
		HttpGet httpget = new HttpGet(s);
		httpget.setHeader("Accept-Encoding", "gzip");
		httpresponse = (new DefaultHttpClient()).execute(httpget);
		if (httpresponse.getStatusLine().getStatusCode() == 200) goto _L2; else goto _L1
_L1:
		boolean flag = false;
_L5:
		return flag;
_L2:
		Object obj;
		FileOutputStream fileoutputstream;
		byte abyte0[];
		HttpEntity httpentity = httpresponse.getEntity();
		Header header = httpentity.getContentEncoding();
		obj = httpentity.getContent();
		if (header != null && header.getValue().equalsIgnoreCase("gzip"))
			obj = new GZIPInputStream(((InputStream) (obj)));
		fileoutputstream = new FileOutputStream(s1);
		abyte0 = new byte[4096];
_L6:
		int i = ((InputStream) (obj)).read(abyte0);
		if (i > 0) goto _L4; else goto _L3
_L3:
		fileoutputstream.close();
		((InputStream) (obj)).close();
		flag = true;
		  goto _L5
_L4:
		fileoutputstream.write(abyte0, 0, i);
		  goto _L6
		IOException ioexception;
		ioexception;
		flag = false;
		  goto _L5*/
		return false;
	}

	static {
		//加载动态库
		System.loadLibrary("vlccore");
	}
}
