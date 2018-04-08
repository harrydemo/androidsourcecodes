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
	private String _titles[];
	private int _id;
	private String _title;
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
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.ANSWER");
		registerReceiver(InComingSMSReceiver, filter);
		
		rHandler = new Handler();
		fHandler = new Handler();
		rHandler.removeCallbacks(rewind);
		fHandler.removeCallbacks(forward);
		
	
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("com.alex.playmusic");
		filter1.addAction("com.alex.nextone");
		filter1.addAction("com.alex.lastone");
		filter1.addAction("com.alex.startapp");
		registerReceiver(appWidgetReceiver, filter1);	
		
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
		
		
		if ((flag==0)&&(intent.getExtras().getInt("list")==1)){
			System.out.println("Service flag=0");
			return;
		}
		if (intent.getIntArrayExtra("_ids")!=null){
			_ids = intent.getIntArrayExtra("_ids");
			_titles = intent.getStringArrayExtra("_titles");
		}
		int position1 = intent.getIntExtra("position", -1);
		if (position1!=-1){
			position = position1;
			_id = _ids[position];
			_title = _titles[position];
		}
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
			Intent intent2 = new Intent("com.alex.musictitle");
			intent2.putExtra("title", _title);
			sendBroadcast(intent2);
			Intent playIntent = new Intent("com.alex.play");
			sendBroadcast(playIntent);
		}
		
		
		
	
		int op = intent.getIntExtra("op", -1);
		if (op!=-1){
			switch (op) {
			case MUSIC_PLAY:
				if(!mp.isPlaying()){
					play();
				}
				break;
			case MUSIC_PAUSE:
				if (mp.isPlaying()){
					pause();
				}
				break;
			case MUSIC_STOP://停止
				stop();
				break;
			case PROGRESS_CHANGE:
			    currentTime = intent.getExtras().getInt("progress");
			    mp.seekTo(currentTime);
			    
				break;
			case MUSIC_REWIND:
				rewind();
				break;
			case MUSIC_FORWARD:
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
	
	private void rewind(){
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
	
	private int getRandomPostion(boolean loopAll)
	{
		int ret = -1;
		
		if(MusicActivity.randomNum < _ids.length-1)
		{
			MusicActivity.randomIDs[MusicActivity.randomNum] = position;
			ret = MusicActivity.findRandomSound(_ids.length);
			MusicActivity.randomNum++;
			
		}
		else if(loopAll == true)
		{
			MusicActivity.randomNum = 0;
			for(int i = 0; i < _ids.length; i++)
			{
				MusicActivity.randomIDs[i] = -1;
			}
			MusicActivity.randomIDs[MusicActivity.randomNum] = position;
			ret = MusicActivity.findRandomSound(_ids.length);
			MusicActivity.randomNum++;
		}
		
		return ret;
	}
	
	private void nextOne(){
		if (_ids.length==1 || MusicActivity.loop_flag == MusicActivity.LOOP_ONE){
			position = position;
			
		} 
		else if (MusicActivity.loop_flag == MusicActivity.LOOP_ALL)
		{
			if(MusicActivity.random_flag == true)
			{//锟截革拷全锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
				int i = getRandomPostion(true);
				if(i == -1)
				{
					stop();
					return;
				}
				else
				{
					position = i;
				}
			}
			else
			{
				if (position == _ids.length-1){
					position = 0;
				} else if (position < _ids.length-1){
					position++;
				}
			}
			
		}
		else if(MusicActivity.loop_flag == MusicActivity.LOOP_NONE)
		{
			if(MusicActivity.random_flag == true)
			{//锟斤拷锟斤拷
				int i = getRandomPostion(false);
				if(i == -1)
				{
					stop();
					return;
				}
				else
				{
					position = i;
				}
			}
			else
			{
				if (position == _ids.length-1){
					stop();
					return;
				} else if (position < _ids.length-1){
					position++;
				}
			}
		}
		uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				"" + _ids[position]);
		DBOperate(_ids[position]);
		id=_ids[position];
		_title = _titles[position];
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
		
		
		Intent intent = new Intent();
		intent.setAction(MUSIC_LIST);
		intent.putExtra("position", position);
		sendBroadcast(intent);
		
		
		Intent intent1 = new Intent();
		intent1.setAction(MUSIC_UPDATE);
		intent1.putExtra("position", position);
		sendBroadcast(intent1);
		
		
		Intent intent2 = new Intent("com.alex.musictitle");
		intent2.putExtra("title", _title);
		sendBroadcast(intent2);
		
	}
	
	private void lastOne(){
		if (_ids.length==1){
			position = position;
			
		} else if (position == 0){
			position = _ids.length-1;
		} else if (position > 0){
			position--;
		}
		uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				"" + _ids[position]);
		DBOperate(_ids[position]);
		id=_ids[position];
		_title = _titles[position];
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
		
		
		Intent intent = new Intent();
		intent.setAction(MUSIC_LIST);
		intent.putExtra("position", position);
		sendBroadcast(intent);
		
		
		Intent intent1 = new Intent();
		intent1.setAction(MUSIC_UPDATE);
		intent1.putExtra("position", position);
		sendBroadcast(intent1);
		
		
		Intent intent2 = new Intent("com.alex.musictitle");
		intent2.putExtra("title", _title);
		sendBroadcast(intent2);
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		nextOne();
	}
	
	
	private void DBOperate(int pos){
		dbHelper = new DBHelper(this, "music.db", null, 2);
		Cursor c = dbHelper.query(pos);
		Date currentTime = new Date();   
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		String dateString = formatter.format(currentTime); 
		if (c==null||c.getCount()==0){//锟斤拷锟斤拷询锟斤拷锟轿拷锟�		
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
	
	
	protected BroadcastReceiver InComingSMSReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
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
	
	
	
	protected BroadcastReceiver appWidgetReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.alex.playmusic")){
				if (mp.isPlaying()){
					pause();
					Intent pauseIntent = new Intent("com.alex.pause");
					sendBroadcast(pauseIntent);
				} else {
					play();
					Intent playIntent = new Intent("com.alex.play");
					sendBroadcast(playIntent);
				}
			} else if (intent.getAction().equals("com.alex.nextone")){
				nextOne();
				Intent playIntent = new Intent("com.alex.play");
				sendBroadcast(playIntent);
			} else if (intent.getAction().equals("com.alex.lastone")){
				lastOne();
				Intent playIntent = new Intent("com.alex.play");
				sendBroadcast(playIntent);
			} else if (intent.getAction().equals("com.alex.startapp")){
				Intent intent1 = new Intent("com.alex.musictitle");
				intent1.putExtra("title", _title);
				sendBroadcast(intent1);
			}
		}
	};
	

}
