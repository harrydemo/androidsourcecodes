package com.alex.media;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

	private static final String MUSIC_CURRENT = "com.alex.currentTime";
	private static final String MUSIC_DURATION = "com.alex.duration";
	private static final String MUSIC_NEXT = "com.alex.next";
	private static final String MUSIC_UPDATE = "com.alex.update";
	private static final String MUSIC_LIST = "com.alex.list";
	private static final int MUSIC_PLAY = 1;
	private static final int MUSIC_PAUSE = 2;
	private static final int MUSIC_STOP = 3;
	private static final int PROGRESS_CHANGE = 4;
	private static final int MUSIC_REWIND = 5;
	private static final int MUSIC_FORWARD = 6;
	private MediaPlayer mp = null;
	int progress;
	private Uri uri = null;
	private int id=10000;
	private Handler handler=null;
	private Handler rHandler = null;
	private Handler fHandler = null;
	private int currentTime;
	private int duration;
	private DBHelper dbHelper = null;
	private int flag;
	private int position;
	private int _ids[];
	private int _id;
	@Override
	public void onCreate() {
		super.onCreate();
		if (mp != null) {
			mp.reset();
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		
		/**
		 * 注册来电接收器
		 */
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.ANSWER");
		registerReceiver(InComingSMSReceiver, filter);
		
		rHandler = new Handler();
		fHandler = new Handler();
		rHandler.removeCallbacks(rewind);
		fHandler.removeCallbacks(forward);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service destroy!");
		if (mp!=null){
			mp.stop();
			mp = null;
		}
		if (dbHelper!=null){
			dbHelper.close();
			dbHelper = null;
		}
		if (handler!=null){
			handler.removeMessages(1);
			handler=null;
		}
	}
	

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		/**
		 * 初始化mp
		 */
		if ((flag==0)&&(intent.getExtras().getInt("list")==1)){
			System.out.println("Service flag=0");
			return;
		}
		if (intent.getIntArrayExtra("_ids")!=null){
			_ids = intent.getIntArrayExtra("_ids");
		}
		int position1 = intent.getIntExtra("position", -1);
		System.out.println("position1:"+position1);
		System.out.println("position:"+position);
		if (position1!=-1){
			position = position1;
			_id = _ids[position];
		}
		System.out.println("_id:"+_id);
		System.out.println("id:"+id);
		int length = intent.getIntExtra("length", -1);
		if (_id!=-1){
			if (id!=_id){
				id=_id;
				uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						"" + _id);
				DBOperate(_id);
				try {
					mp.reset();
					mp.setDataSource(this, uri);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (length==1){
				
				try {
					mp.reset();
					mp.setDataSource(this, uri);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		setup();
		init();
		if (position!=-1){
			Intent intent1 = new Intent();
			intent1.setAction(MUSIC_LIST);
			intent1.putExtra("position", position);
			sendBroadcast(intent1);
			System.out.println("Service position:" + position);
		}
		
		/**
		 * 开始、暂停、停止
		 */
		int op = intent.getIntExtra("op", -1);
		if (op!=-1){
			switch (op) {
			case MUSIC_PLAY://播放
				if(!mp.isPlaying()){
					play();
				}
				break;
			case MUSIC_PAUSE://暂停
				if (mp.isPlaying()){
					pause();
				}
				break;
			case MUSIC_STOP://停止
				stop();
				break;
			case PROGRESS_CHANGE://改变歌曲进度
			    currentTime = intent.getExtras().getInt("progress");
			    mp.seekTo(currentTime);
			    
				break;
			case MUSIC_REWIND://快退
				rewind();
				break;
			case MUSIC_FORWARD://快进
				forward();
				break;
			} 
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void play(){
		if (mp!=null){
			mp.start();
		}
		flag = 1;
		rHandler.removeCallbacks(rewind);
		fHandler.removeCallbacks(forward);
	}
	
	private void pause(){
		if (mp!=null){
			mp.pause();
		}
		flag = 1;
	}
	
	private void stop(){
		if (mp!=null){
			mp.stop();
			try {
				mp.prepare();
				mp.seekTo(0);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			handler.removeMessages(1);
			rHandler.removeCallbacks(rewind);
			fHandler.removeCallbacks(forward);
		}
	}
	
	
	private void init(){
		final Intent intent = new Intent();
		intent.setAction(MUSIC_CURRENT);
		if (handler==null){
			handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what==1){
						if(flag==1){
							currentTime = mp.getCurrentPosition();
							intent.putExtra("currentTime", currentTime);
							sendBroadcast(intent);
						}
						handler.sendEmptyMessageDelayed(1, 600);
					}
				}
			};
		}
	}
	
	private void setup(){
		final Intent intent = new Intent();
		intent.setAction(MUSIC_DURATION);
		try {
			if (!mp.isPlaying()){
				mp.prepare();
			}
			mp.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					handler.sendEmptyMessage(1);
				}
			});
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		duration = mp.getDuration();
		intent.putExtra("duration", duration);
		sendBroadcast(intent);
	}
	
	private void rewind(){//快进
		rHandler.post(rewind);
	}
	
	private void forward(){
		fHandler.post(forward);
	}
	
	Runnable rewind = new Runnable() {
		
		@Override
		public void run() {
			if (currentTime>=0){
				currentTime = currentTime - 5000;
				mp.seekTo(currentTime);
				rHandler.postDelayed(rewind, 500);
			}
			
		}
	};
	
	Runnable forward = new Runnable() {
		
		@Override
		public void run() {
			if (currentTime<=duration){
				currentTime = currentTime + 5000;
				mp.seekTo(currentTime);
				fHandler.postDelayed(forward, 500);
			}
		}
	};
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		/*Intent intent = new Intent();
		intent.setAction(MUSIC_NEXT);
		sendBroadcast(intent);
		System.out.println("onCompletion...");*/
		if (_ids.length==1){
			position = position;
			
		} else if (position == _ids.length-1){
			position = 0;
		} else if (position < _ids.length-1){
			position++;
		}
		uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				"" + _ids[position]);
		DBOperate(_ids[position]);
		id=_ids[position];
		_id=id;
		try {
			mp.reset();
			mp.setDataSource(this, uri);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		handler.removeMessages(1);
		rHandler.removeCallbacks(rewind);
		fHandler.removeCallbacks(forward);
		setup();
		init();
		play();
		
		//通知音乐列表更新
		Intent intent = new Intent();
		intent.setAction(MUSIC_LIST);
		intent.putExtra("position", position);
		sendBroadcast(intent);
		
		//通知播放界面更新
		Intent intent1 = new Intent();
		intent1.setAction(MUSIC_UPDATE);
		intent1.putExtra("position", position);
		sendBroadcast(intent1);
		
		
		
	}
	
	/**
	 * 数据库操作
	 * @param pos
	 */
	private void DBOperate(int pos){
		dbHelper = new DBHelper(this, "music.db", null, 2);
		Cursor c = dbHelper.query(pos);
		Date currentTime = new Date();   
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		String dateString = formatter.format(currentTime); 
		if (c==null||c.getCount()==0){//如果查询结果为空			
			ContentValues values = new ContentValues();
			values.put("music_id", pos);
			values.put("clicks", 1);
			values.put("latest", dateString);
			dbHelper.insert(values);
		} else{
			c.moveToNext();
			int clicks = c.getInt(2);
			clicks++;
			ContentValues values = new ContentValues();
			values.put("clicks", clicks);
			values.put("latest", dateString);
			dbHelper.update(values, pos);
		}
		if (c!=null){
			c.close();
			c = null;
		}
		if (dbHelper!=null){
			dbHelper.close();
			dbHelper = null;
		}
	}
	
	/**
	 * 来电广播接收器
	 */
	protected BroadcastReceiver InComingSMSReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("android.intent.action.ANSWER");
			if (intent.getAction().equals(Intent.ACTION_ANSWER)){
				TelephonyManager telephonymanager = 
					(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				switch (telephonymanager.getCallState()) {
					case TelephonyManager.CALL_STATE_RINGING:
						pause();
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:
						play();
						break;
					 default:
						 break;
				}
			}
		}
	};
	
	

}
