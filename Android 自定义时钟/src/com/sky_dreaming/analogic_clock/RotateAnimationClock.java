package com.sky_dreaming.analogic_clock;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.sky_dreaming.analogic_clock.animation.RotateAnimation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class RotateAnimationClock extends Activity {
	/**
	 * 时分秒图标
	 */
	private ImageView hour_hands = null;
	private ImageView minute_hands = null;
	private ImageView second_hands = null;
	
	/**
	 * 时分秒动画
	 */
	private RotateAnimation second_anim = null;
	private RotateAnimation hour_anim = null;
	private RotateAnimation minute_anim = null;
	
	/**
     * 时，分，秒值
     */
    private float mHours;
    private float mMinutes;
    private float mSeconds;
    
    /**
     * 时区
     */
    //private String time_zone;
    
    private Time mCalendar;
    
    private boolean mAttached = false;
    /**
     *  该方法在view附着于window时候调用，这时，界面将要绘制，也就是说该方法必定于onDraw(Canvas)之前调用
     *  然而，该方法会在onDraw(Canvas)之前的任意时间调用，不一定在onMeasure(int, int)前后
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            
            /**
             * 注册一个消息过滤器，获取时间重置或者时区改变的action
             */
            IntentFilter filter = new IntentFilter();
            
//            filter.addAction(Intent.ACTION_TIME_TICK);
            
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            /**
             * 注册广播接收器，通过loopHandler接收信息
             */
            this.registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        mCalendar = new Time();
        /**
         * 确保将时间调到当前时间
         */
        onTimeChanged();
    }

    /**
     * 当view脱离window时候调用，此时界面不再需要绘制
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            this.unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /**
         * 程序组件初始化
         */
        initActivity();
        run();
    }

    /**
     * 程序组件初始化
     */
	private void initActivity() {
		/**
		 * Views
		 */
		hour_hands = (ImageView) findViewById(R.id.hour_hands);
        minute_hands = (ImageView) findViewById(R.id.minute_hands);
        second_hands = (ImageView) findViewById(R.id.second_hands);
        
        /**
         * 动画初始化
         */
        second_anim = new RotateAnimation(0, 0 ,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.9f);
        minute_anim = new RotateAnimation(0, 0 , Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.8f);
        hour_anim = new RotateAnimation(0, 0 , Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.7f);

        LinearInterpolator lir = new LinearInterpolator();  
        second_anim.setInterpolator(lir);
        minute_anim.setInterpolator(lir); 
        hour_anim.setInterpolator(lir);
        
        mCalendar = new Time();
        onTimeChanged();
		hour_anim.setmToDegrees(mHours / 12.0f * 360.0f);
		minute_anim.setmToDegrees(mMinutes / 60.0f * 360.0f);
		second_anim.setmToDegrees(mSeconds / 60.0f * 360.0f);
		updateViews();
		
		/**
		 * 设置动画播放时间为1秒
		 */
        second_anim.setDuration(1000);
        minute_anim.setDuration(1000);
        hour_anim.setDuration(1000);

	}
	
	/**
     * 消息传递和处理机制
     */
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {     
    		switch (msg.what) {     
    		case 1:     
    			updateViews();
    		break;     
    		}     
    		super.handleMessage(msg);  
    	}

		
    };
    
    /**
     * 更新时分秒指针
     */
    private void updateViews() {
		float hour_ToDegrees = hour_anim.getmToDegrees();
    	if(hour_ToDegrees == 360)
    	{
    		hour_ToDegrees = 0;
    	}
		hour_anim.setmFromDegrees(hour_ToDegrees);
		
		float minute_ToDegrees = minute_anim.getmToDegrees();
    	if(minute_ToDegrees == 360)
    	{
    		minute_ToDegrees = 0;
    	}
		minute_anim.setmFromDegrees(minute_ToDegrees);
		
		float second_ToDegrees = second_anim.getmToDegrees();
    	if(second_ToDegrees == 360)
    	{
    		second_ToDegrees = 0;
    	}
		second_anim.setmFromDegrees(second_ToDegrees);
		
	
		onTimeChanged();

		if(mHours == 0)
		{
			hour_anim.setmToDegrees(360);
		}
		else
			hour_anim.setmToDegrees(mHours / 12.0f * 360.0f);
		
		if(mMinutes == 0)
		{
			minute_anim.setmToDegrees(360);
		}
		else
			minute_anim.setmToDegrees(mMinutes / 60.0f * 360.0f);
		
		if(mSeconds == 0)
		{
			second_anim.setmToDegrees(360);
		}
		else
			second_anim.setmToDegrees(mSeconds / 60.0f * 360.0f);
		
		/**
		 * 播放动画
		 */
		hour_hands.startAnimation(hour_anim);
		minute_hands.startAnimation(minute_anim);
		second_hands.startAnimation(second_anim);
	}
	 /**
     * 时钟运行
     */
    private void run()
    {
    	timer.schedule(clockTask,1000, 1000); 
    }
    

    private Timer timer = new Timer(true);
    
    private TimerTask clockTask = new TimerTask(){

		@Override
		public void run() {
			
			Message message = new Message();     
			message.what = 1;     
			mHandler.sendMessage(message); 
		}
    };
    
    /**
     * 处理时间更新事件
     */
    private void onTimeChanged() {
    	/**
    	 * 更新时间到当前
    	 */
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        
        mSeconds = second;
        mMinutes = minute + second / 60.0f;
        mHours = hour + mMinutes / 60.0f;
    }
    
    
    /**
     * 接收日期重置，时区变化时的事件
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	String tz = "";
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
                //time_zone = mCalendar.timezone;
            }
            Log.i("********************",tz);

            updateViews();
        }
    };
}