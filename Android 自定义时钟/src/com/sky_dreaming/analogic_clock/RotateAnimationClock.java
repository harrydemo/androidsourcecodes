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
	 * ʱ����ͼ��
	 */
	private ImageView hour_hands = null;
	private ImageView minute_hands = null;
	private ImageView second_hands = null;
	
	/**
	 * ʱ���붯��
	 */
	private RotateAnimation second_anim = null;
	private RotateAnimation hour_anim = null;
	private RotateAnimation minute_anim = null;
	
	/**
     * ʱ���֣���ֵ
     */
    private float mHours;
    private float mMinutes;
    private float mSeconds;
    
    /**
     * ʱ��
     */
    //private String time_zone;
    
    private Time mCalendar;
    
    private boolean mAttached = false;
    /**
     *  �÷�����view������windowʱ����ã���ʱ�����潫Ҫ���ƣ�Ҳ����˵�÷����ض���onDraw(Canvas)֮ǰ����
     *  Ȼ�����÷�������onDraw(Canvas)֮ǰ������ʱ����ã���һ����onMeasure(int, int)ǰ��
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            
            /**
             * ע��һ����Ϣ����������ȡʱ�����û���ʱ���ı��action
             */
            IntentFilter filter = new IntentFilter();
            
//            filter.addAction(Intent.ACTION_TIME_TICK);
            
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            /**
             * ע��㲥��������ͨ��loopHandler������Ϣ
             */
            this.registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        mCalendar = new Time();
        /**
         * ȷ����ʱ�������ǰʱ��
         */
        onTimeChanged();
    }

    /**
     * ��view����windowʱ����ã���ʱ���治����Ҫ����
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
         * ���������ʼ��
         */
        initActivity();
        run();
    }

    /**
     * ���������ʼ��
     */
	private void initActivity() {
		/**
		 * Views
		 */
		hour_hands = (ImageView) findViewById(R.id.hour_hands);
        minute_hands = (ImageView) findViewById(R.id.minute_hands);
        second_hands = (ImageView) findViewById(R.id.second_hands);
        
        /**
         * ������ʼ��
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
		 * ���ö�������ʱ��Ϊ1��
		 */
        second_anim.setDuration(1000);
        minute_anim.setDuration(1000);
        hour_anim.setDuration(1000);

	}
	
	/**
     * ��Ϣ���ݺʹ������
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
     * ����ʱ����ָ��
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
		 * ���Ŷ���
		 */
		hour_hands.startAnimation(hour_anim);
		minute_hands.startAnimation(minute_anim);
		second_hands.startAnimation(second_anim);
	}
	 /**
     * ʱ������
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
     * ����ʱ������¼�
     */
    private void onTimeChanged() {
    	/**
    	 * ����ʱ�䵽��ǰ
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
     * �����������ã�ʱ���仯ʱ���¼�
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