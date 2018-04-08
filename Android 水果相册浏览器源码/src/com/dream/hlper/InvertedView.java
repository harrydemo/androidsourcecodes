package com.dream.hlper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

public class InvertedView {
	public  static Bitmap zoomBitmap(Bitmap bitmap ,int w ,int h){
		  int width = bitmap.getWidth();//获得图像的宽
		  int height = bitmap.getHeight();//获得图像的高
		  Matrix matrix = new Matrix();
		  float scaleWidht = ((float)w / width);//缩放比例
		  float scaleHeight = ((float)h / height);
		     matrix.postScale(scaleWidht, scaleHeight);
		     Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		  return newbmp;
		 }
		//倒影 
	 public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
	        final int reflectionGap = 4;
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();

	        Matrix matrix = new Matrix();
	        matrix.preScale(1, -1);

	        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
	                width, height / 2, matrix, false);

	        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,(height + height / 2), Config.ARGB_8888);

	        Canvas canvas = new Canvas(bitmapWithReflection);
	        canvas.drawBitmap(bitmap, 0, 0, null);
	        Paint deafalutPaint = new Paint();
	        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
	        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

	        Paint paint = new Paint();
	        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
	                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
	                0x00ffffff, TileMode.CLAMP);
	        paint.setShader(shader);
	        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()+ reflectionGap, paint);

	        return bitmapWithReflection;
	    }	
}
