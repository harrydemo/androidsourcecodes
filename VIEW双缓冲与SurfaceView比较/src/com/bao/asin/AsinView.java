package com.bao.asin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.View;

public class AsinView extends View{
	ShowThread showThread;
	int x = 0;
	Bitmap bitmap;
	int A = 50;
	Paint paint;
	
	class ShowThread extends Thread{
		private AsinView ssinView;
		boolean flag = true;
		int sleepTime = 200;
		
		public ShowThread(AsinView ssinView){
			this.ssinView = ssinView;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(flag == true){
				
				ssinView.postInvalidate();
		
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public AsinView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		showThread = new ShowThread(this);
		
		bitmap = Bitmap.createBitmap(480, 104, Config.ARGB_8888);     
		paint = new Paint();
		paint.setColor(Color.WHITE);
		showThread.start();
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(480, 160);
		
	}

	public void onDraw(Canvas canvas){
		
		if(x >= 480){
			x = 0;
			canvas.drawColor(Color.BLACK); 
			bitmap = Bitmap.createBitmap(480, 104, Config.ARGB_8888);     

		}else{
			Canvas c = new Canvas();
			c.setBitmap(bitmap);
			int y = (int)(A*Math.sin(x/180.0f*Math.PI));
			System.out.println("==================y:"+y+"===x:"+x);
			c.drawPoint(x, y+A, paint);
			//canvas.save();
			canvas.drawBitmap(bitmap, 0, 0,paint);
			x+=5;
		}
	}
	
}
