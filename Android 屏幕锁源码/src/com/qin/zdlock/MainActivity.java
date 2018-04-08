package com.qin.zdlock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static String TAG = "QINZDLOCK";

	private SliderRelativeLayout sliderLayout = null;

	private ImageView imgView_getup_arrow; // 动画图片
	private AnimationDrawable animArrowDrawable = null;

    private Context mContext = null ;
	
    public static int MSG_LOCK_SUCESS = 1;
    
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MainActivity.this;
		/*设置全屏，无标题*/
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.main);
		initViews();
		
		startService(new Intent(MainActivity.this, ZdLockService.class));
		
		sliderLayout.setMainHandler(mHandler);
	}

	private void initViews(){    	
    	sliderLayout = (SliderRelativeLayout)findViewById(R.id.slider_layout);
    	//获得动画，并开始转动
    	imgView_getup_arrow = (ImageView)findViewById(R.id.getup_arrow);
    	animArrowDrawable = (AnimationDrawable) imgView_getup_arrow.getBackground() ;
    }

	@Override
	protected void onResume() {
		super.onResume();
		//设置动画
		mHandler.postDelayed(AnimationDrawableTask, 300);  //开始绘制动画
	}
	@Override
	protected void onPause() {
		super.onPause();
		animArrowDrawable.stop();
	}

	protected void onDestory(){
		super.onDestroy();
	}
	
	//通过延时控制当前绘制bitmap的位置坐标
	private Runnable AnimationDrawableTask = new Runnable(){
		
		public void run(){
			animArrowDrawable.start();
			mHandler.postDelayed(AnimationDrawableTask, 300);
		}
	};
	
	private Handler mHandler =new Handler (){
		
		public void handleMessage(Message msg){
			
			Log.i(TAG, "handleMessage :  #### " );
			
			if(MSG_LOCK_SUCESS == msg.what)
				finish(); // 锁屏成功时，结束我们的Activity界面
		}
	};
	
	//屏蔽掉Home键
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	    super.onAttachedToWindow();
    }
	
	//屏蔽掉Back键
	public boolean onKeyDown(int keyCode ,KeyEvent event){
		
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
			return true ;
		else
			return super.onKeyDown(keyCode, event);
		
	}

}