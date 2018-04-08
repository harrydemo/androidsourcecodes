package com.dsl.fireworks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FireworksSurface extends SurfaceView implements SurfaceHolder.Callback,Runnable{
	public Canvas cs;
	public Thread mThread;
	public SurfaceHolder mHolder;
	public int width,height;
	public List<Fires> fire_list=new ArrayList<Fires>();
	private Fires fire;
	private boolean isStart=false;
	public static boolean isEnd=false;
	private FireThread fireThread;
	
	public FireworksSurface(Context context,int width,int height) {
		super(context);
		this.width=width;
		this.height=height;
		mHolder=getHolder();
		mHolder.addCallback(this);
		fire=new Fires(this);
		fire_list.add(new Fires(this));
		fire_list.add(new Fires(this));
		fire_list.add(new Fires(this));
		fire_list.add(new Fires(this));
		fireThread=new FireThread();
		// TODO Auto-generated constructor stub
	}

	public void draw(Canvas cs){
		for(Fires f:fire_list){
			if(null==f){
				fire_list.remove(f);
			}
			else{
				if(null!=f){
					if(!f.isEnd){
					f.start(cs);
					}
					else{
						f=null;
					}
				}
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isStart){
			cs=mHolder.lockCanvas();
			cs.drawBitmap(revort(R.drawable.xingk,width,height), 0, 0, new Paint());
			draw(cs);
			mHolder.unlockCanvasAndPost(cs);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mThread=new Thread(this);
		isStart=true;
		isEnd=true;
		mThread.start();
		fireThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isStart=false;
		isEnd=false;
	}
	
	public Bitmap revort(int id, int x, int y) {

		Bitmap bm = BitmapFactory.decodeStream(getResources().openRawResource(
				id));
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth = x;
		int newHeight = y;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
		
	}

	class FireThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isEnd){
				fire_list.add(new Fires(FireworksSurface.this));
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
