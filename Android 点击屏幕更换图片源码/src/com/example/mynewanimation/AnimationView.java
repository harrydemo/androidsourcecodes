package com.example.mynewanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class AnimationView extends View{

	private static AnimationView animationView;
	
	Bitmap mbitmap = null;
	int width;
	int height;
	
	static AnimationView getInstance(Context context){
		if(animationView == null){
			animationView = new AnimationView(context);
		}
		return animationView;
	}
	
	public AnimationView(Context context) {
		super(context);
		mbitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.success)).getBitmap();
		width = mbitmap.getWidth();
		height = mbitmap.getHeight();
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap mbitmap2 =  Bitmap.createBitmap(mbitmap);
		canvas.drawBitmap(mbitmap2, (480-width)/2, (800-height)/2, null);
	}
	
}