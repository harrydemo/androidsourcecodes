package com.ldci.t56.mobile.tool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ldci.t56.mobile.safe.MainActivity;
import com.ldci.t56.mobile.safe.R;

public class ServiceTool extends Service {
	
	private NotificationManager mNF;
	public static String AUTO_START = "com.ldci.android.t56.mobile.safe.AUTO_START";
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
//	public static int serviceInt = 0;
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
//		Toast.makeText(this, "开启服务，发出通知", Toast.LENGTH_LONG).show();
//		new Thread(new Runnable(){
//			public void run(){
//				while(true){
//					try {
//						Thread.sleep(1000);
//						serviceInt = 0;
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
		autoStartNotification();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		Toast.makeText(this, "停止服务，取消通知", Toast.LENGTH_LONG).show();
		mNF.cancel(R.string.app_name);
	}
	
	private void autoStartNotification(){
		Notification mNotification = new Notification(R.drawable.app_logo,"365手机秘书",System.currentTimeMillis());
		Intent intent = new Intent(this,MainActivity.class);
		intent.setAction(AUTO_START);
		intent.putExtra("auto_start", "boot_completed");
		PendingIntent mPI = PendingIntent.getActivity(this, 0, intent, 0);
		mNotification.setLatestEventInfo(this, "365手机秘书", "成功开机启动", mPI);
		if(null == mNF){
			mNF = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		}
		mNF.notify(R.string.app_name, mNotification);
	}
	
}
