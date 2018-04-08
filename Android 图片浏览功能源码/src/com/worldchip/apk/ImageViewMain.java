package com.worldchip.apk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageViewMain extends Activity implements OnGestureListener{

	 Bitmap bitmap=null;
	 ImageView image=null;
	 private GestureDetector gestureScanner;
	 @Override  
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    
	    //��title    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);    
	    //ȫ��    
	    getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,      
	                      WindowManager.LayoutParams. FLAG_FULLSCREEN);    
	       
	    setContentView(R.layout.image_view);  
	    
	    image = (ImageView)this.findViewById(R.id.imageview);
	    
	    Intent intent = getIntent();  
        String path = intent.getStringExtra("path");  
        Log.i("ImageGridView_onCreate", "path="+path);
        
        try
		{
        	bitmap=BitmapFactory.decodeFile(path);
        	image.setImageBitmap(bitmap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Log.i("ImageViewMain", "error");
		}
		
		gestureScanner = new GestureDetector(this);
		gestureScanner.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
			
		     public boolean onDoubleTap(MotionEvent e) {
		        int bmpWidth=bitmap.getWidth();
		        int bmpHeight=bitmap.getHeight();
			    float scale=1.25f;
					
				Matrix matrix=new Matrix();
				matrix.postScale(scale, scale);
				Bitmap resizeBmp=Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,bmpHeight, matrix, true );
				image.setImageBitmap(resizeBmp);
				
		      // ˫��ʱ����һ��
		      Log.i("setOnDoubleTapListener", "--code comes here---");
		      return false;
		     }

			
			public boolean onDoubleTapEvent(MotionEvent e) {
				// TODO Auto-generated method stub
				
				 Log.i("onDoubleTapEvent", "--code comes here---");
				
				return false;
			}

			
			public boolean onSingleTapConfirmed(MotionEvent e) {
				 Log.i("onSingleTapConfirmed", "--code comes here---");
				// TODO Auto-generated method stub
				return false;
			}
		});
	 }
		    

	public boolean onDown(MotionEvent arg0) {
		
		Log.i("onDown", "--code comes here---");
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		
		Log.i("onFling", "--code comes here---");
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent arg0) {
		
		Log.i("onLongPress", "--code comes here---");
		// TODO Auto-generated method stub
		
	}
	
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		
		Log.i("onScroll", "--code comes here---");
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent arg0) {
		
		Log.i("onShowPress", "--code comes here---");
		// TODO Auto-generated method stub
		
	}
	
	public boolean onSingleTapUp(MotionEvent arg0) {
		
		Log.i("onSingleTapUp", "--code comes here---");
		// TODO Auto-generated method stub
		return false;
	}
}
