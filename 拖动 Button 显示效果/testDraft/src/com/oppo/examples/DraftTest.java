package com.oppo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class DraftTest extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		final int screenWidth = dm.widthPixels;
		final int screenHeight = dm.heightPixels - 50;

		final Button b=(Button)findViewById(R.id.btn);
		
		b.setOnTouchListener(new OnTouchListener(){

			int lastX, lastY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int ea=event.getAction();
				Log.i("TAG", "Touch:"+ea);

				//Toast.makeText(DraftTest.this, "Œª÷√£∫"+x+","+y, Toast.LENGTH_SHORT).show();

				switch(ea){
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					break;
					/**
					 * layout(l,t,r,b)
					 * l  Left position, relative to parent 
                    t  Top position, relative to parent 
                    r  Right position, relative to parent 
                    b  Bottom position, relative to parent  
					 * */
				case MotionEvent.ACTION_MOVE:
					int dx =(int)event.getRawX() - lastX;
					int dy =(int)event.getRawY() - lastY;
				
					int left = v.getLeft() + dx;
					int top = v.getTop() + dy;
					int right = v.getRight() + dx;
					int bottom = v.getBottom() + dy;
					
					if(left < 0){
						left = 0;
						right = left + v.getWidth();
					}
					
					if(right > screenWidth){
						right = screenWidth;
						left = right - v.getWidth();
					}
					
					if(top < 0){
						top = 0;
						bottom = top + v.getHeight();
					}
					
					if(bottom > screenHeight){
						bottom = screenHeight;
						top = bottom - v.getHeight();
					}
					
					v.layout(left, top, right, bottom);
					
					Log.i("", "position£∫" + left +", " + top + ", " + right + ", " + bottom);
   
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					break;        		
				}
				return false;
			}});
	}
}