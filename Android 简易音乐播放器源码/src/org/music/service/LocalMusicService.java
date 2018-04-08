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
 *�������ֲ��ŷ���,Ϊ��ʵ�ֺ�̨������Ӳ��ͷƤȥд��СС�²���:Mediaplayer������Ȼǿ�����ǲ������֣����Դ�start��pause��stop���������ǲ�����һ�ף���һ����ôû��next��last�����
 *��Ҫ����ȥд�������������- -������4.1��OnStart����������.��Ȼ�ù㲥�ͽ��������������Ǿ��û��ǲ��á����ǲ��Ű��Ҹĸİɡ�
 */
public class LocalMusicService extends Service implements OnCompletionListener {
	private MediaPlayer mp;// MediaPlayer����
	private static final int PLAYING = 1;// �������ô�����ֲ����ĳ���,�粥����1
	private static final int PAUSE = 2;// ��ͣ�¼���2
	private static final int STOP = 3;// ֹͣ�¼���3
	private static final int PROGRESS_CHANGE = 4;// �������ı��¼���Ϊ4
	private static final String MUSIC_CURRENT = "com.music.currentTime";
	private static final String MUSIC_DURATION = "com.music.duration";
	private static final String MUSIC_NEXT = "com.music.next";
	private Handler handler;// handler����
	private Uri uri = null;// ·����ַ
	private int id = 10000;
	private int currentTime;// ��ǰʱ��
	private int duration;// ��ʱ��
	private DBHelper dbHelper;
	
	

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		if (mp != null) {
			mp.release();// �������������
			mp.reset();// ����ʼ��
			mp = null;
		}
		mp = new MediaPlayer();// ʵ����Mdiaplayer����
		mp.setOnCompletionListener(this);// ����һ�׼���
		IntentFilter intentfilter=new IntentFilter();
		intentfilter.addAction("com.app.media.MUSIC_SERVICE");
		registerReceiver(phonelistener, intentfilter);

	}

	/**
	 * ��ɱ����
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
	 * ����ʼ��4.1��onStart"����"��
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		/**
		 * ��ʼ��ý��
		 */
		int _id = intent.getIntExtra("_id", -1);
		if (_id!=-1){
			if (id!=_id){
				id=_id;
				uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						"" + _id);
				DBOperate(_id);//�������ݿ�
				try {
					mp.reset();
					mp.setDataSource(this, uri);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		setup();
		Load();// ��ʼ������
		int op = intent.getIntExtra("op", -1);// ��UI���յ������ݽ���������
		// �ӽ��յ����������жϲ��Ų���
		if (op != -1) {
			switch (op) {
			case PLAYING:
				play();// ��������
				break;

			case PAUSE:
				pause();// ��ͣ����
				break;

			case STOP:
		       stop();// ֹͣ����
			   break;
			// �������ı�
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
	 * ��������
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

	// ֹͣ����
	public void stop() {
		if (mp != null) {
			mp.stop();
			try {
				mp.prepare();// ���￪ʼ����ʱ��������쳣
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
	 * ���ݿ����
	 * 
	 * @param pos
	 */
	private void DBOperate(int pos) {
		dbHelper = new DBHelper(this, "music.db", null, 2);
		Cursor c = dbHelper.query(pos);
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		if (c == null || c.getCount() == 0) {// �����ѯ���Ϊ��
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
	 * �绰��������
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
