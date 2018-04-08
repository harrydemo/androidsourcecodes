package com.androidmediaplayer.mp3player.service;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.androidmediaplayer.constant.ConstantExtendsApplication;
import com.androidmediaplayer.model.DownloadTask;
import com.androidmediaplayer.mp3player.RemoteMp3ListActivity;

public class ScanService extends Service {

	private ArrayList<Integer> taskIndexs = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("ScanService onCreate()");
		super.onCreate();
		taskIndexs = new ArrayList<Integer>();
		setScanBrocast();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int taskIndex = intent.getIntExtra("taskIndex", -1);
		taskIndexs.add(taskIndex);
		sendBrocast();
		return super.onStartCommand(intent, flags, startId);
	}

	private ScanSdReceiver scanSdReceiver = null;
	
	/**
	 * 通知系统扫描多媒体文件
	 */
	private void setScanBrocast() {
		IntentFilter intentfilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		scanSdReceiver = new ScanSdReceiver();
		registerReceiver(scanSdReceiver, intentfilter);
	}
	
	private void sendBrocast(){
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	class ScanSdReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
				Iterator<Integer> iter = taskIndexs.iterator();
				while(iter.hasNext()){
					int key = iter.next();
					System.out.println("scan key: "+key);
					RemoteMp3ListActivity.downloadTasks.get(key).setState_download(DownloadTask.STATE_DONE);
				}
				ConstantExtendsApplication cea = ((ConstantExtendsApplication) getApplicationContext());
				cea.setRefreshOnly(true);
				System.out.println("scan complete");
			}
		}
	}

	@Override
	public void onDestroy() {
		System.out.println("ScanService onDestory()");
		unregisterReceiver(scanSdReceiver);
		super.onDestroy();
	}

}
