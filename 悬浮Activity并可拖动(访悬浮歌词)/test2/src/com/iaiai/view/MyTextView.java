package com.iaiai.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 
 * <p>
 * Title: MyTextView.java
 * </p>
 * <p>
 * E-Mail: 176291935@qq.com
 * </p>
 * <p>
 * QQ: 176291935
 * </p>
 * <p>
 * Http: iaiai.iteye.com
 * </p>
 * <p>
 * Create time: 2011-9-28
 * </p>
 * 
 * @author 丸子
 * @version 0.0.1
 */
public class MyTextView extends TextView {
	private final String TAG = MyTextView.class.getSimpleName();
	
	public static int TOOL_BAR_HIGH = 0;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams(); 
	private float startX;
	private float startY;
	private float x;
	private float y;
	
	private String text;
	
	WindowManager wm = (WindowManager)getContext().getApplicationContext().getSystemService(getContext().WINDOW_SERVICE);
 
	public MyTextView(Context context) {
		super(context);
		text = "世上只有妈妈好,有妈的孩子像块宝";
		this.setBackgroundColor(Color.argb(90, 150, 150, 150));
		handler = new Handler();
//		handler.post(update);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//触摸点相对于屏幕左上角坐标
		x = event.getRawX();   
	    y = event.getRawY() - TOOL_BAR_HIGH;
	    Log.d(TAG, "------X: "+ x +"------Y:" + y);
	    
	    switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		startX = event.getX();
	    		startY = event.getY();
	    		break;
	    	case MotionEvent.ACTION_MOVE:
	    		updatePosition();
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		updatePosition();
	    		startX = startY = 0;
	    		break;
	    }
	    
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float1 += 0.001f;
		float2 += 0.001f;	
		
		if(float2 > 1.0){
			float1 = 0.0f;
			float2 = 0.01f;
		}
		this.setText("");
		float len = this.getTextSize() * text.length();
		Shader shader = new LinearGradient(0, 0, len, 0, 
				new int[] { Color.YELLOW, Color.RED },  new float[]{float1, float2},
				TileMode.CLAMP);
		Paint p = new Paint();
		p.setShader(shader);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(text, 0, 10, p);
	}
	
	private Runnable update = new Runnable() {
        public void run() {
        	MyTextView.this.update();
        	handler.postDelayed(update, 5);
        }
    };
	
	private void update(){
		postInvalidate();	//更新界面
	}
	
	private float float1 = 0.0f;
	private float float2 = 0.01f;
	
	private Handler handler;	

	//更新浮动窗口位置参数
	 private void updatePosition(){
		 // View的当前位置
		 params.x = (int)( x - startX);
		 params.y = (int) (y - startY);
		 wm.updateViewLayout(this, params);
	 }
}
