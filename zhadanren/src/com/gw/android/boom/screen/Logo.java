package com.gw.android.boom.screen;

import com.gw.android.boom.R;
import com.gw.android.boom.globe.BoomVariable;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Logo {
	private Bitmap im;
	private int t;
	private int a;// 透明度
	private int SW, SH;
	public Logo() {

	}

	public Logo(Context context) {
		SW=BoomVariable.SCREENW;
		SH=BoomVariable.SCREENH;
		// 获得系统资源
		Resources r = context.getResources();
		// 加载logo图片
		im = BitmapFactory.decodeResource(r, R.drawable.logo);
		r = null;
		a = 255;
	}

	public void paint(Canvas c) {
		long cx = 0;
		long cy = 0;
		// 取得画笔
		Paint paint = new Paint();
		// 设置画笔颜色
		paint.setColor(0xff000000);
		c.drawRect(0, 0, SW, SH, paint);
		paint.setAlpha(a);
		cx = SW/ 2 - im.getWidth() / 2;//居中
		cy = SH / 2 - im.getHeight() / 2;//居中
		c.drawBitmap(im, cx, cy, paint);
	}

	public void update() {
		// fade in与fade out
		t++;
		if (t > 50) {
			a -= 10;
		}
		if (a <= 0) {
			a = 0;
			BoomVariable.GAME_STATE = BoomVariable.TITLE_SCREEN;
			t = 0;
		}
	}
}
