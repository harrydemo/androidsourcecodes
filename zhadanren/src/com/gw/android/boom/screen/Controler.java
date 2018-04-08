package com.gw.android.boom.screen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gw.android.boom.R;

public class Controler {
	/**
	 * Ä£ÄâÆ÷»æÖÆ
	 */
	private Bitmap e1, e2;

	public static String direction = "none";
	public static String preD="down";

	public Controler() {

	}

	public Controler(Context ct) {
		Resources res = ct.getResources();
		e1 = BitmapFactory.decodeResource(res, R.drawable.e1);
		e2 = BitmapFactory.decodeResource(res, R.drawable.e2);
		res = null;
	}

	public void paint(Canvas c) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		c.drawBitmap(e1, 0, 200, paint);
		c.drawBitmap(e2, 400, 240, paint);
	}

	public void update() {

	}
}
