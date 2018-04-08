package com.gw.android.boom.screen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gw.android.boom.R;
import com.gw.android.boom.game.MainGame;
import com.gw.android.boom.globe.BoomVariable;
import com.gw.android.boom.globe.Utils;

public class TitleScreen {
	private Bitmap bg;
	private String[] menu = { "新游戏", "读取上次游戏", "帮助", "关于", "退出" };
	private int curItem;
	public Context context;

	public TitleScreen(Context context) {
		this.context = context;
		Resources res = context.getResources();
		bg = BitmapFactory.decodeResource(res, R.drawable.start);
		res = null;
	}

	public void paint(Canvas c) {
		Canvas canvas = Utils.getCanvas();
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawBitmap(bg, 0, 0, paint);
		paint.setARGB(100, 200, 200, 200);
		canvas.drawRect(new Rect(50, 40, 70 + menu[1].length() * 20, 230),
				paint);
		paint.setAlpha(255);
		paint.setTextSize(20);
		for (int i = 0; i < menu.length; i++) {
			if (curItem == i) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.BLACK);
			}
			canvas.drawText(menu[i], 0, menu[i].length(), 60, 60 + i * 40,
					paint);
		}
		c.drawBitmap(Utils.mscBitmap, 0, 0, paint);//具体什么意思？？？？不是太清楚喔 
	}

	public void update() {
		
	}

	@SuppressWarnings("all")
	public synchronized void onTouchEvent(MotionEvent event) {
		float _x = event.getX();
		float _y = event.getY();
		if (event.getAction() == event.ACTION_DOWN) {
			if (_x > 60 && _x < 60 + menu[0].length() * 20 && _y > 40
					&& _y < 60) {
				curItem = 0;
				MainGame.ms = new MainScreen(context);
				BoomVariable.GAME_STATE = BoomVariable.GAME_SCREEN;
			}
			if (_x > 60 && _x < 60 + menu[1].length() * 20 && _y > 80
					&& _y < 100) {
				curItem = 1;
			}
			if (_x > 60 && _x < 60 + menu[2].length() * 20 && _y > 120
					&& _y < 140) {
				curItem = 2;
			}
			if (_x > 60 && _x < 60 + menu[3].length() * 20 && _y > 160
					&& _y < 180) {
				curItem = 3;
			}
			if (_x > 60 && _x < 60 + menu[4].length() * 20 && _y > 200
					&& _y < 220) {
				curItem = 4;
				System.exit(0);
			}
		}
		if (event.getAction() == event.ACTION_UP) {
			
		}
	}
}
