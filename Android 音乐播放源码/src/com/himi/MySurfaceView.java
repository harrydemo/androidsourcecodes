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
	private HashMap<Integer, Integer> soundPoolMap;//��ע1
	private int loadId;
	private SoundPool soundPool; 
	public MySurfaceView(Context context) {
		super(context);
// ��ȡ��Ƶ����Ȼ��ǿת��һ����Ƶ������,���淽����������������С��
		am = (AudioManager) MainActivity.instance
				.getSystemService(Context.AUDIO_SERVICE);
		maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// ��ȡ�������ֵ��15���! .����100����
		sfh = this.getHolder();
		sfh.addCallback(this);
		th = new Thread(this);
		this.setKeepScreenOn(true);
		setFocusable(true);
		paint = new Paint();
		paint.setAntiAlias(true);
		//MediaPlayer�ĳ�ʼ��
		player = MediaPlayer.create(context, R.raw.himi); 
		player.setLooping(true);//����ѭ������
		//SoundPool�ĳ�ʼ��
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(MainActivity.content,
				R.raw.himi_ogg, 1));
		loadId = soundPool.load(context, R.raw.himi_ogg, 1);
//load()���������һ����������ʶ���ȿ��ǵ�������Ŀǰû���κ�Ч����ʹ����Ҳֻ�Ƕ�δ���ļ����Լ�ֵ��

	}

	public void surfaceCreated(SurfaceHolder holder) {
		/*
		 * Android OS�У������ȥ���ֻ��ϵĵ��������İ�ť��������������
		 * һ���ǵ����ֻ����������������һ���ǵ�����Ϸ����������ֲ��ŵ�����
		 * ����������Ϸ�е�ʱ�� �������������Ϸ�������������ֻ�������������
		 * ���Ƿ��˵����������ˣ����ڿ����з��֣�ֻ����Ϸ���������ڲ��ŵ�ʱ��
		 * �������ȥ������Ϸ����������������ֻ�����������û�а취���ֻ�ֻҪ��
		 * ��������Ϸ��״̬��ֻ������Ϸ�������أ�����������δ����!
		 */
		MainActivity.instance.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// �趨��������Ϊý������,����ͣ���ŵ�ʱ����������Ͳ�����Ĭ�ϵ������������ˣ��޹���
		
		player.start();
		th.start();

	}

	public void draw() {
		canvas = sfh.lockCanvas();
		canvas.drawColor(Color.WHITE);
		paint.setColor(Color.RED);
		canvas.drawText("��ǰ����: " + currentVol, 100, 40, paint);
		canvas.drawText("��ǰ���ŵ�ʱ��" + player.getCurrentPosition() + "����", 100,
				70, paint);
		canvas.drawText("������м䰴ť�л� ��ͣ/��ʼ", 100, 100, paint);
		canvas.drawText("�������������5�� ", 100, 130, paint);
		canvas.drawText("������������5�� ", 100, 160, paint);
		canvas.drawText("����������������� ", 100, 190, paint);
		canvas.drawText("�����������С����", 100, 220, paint);
		sfh.unlockCanvasAndPost(canvas);
	}

	private void logic() {
		currentVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);// ���ϻ�ȡ��ǰ������ֵ
	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {
		if (key == KeyEvent.KEYCODE_DPAD_CENTER) {
			ON = !ON;
			if (ON == false)
				player.pause();
			else
				player.start(); 
		} else if (key == KeyEvent.KEYCODE_DPAD_UP) {// �������ﱾӦ����RIGHT,������Ϊ��ǰ�Ǻ���ģʽ,������ͬ
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
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol,// ��ע2
					AudioManager.FLAG_PLAY_SOUND);
		} else if (key == KeyEvent.KEYCODE_DPAD_RIGHT) {
			currentVol -= 1;
			if (currentVol <= 0) {
				currentVol = 0;
			}
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol,
					AudioManager.FLAG_PLAY_SOUND);
		}
		soundPool.play(loadId, currentVol, currentVol, 1, 0, 1f);// ��ע3
//		soundPool.play(soundPoolMap.get(1), currentVol, currentVol, 1, 0, 1f);//��ע4
//		soundPool.pause(1);//��ͣSoundPool������ 
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
