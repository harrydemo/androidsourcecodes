
package com.android.ui;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sph.player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class LoadActivity extends Activity
{
	//AsyncTask-轻量级线程 HttpBandWidthTask-为http通信类,进行版本以及带宽检测
	public class HttpBandWidthTask extends AsyncTask{

		//结束事件
		private long EndTime;
		//开始时间
		private long StartTimeNew;
		//开始事件标识
		boolean bandwidthStartTimeFlag;
		//图片长度
		long imagelen;
		//所属的activity
		final LoadActivity this$0;

		/*
		 * 此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。
		 * 在执行过程中可以调用publicProgress(Progress…)来更新任务的进度
		 * */
		protected  Object doInBackground(Object aobj[]){
			//转换成字符串
			String s = aobj[0].toString();
			InputStream inputstream = null;
			HttpURLConnection httpurlconnection = null;
			Object obj = null; 
			
			
			try {
				if(s.compareTo("http://m.vfun.tv:8080/android_version.php") == 0){
					System.out.println("s.compareTo(http://m.vfun.tv:8080/android_version.php) == 0");
					
					//vertion-information
					flag = false;
					String s13 = "";
					//创建URL对象
					URL url2 = new URL("http://m.vfun.tv:8080/android_version.php");
					//获取httpurlConnect对象
					httpurlconnection = (HttpURLConnection) url2.openConnection();
					//设置超时
					httpurlconnection.setConnectTimeout(10000);
					//设置请求的方法
					httpurlconnection.setRequestMethod("GET");
					//获取响应码
					int code = httpurlconnection.getResponseCode();
					
					//读取数据
					if (code == 200) {
						//获取输入流
						inputstream = httpurlconnection.getInputStream();
						
						//读取数据
						InputStreamReader inputReader = new InputStreamReader(inputstream);   
	                    BufferedReader reader = new BufferedReader(inputReader);   
	                    String inputLine = null;   
	                    StringBuffer sb = new StringBuffer();   
	                    while ((inputLine = reader.readLine()) != null) {   
	                        sb.append(inputLine).append("\n");   
	                    }   
	                    obj = sb.toString();
	                    
	                    reader.close();
	                    inputReader.close();
	                    inputstream.close();
	                    httpurlconnection.disconnect();
					}
				}
				else{
					flag = true;
					//image-information，get-bandwidth
					URL url = new URL("http://m.vfun.tv:8080/200k.jpg");
					//获取httpurlConnect对象
					httpurlconnection = (HttpURLConnection) url.openConnection();
					//设置超时
					httpurlconnection.setConnectTimeout(10000);
					//设置请求的方法
					httpurlconnection.setRequestMethod("GET");
					//获取响应码
					int code = httpurlconnection.getResponseCode();

					if (code == 200) {
						//获取输入流
						inputstream = httpurlconnection.getInputStream();
						long l = httpurlconnection.getContentLength();
						imagelen = l;
						long l1 = 0L;
						EndTime = l1;
						
						if (imagelen == 65535L) {
							obj = "socket_error";
							return obj;

						} else {
							int k = 0;
							while (true) {
								byte abyte0[] = new byte[5120];
								int i1 = inputstream.read(abyte0);

								if (i1 <= 0) {
									break;
								} else {
									k += i1;
									if (bandwidthStartTimeFlag) {
										boolean flag1 = false;
										bandwidthStartTimeFlag = flag1;
										long l5 = System.nanoTime();
										StartTimeNew = l5;
										LoadActivity loadactivity = LoadActivity.this;
										long l6 = imagelen;
										long l7 = k;
										long l8 = l6 - l7;
										loadactivity.actualBytes = l8;
									}
									double d2 = k;
									double d3 = imagelen;
									double d4 = d2 / d3;
									LoadActivity loadactivity1 = LoadActivity.this;
									int k1 = (int) (0F * d4);
									loadactivity1.percent = k1;
									Object aobj1[] = new Object[1];
									String s6 = String.valueOf(percent);
									String s7 = (new StringBuilder(s6)).append("%")
											.toString();
									aobj1[0] = s7;
									HttpBandWidthTask httpbandwidthtask = this;
									Object aobj2[] = aobj1;
									httpbandwidthtask.publishProgress(aobj2);
									String s8 = TAGMAIN;
									String s9 = String.valueOf(k);
									String s10 = (new StringBuilder(s9)).append(
											"====").toString();
									int i2 = Log.d(s8, s10);
								}
							}
							long l2 = System.nanoTime();
							EndTime = l2;
							double d = imagelen;
							long l3 = EndTime;
							long l4 = StartTimeNew;
							double d1 = (l3 - l4) / 0xf4240L;
							String s5 = String.valueOf((int) (d / d1));
							obj = (new StringBuilder(s5)).toString();
							
							inputstream.close();
			                httpurlconnection.disconnect();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				obj = "socket_error";
				return obj;
			}
			return obj;

		}

		//取消方法
		protected void onCancelled(){
			super.onCancelled();
		}

		//此方法在主线程执行，任务执行的结果作为此方法的参数返回
		protected void onPostExecute(Object obj){
			//取消进度条
			progressDlg.dismiss();
			
			LoadActivity loadactivity = LoadActivity.this;
			//转换成字符串
			String s = obj.toString();
			//调用加载activity中的回调函数
			loadactivity.CallBack(s);
			//调用父类的构造函数
			super.onPostExecute(obj);
		}

		//当任务执行之前开始调用此方法，可以在这里显示进度对话框
		protected void onPreExecute(){
			//判断是否存在进度条并在显示，如果有则取消此进度条并销毁
			if (progressDlg != null && progressDlg.isShowing()){
				progressDlg.dismiss();
				progressDlg = null;
			}
			
			LoadActivity loadactivity = LoadActivity.this;
			LoadActivity loadactivity1 = LoadActivity.this;
			//创建进度条对象
			ProgressDialog progressdialog = new ProgressDialog(LoadActivity.this);
			//设置加载activity中的进度条
			loadactivity.progressDlg = progressdialog;
			//根据开始时间标识当前状态
			if (flag){
				progressDlg.setMessage("检测带宽,请稍等...");
			}
			else{
				progressDlg.setMessage("版本检测");
			}
			//设置不可取消
			progressDlg.setCancelable(false);
			//显示进度条
			progressDlg.show();
			//继承父类函数
			super.onPreExecute();
		}

		//此方法在主线程执行，用于显示任务执行的进度
		protected  void onProgressUpdate(Object aobj[]){
			//更新进度条
			if (flag){
				String s = aobj[0].toString();
				ProgressDialog progressdialog = progressDlg;
				String s1 = (new StringBuilder("正在测试: ")).append(s).toString();
				progressdialog.setMessage(s1);
			}
			super.onProgressUpdate(aobj);
		}

		//构造函数
		public HttpBandWidthTask(){
			super();
			//相关变量初始化
			this$0 = LoadActivity.this;
			imagelen = 0L;
			StartTimeNew = 0L;
			EndTime = 0L;
			bandwidthStartTimeFlag = true;
		}
	}


	static final String LoadImageURL = "http://m.vfun.tv:8080/200k.jpg";
	public static final String PREF = "sph_version";
	public static final String VERSION = "version";
	static final String VLC_APK_NAME = "VLC-debug.apk";
	static final String VLC_APK_URI = "http://61.151.247.60:8080/VLC-debug.apk";
	static final String VLC_downLoadAPK_Path = "/sdcard/";
	static final String VLC_packagePath = "com.btn.videolan.android.plugin";
	private int ConnCurTimes;
	private final int ConnTimes = 3;
	Thread ConnectThread;
	private ImageView LoadImage;
	private final String TAGMAIN;
	long actualBytes;
	private AlertDialog alertDialog;
	double bandwidth;
	int downLoadFileSize;
	int fileSize;
	String filename;
	private boolean flag;
	int percent;
	private ProgressDialog progressDlg;
	Thread tread;

	public LoadActivity()
	{
		String s = "com.android.ui.LoadActivity.getSimpleName()";
		TAGMAIN = s;
		LoadImage = null;
		bandwidth = 0D;
		fileSize = 0;
		downLoadFileSize = 0;
		filename = "";
		actualBytes = 0L;
		ConnCurTimes = 0;
		percent = 0;
		flag = false;
		alertDialog = null;
	}

	//网络连接错误对话框
	private void ConnectErrorDialog(){
		//创建对话框对象 setIcon--设置图标 setTitle-设置标题 setMessage-设置消息
		android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(this)).
			setIcon(R.layout.load_image).setTitle(R.string.band_width).setMessage(R.string.band_width_msg);
		
		//setPositiveButton--设置按钮,设置了按钮的点击事件监听器 show--显示对话框
		AlertDialog alertdialog = builder.setPositiveButton(R.string.button_confirm, 
				new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialoginterface, int i){
				HiddleDialgo();
			}
		}).show();
		alertDialog = alertdialog;
	}

	private void GotoDownloadDialgo()
	{
		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
			alertDialog = null;
			finish();
			Intent intent = new Intent();
			Intent intent1 = intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://m.vfun.tv:8080/vfun/sph.apk");
			Intent intent2 = intent.setData(uri);
			startActivity(intent);
		}
	}

	//进入主界面-参数为当前检测的带宽
	private void GotoMainActivity(int i){
		//获取带宽参数，并答应相关信息
		String s = TAGMAIN;
		String s1 = (new StringBuilder("bandwidth=")).append(i).toString();
		int j = Log.d(s, s1);
		
		//通过Intent进行activity切换，进入主界面
		Intent intent = new Intent();
		Intent intent1 = intent.setClass(this, TypeActivity.class);
		
		//计算带宽
		int k = i * 8;
		if (k == 0){
			k = 80;
		}
		String s2 = String.valueOf(k);
		String s3 = (new StringBuilder(s2)).toString();
		//保存带宽
		SaveBandWidth(s3);
		
		//activity跳转，进入主UI，分类界面
		startActivityForResult(intent, 0);
	}

	private void HiddleDialgo()
	{
		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
			alertDialog = null;
			finish();
		}
	}

	private String ReadVersion(){
		String s = getSharedPreferences("sph_version", 0).getString("version", "");
		String s1;
		if (s.compareTo("") == 0)
		{
			boolean flag1 = getSharedPreferences("sph_version", 0).edit().putString("version", "1.0.1").commit();
			s1 = "1.0.1";
		} else
		{
			s1 = s;
		}
		return s1;
	}

	//保存带宽函数
	private void SaveBandWidth(String s){
		boolean flag1 = getSharedPreferences("faplayer_prefer", 0).edit().
			putString("band_width", s).commit();
	}

	//获取连接管理服务
	private ConnectivityManager getConnectManager(){
		return (ConnectivityManager)getSystemService("connectivity");
	}

	private boolean isAvilible(Context context, String s){
		List list;
		ArrayList arraylist;
		list = context.getPackageManager().getInstalledPackages(0);
		arraylist = new ArrayList();
		
		if (list == null) {
			return arraylist.contains(s);
		} else {
			for(int i = 0; i < list.size(); i++){
				String s1 = ((PackageInfo)list.get(i)).packageName;
				 arraylist.add(s1);
			}
			return true;
		}
	}

	public boolean APK_IsExists()
	{
		boolean flag1 = false;
		if (SD_IsExists())
		{
			if (filename.length() <= 0)
				filename = "VLC-debug.apk";
			StringBuilder stringbuilder = new StringBuilder();
			File file = Environment.getExternalStorageDirectory();
			StringBuilder stringbuilder1 = stringbuilder.append(file).append("/");
			String s = filename;
			String s1 = stringbuilder1.append(s).toString();
			flag1 = (new File(s1)).exists();
		}
		return flag1;
	}

	//回调函数，线程执行后回调用才回调函数
	public void CallBack(String s){
		if (!flag) {
			//判断是否socket错误
			if (s.contains("socket_error")){
				//再次执行线程，直到3次错误
				if (ConnCurTimes < 3){
					int k = ConnCurTimes + 1;
					ConnCurTimes = k;
					HttpBandWidthTask httpbandwidthtask1 = new HttpBandWidthTask();
					Object aobj1[] = new Object[1];
					aobj1[0] = "http://m.vfun.tv:8080/200k.jpg";
					AsyncTask asynctask1 = httpbandwidthtask1.execute(aobj1);
				} else{
					//连接错误对话框
					ConnectErrorDialog();
				}
			}
			//
			else{
				String s1 = s.trim();
				HttpBandWidthTask httpbandwidthtask2 = new HttpBandWidthTask();
				Object aobj2[] = new Object[1];
				aobj2[0] = "http://m.vfun.tv:8080/200k.jpg";
				AsyncTask asynctask2 = httpbandwidthtask2.execute(aobj2);
			}
		} else{
			//socket错误
			if (s.contains("socket_error")){
				//如果错误次数小于3，则再次启动线程去检测带宽
				if (ConnCurTimes < 3){
					int i = ConnCurTimes + 1;
					ConnCurTimes = i;
					HttpBandWidthTask httpbandwidthtask = new HttpBandWidthTask();
					Object aobj[] = new Object[1];
					aobj[0] = "http://m.vfun.tv:8080/200k.jpg";
					AsyncTask asynctask = httpbandwidthtask.execute(aobj);
				} else{
					//连接错误对话框
					ConnectErrorDialog();
				}
			}
			//带宽检测通过，进入主activity
			else{
				int j = Integer.parseInt(s);
				//进入主activity
				GotoMainActivity(j);
			}
		}
	}

	//关闭activity
	public void CloseActivity(){
		finish();
	}

	public void InstallApk()
	{
		StringBuilder stringbuilder = new StringBuilder();
		File file = Environment.getExternalStorageDirectory();
		StringBuilder stringbuilder1 = stringbuilder.append(file).append("/");
		String s = filename;
		String s1 = stringbuilder1.append(s).toString();
		Intent intent = new Intent("android.intent.action.VIEW");
		Uri uri = Uri.fromFile(new File(s1));
		Intent intent1 = intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
	}

	//设置保持屏幕一直活动，不进入休眠状态
	public void KeepScreenOn(){
		//获取window组件
		Window window = getWindow();
		//设置window的属性，保存屏幕一直活动
		if ((window.getAttributes().flags & 0x80) == 0){
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	public boolean SD_IsExists()
	{
		return Environment.getExternalStorageState().equals("mounted");
	}

	protected void onActivityResult(int i, int j, Intent intent)
	{
		finish();
	}

	/*
	 * activity创建方法
	 * */
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		//保存屏幕一直活动，不进入休眠状态
		KeepScreenOn();
		//设置布局文件--加载图片布局文件
		setContentView(R.layout.load_image);
		
		//获取Wifi的状态
		boolean flag1 = getConnectManager().getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		//获取手机网络状态
		boolean flag2 = getConnectManager().getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
		
		//判断是否具有正常网络状态
		if (!flag1 && !flag2){
			//创建对话框对象 setIcon--设置图标 setTitle-设置标题 setMessage-设置消息
			android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(this)).
				setIcon(R.drawable.picloading).setTitle(R.string.network_apn_status).
				setMessage(R.string.network_apn_content_noteinfo);
			
			//setPositiveButton--设置按钮,设置了按钮的点击事件监听器 show--显示对话框
			AlertDialog alertdialog = builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialoginterface, int i){
					//关闭activity
					CloseActivity();
				}
			}).show();
		}
		//网络正常
		else{
			HttpBandWidthTask httpbandwidthtask = new HttpBandWidthTask();
			Object aobj[] = new Object[1];
			aobj[0] = "http://m.vfun.tv:8080/android_version.php";
			AsyncTask asynctask = httpbandwidthtask.execute(aobj);
		}
	}

	protected void onDestroy()
	{
		super.onDestroy();
	}

	protected void onPause()
	{
		super.onPause();
	}

	protected void onRestart()
	{
		super.onRestart();
	}

	protected void onResume()
	{
		super.onResume();
	}

	protected void onStart()
	{
		super.onStart();
	}

	protected void onStop()
	{
		super.onStop();
	}


}
