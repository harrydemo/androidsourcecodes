package com.turlet.shadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * @author turlet
 *
 */
public class ImageShadowView extends SurfaceView implements SurfaceHolder.Callback{

	private Bitmap mBitmap;
	
	public ImageShadowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);
		mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xiaochuan);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		canvas.drawBitmap(mBitmap, 0, 0, null);
		
		Bitmap nBitmap = setShadow(mBitmap);
		nBitmap = setAlpha(nBitmap,50);//一半透明
		canvas.drawBitmap(nBitmap, 0, height+2, null);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Canvas canvas = holder.lockCanvas();
		this.onDraw(canvas);
		holder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 * @param sourceImg 传入的图片
	 * @param number 0-100(0为完全透明，100为不透明)
	 * @return Bitmap 处理后的图片
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {   
	     int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];   
	     sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,sourceImg.getWidth(), sourceImg.getHeight());   
	     number = number * 255 / 100;   
	     double round = (double)number/(double)(argb.length);
	     System.out.println(round+ "  l="+argb.length +" n="+number);
	     for (int i = 0; i < argb.length; i++) {   
	      if(number-i*round>10){
	    	  argb[i] = ((int)(number-i*round) << 24) | (argb[i] & 0x00FFFFFF);
	    	  continue;
	      }
	      else{
	    	  argb[i] = (10 << 24) | (argb[i] & 0x00FFFFFF);
	    	  continue;
	      }

	     }   
	     sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Config.ARGB_8888);   
	        
	     return sourceImg;   
	   }   
	/**
	 * 传入一张图片，经倒转后，再取一半
	 * @param bitmap
	 * @return
	 */
	public static Bitmap setShadow(Bitmap bitmap){
		int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap shadowImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);
		return shadowImage;
	}

}
