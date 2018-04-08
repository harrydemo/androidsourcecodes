package com.android.tools;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.*;
import java.net.*;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.*;

/*
 * Android的Http通信，轻量级线程 
 * */
public class AndroidHttp extends AsyncTask{

	HttpResponseCallback callback;
	Context context;
	private ProgressDialog progressDlg;
	int responeCode;
	String responeString;

	//构造函数
	public AndroidHttp(Context context1, HttpResponseCallback httpresponsecallback){
		responeCode = 0;
		responeString = "";
		context = context1;
		callback = httpresponsecallback;
	}

	private String convertStreamToString(InputStream inputstream){
		/*BufferedReader bufferedreader;
		StringBuilder stringbuilder;
		InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		bufferedreader = new BufferedReader(inputstreamreader);
		stringbuilder = new StringBuilder();
_L3:
		String s = bufferedreader.readLine();
		String s1 = s;
		if (s1 != null) goto _L2; else goto _L1
_L1:
		inputstream.close();
_L4:
		return stringbuilder.toString();
_L2:
		String s2 = String.valueOf(s1);
		String s3 = (new StringBuilder(s2)).append("\n").toString();
		StringBuilder stringbuilder1 = stringbuilder.append(s3);
		  goto _L3
		IOException ioexception;
		ioexception;
		ioexception.printStackTrace();
		inputstream.close();
		  goto _L4
		printStackTrace();
		  goto _L4
		Exception exception;
		exception;
		inputstream.close();
_L6:
		throw exception;
		printStackTrace();
		if (true) goto _L6; else goto _L5
_L5:
		printStackTrace();
		  goto _L4*/
		return "";
	}

	/*
	 * 此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。
	 * 在执行过程中可以调用publicProgress(Progress…)来更新任务的进度
	 * */
	protected  Object doInBackground(Object aobj[]){
		//获取URL
		String Url = aobj[0].toString();
		//返回的字符串
		String response = "";
		
		//判断URL是否存在
		if (Url != null && Url.length() > 0){
			//创建httpget对象
			HttpGet httpget = new HttpGet(Url);
			//设置包头
			httpget.setHeader("accept", "*/*");
			httpget.setHeader("user-agent", "windows");
			
			DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
			try{
				HttpResponse httpresponse = defaulthttpclient.execute(httpget);
				if (httpresponse.getStatusLine().getStatusCode() == 200){
					responeString = EntityUtils.toString(httpresponse.getEntity(), "UTF-8");
				}
			}
			catch (ClientProtocolException clientprotocolexception){
				responeString = "socket_error";
				clientprotocolexception.printStackTrace();
			}
			catch (IOException ioexception){
				responeString = "socket_error";
				ioexception.printStackTrace();
			}
		}
		
		//socket错误
		else{
			responeString = "socket_error";
		}
		System.out.println("responeString = "+responeString);
		
		
		return responeString;
	}


	//取消函数
	protected void onCancelled(){
		super.onCancelled();
	}

	//此方法在主线程执行，任务执行的结果作为此方法的参数返回
	protected void onPostExecute(Object obj){
		//取消进度条
		progressDlg.dismiss();
		
		//回调函数
		callback.ResponseData(responeString);
		super.onPostExecute(obj);
	}

	//当任务执行之前开始调用此方法，可以在这里显示进度对话框
	protected void onPreExecute(){
		//创建进度条
		progressDlg = new ProgressDialog(context);
		//设置进度条的消息
		progressDlg.setMessage("请稍等...");
		//设置进度条不可取消
		progressDlg.setCancelable(false);
		//显示进度条
		progressDlg.show();
		
		//继承父类函数
		super.onPreExecute();
	}

	//此方法在主线程执行，用于显示任务执行的进度
	protected  void onProgressUpdate(Object aobj[]){
		super.onProgressUpdate(aobj);
	}
}
