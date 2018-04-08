package com.cosina.game.kickkick;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class ImageManager {
	
	private static Map<Integer,Bitmap> values = new HashMap<Integer, Bitmap>();

	public static Bitmap getBitmap(int index){
		return values.get(index);
	}
	
	public static void init(Context context){
		Resources resources = context.getResources();
		values.put(new Integer(13), loadBallView(resources,R.drawable.show1));
		values.put(new Integer(12), loadBallView(resources,R.drawable.show2));
		values.put(new Integer(11), loadBallView(resources,R.drawable.show3));
		values.put(new Integer(10), loadBallView(resources,R.drawable.show4));
		values.put(new Integer(9), loadBallView(resources,R.drawable.show5));
		values.put(new Integer(8), loadBallView(resources,R.drawable.show6));
		values.put(new Integer(7), loadBallView(resources,R.drawable.show6));
		values.put(new Integer(6), loadBallView(resources,R.drawable.show6));
		values.put(new Integer(5), loadBallView(resources,R.drawable.show5));
		values.put(new Integer(4), loadBallView(resources,R.drawable.show4));
		values.put(new Integer(3), loadBallView(resources,R.drawable.show3));
		values.put(new Integer(2), loadBallView(resources,R.drawable.show2));
		values.put(new Integer(1), loadBallView(resources,R.drawable.show1));
		values.put(new Integer(0), loadBallView(resources,R.drawable.emptyhole));
		
		values.put(new Integer(-9), loadBallView(resources,R.drawable.hit));
		values.put(new Integer(-8), loadBallView(resources,R.drawable.hit));		
		values.put(new Integer(-7), loadBallView(resources,R.drawable.hit));
		values.put(new Integer(-6), loadBallView(resources,R.drawable.hit));
		values.put(new Integer(-5), loadBallView(resources,R.drawable.show5));
		values.put(new Integer(-4), loadBallView(resources,R.drawable.show4));
		values.put(new Integer(-3), loadBallView(resources,R.drawable.show3));
		values.put(new Integer(-2), loadBallView(resources,R.drawable.show2));
		values.put(new Integer(-1), loadBallView(resources,R.drawable.show1));
		
	}
	
	private static Bitmap loadBallView(Resources resources,int resId) {
		 
	 	Drawable image = resources.getDrawable(resId);
		Bitmap bitmap = Bitmap.createBitmap(80,80, Bitmap.Config.ARGB_8888);
        
        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, 80,80);
        image.draw(canvas);
	 	
		return bitmap;
    }
}
