package com.bao.asin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class AsinSurfaceView extends SurfaceView implements Callback {
	ShowThread showThread;
	int x = 0;
	Bitmap bitmap;
	int A = 50;
	Paint paint;
	class ShowThread extends Thread{
		private AsinSurfaceView asinSurfaceView;
		private SurfaceHolder surfaceHolder;
		boolean flag = true;
		int sleepTime = 200;
		
		public ShowThread(AsinSurfaceView asinSurfaceView, SurfaceHolder surfaceHolder){
			this.asinSurfaceView = asinSurfaceView;
			this.surfaceHolder = surfaceHolder;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(flag == true){
				
				
				Canvas canvas = surfaceHolder.lockCanvas(null);
			
				onDraw(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
				
				asinSurfaceView.x += 5;
				
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public AsinSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		showThread = new ShowThread(this,getHolder());
		getHolder().addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(480, 160);
		
	}

	
	public void onDraw(Canvas canvas){
		if(x >= 480 || x == 0){
			x = 0;
			canvas.drawColor(Color.BLACK);// 清除画布  

		}
		else{
			if(x == 5){
				canvas.drawColor(Color.BLACK);// 清除画布  

			}
			int y = (int)(A*Math.sin(x/180.0f*Math.PI));
			System.out.println("==================y:"+y+"===x:"+x);
			canvas.drawPoint(x, y+50, paint);
		}
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		showThread.start();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
}
