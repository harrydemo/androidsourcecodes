package com.gw.android.boom.effect;

import java.util.Random;

import com.gw.android.boom.R;
import com.gw.android.boom.globe.BoomVariable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CloudEffect {

	Bitmap cloudMain;
	Bitmap cloud1;
	Bitmap cloud2;
	Bitmap cloud3;
	Bitmap bg;
	private int left1;
	private int left2;
	private int left3;
	
	private int top1;
	private int top2;
	private int top3;
	
	private int left = -160;

	private int flag = 2;

	final private int leftNo = 1;

	final private int rightNo = 2;

	final private int step = 2;

	final private int maxLength = 10;
	Random rnd=new Random();
	int cx;
	int cy;
	public CloudEffect() {

	}

	public CloudEffect(Context ct) {
		Resources r = ct.getResources();
		cloudMain = BitmapFactory.decodeResource(r, R.drawable.maincloud);
		cloud1 = BitmapFactory.decodeResource(r, R.drawable.cloud1);
		cloud2 = BitmapFactory.decodeResource(r, R.drawable.cloud2);
		cloud3 = BitmapFactory.decodeResource(r, R.drawable.cloud3);
		cx=BoomVariable.SCREENW;
		cy=BoomVariable.SCREENH;
		left1=rnd.nextInt(cx);
		left2=rnd.nextInt(cx);
		left3=rnd.nextInt(cx);
		top1=rnd.nextInt(cy/2);
		top2=rnd.nextInt(cy/2);
		top3=rnd.nextInt(cy/2);
		r = null;
	}
	public void update() {
		switch (flag) {
		case rightNo:
			left = left + step;
			if (left + rnd.nextInt(maxLength) >= 0) {
				flag = leftNo;
			}
			break;
		case leftNo:
			left = left - step;
			if (left <= -160) {
				flag = rightNo;
			}
			break;
		default:
			break;
		}
		if (left1 >= cx) {
			left1 = 0;
		}else{
			left1++;
		}
		if (left2 >= cx) {
			left2 = 0;
		}else{
			left2++;
		}
		if (left3 >= cx) { 
			left3 = 0;
		}else{
			left3++;
		}
	}
	public void paint(Canvas c){
		Paint paint=new Paint();
		c.drawBitmap(cloudMain, left, 0, paint);
		c.drawBitmap(cloud1, left1, top1, paint);
		c.drawBitmap(cloud2, left2, top2, paint);
		c.drawBitmap(cloud3, left3, top3, paint);
	}
}
