package com.iaiai;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.iaiai.view.MyTextView;

/**
 * 
 * <p>
 * Title: TopFrame.java
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
public class TopFrame extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.bt);
		
		button.setOnClickListener(onclick);
	}
	
	private MyTextView tv = null;
	
	OnClickListener onclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(tv != null && tv.isShown()){
				WindowManager wm = (WindowManager)getApplicationContext().getSystemService(TopFrame.this.WINDOW_SERVICE);
				wm.removeView(tv);
			}
			show();
			
		}
	};
	
	
	private void show(){
		Rect frame = new Rect();  
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		MyTextView.TOOL_BAR_HIGH = frame.top;  
		
		WindowManager wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params = MyTextView.params;
		
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		
		params.width = WindowManager.LayoutParams.FILL_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.alpha = 80;
		
		params.gravity=Gravity.LEFT|Gravity.TOP;
	    //以屏幕左上角为原点，设置x、y初始值
		params.x = 0;
		params.y = 0;
	        
		tv = new MyTextView(TopFrame.this);
		wm.addView(tv, params);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
		
//		if(tv != null && tv.isShown()){
//			wm.removeView(tv);
//		}
		super.onDestroy();
	}
	
	
	
}