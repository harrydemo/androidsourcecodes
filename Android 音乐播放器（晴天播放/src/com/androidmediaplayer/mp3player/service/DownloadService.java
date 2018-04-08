package com.androidmediaplayer.mp3player.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.androidmediaplayer.R;
import com.androidmediaplayer.constant.AppConstant;
import com.androidmediaplayer.model.DownloadTask;
import com.androidmediaplayer.model.Mp3Info;
import com.androidmediaplayer.model.Progress;
import com.androidmediaplayer.mp3player.MainActivity;
import com.androidmediaplayer.mp3player.RemoteMp3ListActivity;
import com.androidmediaplayer.utils.DownloadTaskThread;
import com.androidmediaplayer.utils.FileUtil;
import com.androidmediaplayer.utils.HttpDownloaderUtils;

public class DownloadService extends Service {

	private final int base_notificationId = android.R.drawable.stat_sys_download;
	private static int notificationId;
	private HashMap<Integer, ArrayList<Thread>> tasks = null;
	private HashMap<Integer, Runnable> notiThreads = null;
	private NotificationManager notificationManager = null;
	private static boolean flag = true;
	private HashMap<Integer, String> mp3Names = null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationId = android.R.drawable.stat_sys_download;
		tasks = new HashMap<Integer, ArrayList<Thread>>();
		notiThreads = new HashMap<Integer, Runnable>();
		mp3Names = new HashMap<Integer, String>();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int temp = -1;
		if((temp = intent.getIntExtra("taskIndex", -1)) >= 0){
			if(RemoteMp3ListActivity.downloadTasks.get(temp).getState_download() == DownloadTask.STATE_DOWNLOADING){
				stopTask(temp, mp3Names.get(temp));
			} else if(RemoteMp3ListActivity.downloadTasks.get(temp).getState_download() == DownloadTask.STATE_DONE){
				cancelNotification(base_notificationId + temp);
			}
		}else{
			go(intent);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void go(Intent intent){
		Mp3Info mp3Info = (Mp3Info) (intent.getSerializableExtra("mp3Info"));
		DownloadTask downloadTask = (DownloadTask) (intent.getSerializableExtra("downloadTask"));
		mp3Names.put(downloadTask.getTaskIndex(), mp3Info.getMp3Name()+".mp3");
		
		//组拼Url路径
		String mp3UrlPath = AppConstant.URL.BASE_URL + mp3Info.getAudio_id();
//		String mp3UrlPath = "http://dc242.4shared.com/img/574783691/fd70ebaf/dlink__2Fdownload_2F6Tw8n866_3Ftsid_3D20110419-211003-f5a6cf32/preview.mp3";
		//进度类
		Progress pg = new Progress();
		//启动下载任务
		startDownloadTask(pg, mp3UrlPath, mp3Info.getMp3Name(), mp3Info.getArtist(), downloadTask);
		//发送下载情况广播
		sendDownLoadMsg(AppConstant.PlayerMsg.ADDTODOWNLOADLIST_MSG);
		//获取歌词下载路径
		String lrcUrl = AppConstant.URL.BASE_URL + mp3Info.getLrcName();
		// 启动歌词下载线程
		new Thread(new DownloadLrcThread(lrcUrl, "mp3", mp3Info.getMp3Name())).start();
	}
	
	/**
	 * 启动下载任务
	 * @param pg
	 * @param fileUrlPath
	 * @param fileName
	 */
	private void startDownloadTask(Progress pg, String fileUrlPath, String finalFileName, String artist, DownloadTask downloadTask){
		//启动通知栏进度条更新线程
		Runnable r = new UpdateNotificationProgress(pg, finalFileName, artist, notificationId, downloadTask);
		Thread t = new Thread(r);
		notiThreads.put(notificationId, r);
		t.start();
		//暂时这样..
		ArrayList<Thread> threads = new ArrayList<Thread>();
		tasks.put(downloadTask.getTaskIndex(), threads);
		// 启动下载任务线程
		Thread t2 = new Thread(new DownloadTaskThread(pg, fileUrlPath, "mp3", finalFileName, AppConstant.PlayerMsg.THREADNUM, downloadTask.getTaskIndex(), threads));
		threads.add(t2);
		t2.start();
		//通知栏ID
		notificationId++;
	}
	
	/**
	 * 终止某个任务
	 * @param takIndex
	 */
	private void stopTask(int taskIndex, String fileName){
		ArrayList<Thread> threads = tasks.get(taskIndex);
		int threadsSize = threads.size();
		for(int i=0; i<threadsSize; i++){
			threads.get(i).interrupt();
		}
		UpdateNotificationProgress unp = (UpdateNotificationProgress)notiThreads.get(base_notificationId + taskIndex);
		unp.stopThreadFlag = false;
		tasks.remove(taskIndex);
		cancelNotification(base_notificationId + taskIndex);
		try {
			FileUtil.deleteFile("mp3", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取消通知
	 * @param notoId
	 */
	private void cancelNotification(int notoId){
		notificationManager.cancel(notoId);
	}
	
	 /**
	  * 发送下载广播
	  * @param msg
	  */
	private void sendDownLoadMsg(int msg) {
		Intent dlIntent = new Intent();
		dlIntent.setAction(AppConstant.Action.DOWNLOAD_MESSAGE_ACTION);
		dlIntent.putExtra("downLoadMsg", msg);
		sendBroadcast(dlIntent);
	}

	class UpdateNotificationProgress implements Runnable {
		
		private Notification notification = null;
		private RemoteViews remoteView = null;
		private Progress pg = null;
		private String fileName = null;
		private String artist = null;
		private int notificationId = 0;
		private DownloadTask downloadTask = null;
		private boolean hadStartService = false;
		
		/**
		 * false为停止该线程
		 */
		public boolean stopThreadFlag = true;
		
		public UpdateNotificationProgress(Progress pg, String fileName, String artist, int notificationId, DownloadTask downloadTask){
			this.pg = pg;
			this.fileName = fileName;
			this.artist = artist;
			this.notificationId = notificationId;
			this.downloadTask = downloadTask;
		}
		
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				System.out.println(pg.getPercent());
				remoteView.setProgressBar(R.id.pb, 100, pg.getPercent(), false);
				remoteView.setTextViewText(R.id.tv, pg.getPercent() + "%");
				notification.contentView = remoteView;
				if(flag && stopThreadFlag){
				    notificationManager.notify(notificationId, notification);
				}
				super.handleMessage(msg);
			}
		};
		
		@Override
		public void run() {
			notification = new Notification();
			remoteView = new RemoteViews(getPackageName(), R.layout.mediaplayer_downloading_notification_320_480);
			Intent intent = new Intent(DownloadService.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("downloadTask", downloadTask);
			PendingIntent pendingIntent = PendingIntent.getActivity(DownloadService.this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.icon = android.R.drawable.stat_sys_download;
			notification.contentIntent = pendingIntent;
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			remoteView.setImageViewResource(R.id.image, android.R.drawable.stat_sys_download);
			remoteView.setTextViewText(R.id.notname, fileName);
			remoteView.setTextViewText(R.id.notartistname, "-" + artist);
			while (flag && stopThreadFlag) {
				if (pg.getPercent() == 100) {
				    if(RemoteMp3ListActivity.downloadTasks.get(downloadTask.getTaskIndex()).getState_download() == DownloadTask.STATE_DONE){
				        notification.tickerText = fileName + " " + getText(R.string.downloadSuccess);
	                    notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
	                    notification.icon = android.R.drawable.stat_sys_download_done;
	                    handler.sendEmptyMessage(0);
	                    break;
				    }
				    if (!hadStartService) {
	                    Intent scanIntent = new Intent(getApplicationContext(), ScanService.class);
	                    scanIntent.putExtra("taskIndex", downloadTask.getTaskIndex());
	                    startService(scanIntent);
	                    hadStartService = true;
	                }
				}else{
				    System.out.println(Thread.currentThread().getId()+ ":" + downloadTask);
				    handler.sendEmptyMessage(0);
				}
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	

	class DownloadLrcThread implements Runnable {

		private String lrcPath = "";
		private String savePath = "";
		private String finalLrcName = "";

		public DownloadLrcThread(String lrcPath, String savePath, String finalLrcName) {
			this.lrcPath = lrcPath;
			this.savePath = savePath;
			this.finalLrcName = finalLrcName;
		}

		@Override
		public void run() {
			HttpDownloaderUtils hdu = new HttpDownloaderUtils();
			hdu.downLoadFile(lrcPath, savePath, finalLrcName);
		}

	}

	@Override
	public void onDestroy() {
		flag = false;
		Set<Integer> keys = tasks.keySet();
		Iterator<Integer> iter = keys.iterator();
		ArrayList<Thread> threads = null;
		int key;
		while(iter.hasNext()){
			key = iter.next();
			threads = tasks.get(key);
			int size = threads.size();
			for(int i=0;i<size;i++){
				threads.get(i).interrupt();
			}
			try {
			    if(RemoteMp3ListActivity.downloadTasks.get(key).getState_download() != DownloadTask.STATE_DONE){
			        FileUtil.deleteFile("mp3", mp3Names.get(key));
			    }
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		notificationManager.cancelAll();
		super.onDestroy();
	}
	
}
