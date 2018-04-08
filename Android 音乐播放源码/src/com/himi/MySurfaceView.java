package com.himi;

import java.util.HashMap;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private Thread th;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private MediaPlayer player;
	private Paint paint;
	private boolean ON = true;
	private int currentVol, maxVol;
	private AudioManager am; 
	private HashMap<Integer, Integer> soundPoolMap;//备注1
	private int loadId;
	private SoundPool soundPool; 
	public MySurfaceView(Context context) {
		super(context);
// 获取音频服务然后强转成一个音频管理器,后面方便用来控制音量大小用
		am = (AudioManager) MainActivity.instance
				.getSystemService(Context.AUDIO_SERVICE);
		maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 获取最大音量值（15最大! .不是100！）
		sfh = this.getHolder();
		sfh.addCallback(this);
		th = new Thread(this);
		this.setKeepScreenOn(true);
		setFocusable(true);
		paint = new Paint();
		paint.setAntiAlias(true);
		//MediaPlayer的初始化
		player = MediaPlayer.create(context, R.raw.himi); 
		player.setLooping(true);//设置循环播放
		//SoundPool的初始化
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(MainActivity.content,
				R.raw.himi_ogg, 1));
		loadId = soundPool.load(context, R.raw.himi_ogg, 1);
//load()方法的最后一个参数他标识优先考虑的声音。目前没有任何效果。使用了也只是对未来的兼容性价值。

	}

	public void surfaceCreated(SurfaceHolder holder) {
		/*
		 * Android OS中，如果你去按手机上的调节音量的按钮，会分两种情况，
		 * 一种是调整手机本身的铃声音量，一种是调整游戏，软件，音乐播放的音量
		 * 当我们在游戏中的时候 ，总是想调整游戏的音量而不是手机的铃声音量，
		 * 可是烦人的问题又来了，我在开发中发现，只有游戏中有声音在播放的时候
		 * ，你才能去调整游戏的音量，否则就是手机的音量，有没有办法让手机只要是
		 * 在运行游戏的状态就只调整游戏的音量呢？试试下面这段代码吧!
		 */
		MainActivity.instance.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// 设定调整音量为媒体音量,当暂停播放的时候调整音量就不会再默认调整铃声音量了，娃哈哈
		
		player.start();
		th.start();

	}

	public void draw() {
		canvas = sfh.lockCanvas();
		canvas.drawColor(Color.WHITE);
		paint.setColor(Color.RED);
		canvas.drawText("当前音量: " + currentVol, 100, 40, paint);
		canvas.drawText("当前播放的时间" + player.getCurrentPosition() + "毫秒", 100,
				70, paint);
		canvas.drawText("方向键中间按钮切换 暂停/开始", 100, 100, paint);
		canvas.drawText("方向键←键快退5秒 ", 100, 130, paint);
		canvas.drawText("方向键→键快进5秒 ", 100, 160, paint);
		canvas.drawText("方向键↑键增加音量 ", 100, 190, paint);
		canvas.drawText("方向键↓键减小音量", 100, 220, paint);
		sfh.unlockCanvasAndPost(canvas);
	}

	private void logic() {
		currentVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);// 不断获取当前的音量值
	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {
		if (key == KeyEvent.KEYCODE_DPAD_CENTER) {
			ON = !ON;
			if (ON == false)
				player.pause();
			else
				player.start(); 
		} else if (key == KeyEvent.KEYCODE_DPAD_UP) {// 按键这里本应该是RIGHT,但是因为当前是横屏模式,以下雷同
			player.seekTo(player.getCurrentPosition() + 5000);
		} else if (key == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (player.getCurrentPosition() < 5000) {
				player.seekTo(0);
			} else {
				player.seekTo(player.getCurrentPosition() - 5000);
			}
		} else if (key == KeyEvent.KEYCODE_DPAD_LEFT) {
			currentVol += 1;
			if (currentVol > maxVol) {
				currentVol = 100;
			}
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol,// 备注2
					AudioManager.FLAG_PLAY_SOUND);
		} else if (key == KeyEvent.KEYCODE_DPAD_RIGHT) {
			currentVol -= 1;
			if (currentVol <= 0) {
				currentVol = 0;
			}
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol,
					AudioManager.FLAG_PLAY_SOUND);
		}
		soundPool.play(loadId, currentVol, currentVol, 1, 0, 1f);// 备注3
//		soundPool.play(soundPoolMap.get(1), currentVol, currentVol, 1, 0, 1f);//备注4
//		soundPool.pause(1);//暂停SoundPool的声音 
		return super.onKeyDown(key, event);
	} 
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	} 
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			draw();
			logic();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}
	} 
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {  
	} 
	public void surfaceDestroyed(SurfaceHolder holder) {  
	} 
}
