package org.music.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.music.tools.DBHelper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
/*
 *本地音乐播放服务,为了实现后台服务，我硬着头皮去写。小小吐槽下:Mediaplayer类你虽然强。但是播放音乐，有自带start，pause，stop方法，但是播放下一首，上一首怎么没有next，last这个呢
 *还要我们去写，这个最讨厌了- -。另外4.1把OnStart方法屏蔽了.虽然用广播和界面做互动。但是觉得还是不好。你们不放帮我改改吧。
 */
public class LocalMusicService extends Service implements OnCompletionListener {
	private MediaPlayer mp;// MediaPlayer对象
	private static final int PLAYING = 1;// 定义该怎么对音乐操作的常量,如播放是1
	private static final int PAUSE = 2;// 暂停事件是2
	private static final int STOP = 3;// 停止事件是3
	private static final int PROGRESS_CHANGE = 4;// 进度条改变事件设为4
	private static final String MUSIC_CURRENT = "com.music.currentTime";
	private static final String MUSIC_DURATION = "com.music.duration";
	private static final String MUSIC_NEXT = "com.music.next";
	private Handler handler;// handler对象
	private Uri uri = null;// 路径地址
	private int id = 10000;
	private int currentTime;// 当前时间
	private int duration;// 总时间
	private DBHelper dbHelper;
	
	

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		if (mp != null) {
			mp.release();// 建立服务先清空
			mp.reset();// 回起始点
			mp = null;
		}
		mp = new MediaPlayer();// 实例化Mdiaplayer对象
		mp.setOnCompletionListener(this);// 加下一首监听
		IntentFilter intentfilter=new IntentFilter();
		intentfilter.addAction("com.app.media.MUSIC_SERVICE");
		registerReceiver(phonelistener, intentfilter);

	}

	/**
	 * 秒杀服务
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mp != null) {
			mp.stop();
			mp = null;
		}
		if (handler != null) {
			handler.removeMessages(1);
			handler = null;
		}
		if (dbHelper == null) {
			dbHelper.close();
		}
	}

	/**
	 * 服务开始，4.1把onStart"屏蔽"了
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		/**
		 * 初始化媒体
		 */
		int _id = intent.getIntExtra("_id", -1);
		if (_id!=-1){
			if (id!=_id){
				id=_id;
				uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						"" + _id);
				DBOperate(_id);//操作数据库
				try {
					mp.reset();
					mp.setDataSource(this, uri);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		setup();
		Load();// 初始化服务
		int op = intent.getIntExtra("op", -1);// 从UI接收到的数据交给服务处理
		// 从接收的数据中作判断播放操作
		if (op != -1) {
			switch (op) {
			case PLAYING:
				play();// 播放音乐
				break;

			case PAUSE:
				pause();// 暂停音乐
				break;

			case STOP:
		       stop();// 停止播放
			   break;
			// 进度条改变
			case PROGRESS_CHANGE:
				int progress = intent.getExtras().getInt("progress");
				mp.seekTo(progress);
				break;

			}
		}

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Intent intent = new Intent();
		intent.setAction(MUSIC_NEXT);
		sendBroadcast(intent);
		System.out.println("onCompletion...");
	}

	/**
	 * 播放音乐
	 */
	public void play() {
		if (mp != null) {
			mp.start();
		}
	
	}

	public void pause() {
		if (mp != null) {
			mp.pause();
		}
	}

	// 停止播放
	public void stop() {
		if (mp != null) {
			mp.stop();
			try {
				mp.prepare();// 这里开始播放时会出现抛异常
				mp.seekTo(0);
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			handler.removeMessages(1);
		
		}
	}

	public void setup() {
		final Intent intent = new Intent();
		intent.setAction(MUSIC_DURATION);
		try {
			if (!mp.isPlaying()) {
				mp.prepare();
				mp.start();
			} else if (!mp.isPlaying()) {
				mp.stop();
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

	public void Load() {
		final Intent intent = new Intent();
		intent.setAction(MUSIC_CURRENT);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					currentTime = mp.getCurrentPosition();
					intent.putExtra("currentTime", currentTime);
					sendBroadcast(intent);
					
				}
				handler.sendEmptyMessageDelayed(1, 610);
			
			}

		};
	}

	/**
	 * 数据库操作
	 * 
	 * @param pos
	 */
	private void DBOperate(int pos) {
		dbHelper = new DBHelper(this, "music.db", null, 2);
		Cursor c = dbHelper.query(pos);
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		if (c == null || c.getCount() == 0) {// 如果查询结果为空
			ContentValues values = new ContentValues();
			values.put("music_id", pos);
			values.put("clicks", 1);
			values.put("latest", dateString);
			dbHelper.insert(values);
		} else {
			c.moveToNext();
			int clicks = c.getInt(2);
			clicks++;
			ContentValues values = new ContentValues();
			values.put("clicks", clicks);
			values.put("latest", dateString);
			dbHelper.update(values, pos);
		}
		if (c != null) {
			c.close();
			c = null;
		}
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}
	/**
	 * 电话监听服务
	 */
	private BroadcastReceiver phonelistener=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_ANSWER)) {
				TelephonyManager tel=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				switch (tel.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING:
					pause();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					play();
					break;
				}
			}
			
		}
	};
}
