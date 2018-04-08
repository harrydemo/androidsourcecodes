package com.gw.android.boom.game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gw.android.boom.effect.CloudEffect;
import com.gw.android.boom.globe.BoomVariable;
import com.gw.android.boom.screen.Logo;
import com.gw.android.boom.screen.MainScreen;
import com.gw.android.boom.screen.TitleScreen;
/**
 * 游戏主类
 */
public class MainGame extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {
	public boolean mainloop;
	public Context context;
	public SurfaceHolder surfaceHolder;

	public Logo logo;
	public TitleScreen ts;
	public static MainScreen ms;

	public CloudEffect cf;

	public MainGame(Context ct) {
		super(ct);
		this.context = ct;
		this.mainloop = true;
		this.setFocusable(true);
		this.surfaceHolder = this.getHolder();
		this.surfaceHolder.addCallback(this);
		this.logo = new Logo(ct);
		this.ts = new TitleScreen(ct);
		this.cf = new CloudEffect(ct);

	}

	public void paint(Canvas c) {
		switch (BoomVariable.GAME_STATE) {
		case BoomVariable.LOGO_SCREEN:
			logo.paint(c);
			break;
		case BoomVariable.TITLE_SCREEN:
			ts.paint(c);
			cf.paint(c);
			break;
		case BoomVariable.GAME_SCREEN:
			if(ms!=null){
				ms.paint(c);
			}
			break;
		}
	}

	public void update() {
		switch (BoomVariable.GAME_STATE) {
		case BoomVariable.LOGO_SCREEN:
			logo.update();
			break;
		case BoomVariable.TITLE_SCREEN:
			ts.update();
			cf.update();
			break;
		case BoomVariable.GAME_SCREEN:
			if (ms != null) {
				ms.update();
			}
			break;
		}
	}

	@Override
	public void run() {
		long st, et;
		while (mainloop) {
			try {
				update();
				st = System.currentTimeMillis();
				Canvas canvas = surfaceHolder.lockCanvas();
				paint(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
				et = System.currentTimeMillis();
				if (et - st < 50) {
					Thread.sleep(50 - et + st);//这样做的目的：为了保持运行【绘制时间+休眠时间】=50,也就是快于一秒24,肉眼即可认为是动画
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (BoomVariable.GAME_STATE) {
		case BoomVariable.LOGO_SCREEN:
			break;
		case BoomVariable.TITLE_SCREEN:
			ts.onTouchEvent(event);
			break;
		case BoomVariable.GAME_SCREEN:
			ms.onTouchEvent(event);
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (BoomVariable.GAME_STATE) {
		case BoomVariable.GAME_SCREEN:
			if(event.getRepeatCount()==0){
				ms.onKeyDown(keyCode, event);
			}
			break;
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mainloop = false;
	}

}
