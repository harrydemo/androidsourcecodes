package com.yarin.android.RingProfile;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;

public class RingProfile extends TabActivity
{
	private static final String TAG="RingToggle";
	
	//声明TabHost对象
	private TabHost mTabHost;

	protected boolean isChange;
	
	private AlarmManager mAlarmManager;
	
	private TimePicker mTimePicker ;
	
	private int mTab;
	
	private ImageView			myImage;
	private ImageButton			downButton;
	private ImageButton			upButton;
	private ImageButton			normalButton;
	private ImageButton			muteButton;
	private ImageButton			vibrateButton;
	private ProgressBar			myProgress;
	private AudioManager		audioMa;
	private int					volume	= 0;
	  
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//取得TabHost对象
		mTabHost = getTabHost();
	    
		/* 为TabHost添加标签 */
		//新建一个newTabSpec(newTabSpec)
		//设置其标签和图标(setIndicator)
		//设置内容(setContent)
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
	    		.setIndicator("普通情景模式",getResources().getDrawable(R.drawable.icon))
	    		.setContent(R.id.RadioGroup01));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
	    		.setIndicator("定时情景模式",getResources().getDrawable(R.drawable.timeprofile))
	    		.setContent(R.id.RelativeLayout01));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
	    		.setIndicator("自定义情景模式",getResources().getDrawable(R.drawable.addprofile))
	    		.setContent(R.id.AbsoluteLayout03));
        
	    //设置TabHost的背景图片资源
	    mTabHost.setBackgroundResource(R.drawable.bg);
	    
	    //设置当前显示哪一个标签
	    mTabHost.setCurrentTab(0);
	    mTab = 0;
	    updateRadioGroup();
	    
	    //初始化，取得AudioManager
	    audioMa = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	    myImage = (ImageView)findViewById(R.id.myImage); 
	    myProgress = (ProgressBar)findViewById(R.id.myProgress); 
	    downButton = (ImageButton)findViewById(R.id.downButton); 
	    upButton = (ImageButton)findViewById(R.id.upButton); 
	    normalButton = (ImageButton)findViewById(R.id.normalButton); 
	    muteButton = (ImageButton)findViewById(R.id.muteButton); 
	    vibrateButton = (ImageButton)findViewById(R.id.vibrateButton); 
	    
	    
	    //标签切换事件处理，setOnTabChangedListener 
	    mTabHost.setOnTabChangedListener(new OnTabChangeListener()
	    {
            @Override
            public void onTabChanged(String tabId) 
            {
            	if ( tabId.equals("tab_test1") )
				{
            		mTab = 0;
            		
                    mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    mTimePicker = (TimePicker)findViewById(R.id.timePkr);
                    mTimePicker.setIs24HourView(true);
                    
                    
            		updateRadioGroup();
				}
            	else if ( tabId.equals("tab_test2") )
				{
            		mTab = 1;
                    mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    mTimePicker = (TimePicker)findViewById(R.id.timePkr);
                    mTimePicker.setIs24HourView(true);
                    
            		updateRadioGroup();
				}
            }            
        });
        
        /***************************************************************/
        RadioGroup group1 = (RadioGroup) findViewById(R.id.RadioGroup01);
        group1.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
        	public void onCheckedChanged(RadioGroup group, int checkedId) 
        	{
            	if (isChange)
            		return;             
            	switch (checkedId)
            	{               
            		case R.id.ring_and_vibrate01: ringAndVibrate(); break;               
            		case R.id.ring01: ring(); break;                   
            		case R.id.vibrate01: vibrate(); break;                              
            		case R.id.silent01: silent(); break;               
            	}              
            	RadioButton radio = (RadioButton) findViewById(checkedId);               
            	if (radio != null)            	
            		radio.setTextSize(30);              
        	}	
        });
        //RadioButton添加监听器
        for (int i = 0, l = group1.getChildCount(); i < l; i++) 
        {   
        	RadioButton radio = (RadioButton) group1.getChildAt(i);
            radio.setOnTouchListener(new OnTouchListener() 
            {               
            	public boolean onTouch(View v, MotionEvent event)                 
            	{        				
            		RadioButton radio = (RadioButton) v;               	
            		if (!radio.isChecked())                  		
            			return false;             		
            		radio.setTextSize(30);                                   		                                        		
            		return false;                                        	
            	}               
            });
        }
        /***************************************************************/
        //添加onChangeListener
        RadioGroup group2 = (RadioGroup) findViewById(R.id.RadioGroup02);
        group2.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
        	public void onCheckedChanged(RadioGroup group, int checkedId) 
        	{
            	if (isChange)
            		return;             
            	switch (checkedId)
            	{               
            		case R.id.ring_and_vibrate02: ringAndVibrate(); break;               
            		case R.id.ring02: ring(); break;                   
            		case R.id.vibrate02: vibrate(); break;                              
            		case R.id.silent02: silent(); break;               
            	}              
            	RadioButton radio = (RadioButton) findViewById(checkedId);               
            	if (radio != null)            	
            		radio.setTextSize(30);              
        	}	
        });
        //RadioButton添加监听器
        for (int i = 0, l = group2.getChildCount(); i < l; i++) 
        {   
        	RadioButton radio = (RadioButton) group2.getChildAt(i);
            radio.setOnTouchListener(new OnTouchListener() 
            {               
            	public boolean onTouch(View v, MotionEvent event)                 
            	{        				
            		RadioButton radio = (RadioButton) v;               	
            		if (!radio.isChecked())                  		
            			return false;             		
            		radio.setTextSize(30);                                   		                                        		
            		return false;                                        	
            	}               
            });
        }
        
        //取得手机的初始音量，并初始化进度条
        volume=audioMa.getStreamVolume(AudioManager.STREAM_RING); 
        myProgress.setProgress(volume);
        //取得初始模式，并分别设置图标
        int mode=audioMa.getRingerMode();
        if(mode==AudioManager.RINGER_MODE_NORMAL)
        {
          myImage.setImageDrawable(getResources()
                                   .getDrawable(R.drawable.icon));
        }
        else if(mode==AudioManager.RINGER_MODE_SILENT)
        {
          myImage.setImageDrawable(getResources()
                                   .getDrawable(R.drawable.mute));
        }
        else if(mode==AudioManager.RINGER_MODE_VIBRATE)
        {
          myImage.setImageDrawable(getResources()
                                   .getDrawable(R.drawable.vibrate));
        }
        //降低音量按键
        downButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          {
        	//adjustVolume可以增加和降低音量
            audioMa.adjustVolume(AudioManager.ADJUST_LOWER, 0); 
            volume=audioMa.getStreamVolume(AudioManager.STREAM_RING);
            //设置进度条
            myProgress.setProgress(volume);
            //设置图标
            int mode=audioMa.getRingerMode();
            if(mode==AudioManager.RINGER_MODE_NORMAL)
            {
              myImage.setImageDrawable(getResources()
                                      .getDrawable(R.drawable.icon));
            }
            else if(mode==AudioManager.RINGER_MODE_SILENT)
            {
              myImage.setImageDrawable(getResources()
                                       .getDrawable(R.drawable.mute));
            }
            else if(mode==AudioManager.RINGER_MODE_VIBRATE)
            {
              myImage.setImageDrawable(getResources()
                                      .getDrawable(R.drawable.vibrate));
            }
          } 
        }); 
        //提高音量
        upButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          { 
        	  //AudioManager.ADJUST_RAISE提高音量
            audioMa.adjustVolume(AudioManager.ADJUST_RAISE, 0);
            volume=audioMa.getStreamVolume(AudioManager.STREAM_RING);
            myProgress.setProgress(volume);
            int mode=audioMa.getRingerMode();
            if(mode==AudioManager.RINGER_MODE_NORMAL)
            {
              myImage.setImageDrawable(getResources()
                                       .getDrawable(R.drawable.icon));
            }
            else if(mode==AudioManager.RINGER_MODE_SILENT)
            {
              myImage.setImageDrawable(getResources()
                                       .getDrawable(R.drawable.mute));
            }
            else if(mode==AudioManager.RINGER_MODE_VIBRATE)
            {
              myImage.setImageDrawable(getResources()
                                      .getDrawable(R.drawable.vibrate));
            }
          } 
        }); 
        //正常状态
        normalButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          {
            audioMa.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            volume=audioMa.getStreamVolume(AudioManager.STREAM_RING); 
            myProgress.setProgress(volume);
            myImage.setImageDrawable(getResources()
                                     .getDrawable(R.drawable.icon));
          } 
        });
        //静音状态
        muteButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          { 
            audioMa.setRingerMode(AudioManager.RINGER_MODE_SILENT); 
            volume=audioMa.getStreamVolume(AudioManager.STREAM_RING);
            myProgress.setProgress(volume);
            myImage.setImageDrawable(getResources()
                                     .getDrawable(R.drawable.mute)); 
          } 
        }); 
        //振动状态
        vibrateButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          { 
            audioMa.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); 
            volume=audioMa.getStreamVolume(AudioManager.STREAM_RING); 
            myProgress.setProgress(volume);
            myImage.setImageDrawable(getResources()
                                     .getDrawable(R.drawable.vibrate)); 
          } 
        }); 
	}

	// 更新情景模式
	protected void updateRadioGroup()
	{
		int checkedId = currentMode();
		RadioButton checked = (RadioButton) findViewById(checkedId);
		isChange = true;
		checked.setChecked(true);
		isChange = false;
	}


	// 取得当前情景模式
	protected int currentMode()
	{
		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		switch (audio.getRingerMode())
		{
			case AudioManager.RINGER_MODE_SILENT:
				if ( mTab == 0 )
				{
					return R.id.silent01;
				}
				else
				{
					return R.id.silent02;
				}
				
			case AudioManager.RINGER_MODE_VIBRATE:
				if ( mTab == 0 )
				{
					return R.id.vibrate01;
				}
				else
				{
					return R.id.vibrate02;
				}
		}

		if (audio.shouldVibrate(AudioManager.VIBRATE_TYPE_RINGER))
		{
			if ( mTab == 0 )
			{
				return R.id.ring_and_vibrate01;
			}
			else
			{
				return R.id.ring_and_vibrate02;
			}
		}
		if ( mTab == 0 )
		{
			return R.id.ring01;
		}
		else
		{
			return R.id.ring02;
		}
	}
	/***************************************************************/
	// 铃声和震动
	protected void ringAndVibrate()
	{
		Intent intent = new Intent(RingBroadcastReceiver.RV_CHANGED);
		if ( mTab == 0 )
		{
			intent.putExtra("checkedId", R.id.ring_and_vibrate01);
		}
		else
		{
			intent.putExtra("checkedId", R.id.ring_and_vibrate02);
		}
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, RingBroadcastReceiver.REQUEST_CODE, intent, 0);
		Log.e(TAG, "" + intent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, getTime(), alarmIntent);
	}


	// 铃声
	protected void ring()
	{
		Intent intent = new Intent(RingBroadcastReceiver.RING_CHANGED);
		if ( mTab == 0 )
		{
			intent.putExtra("checkedId", R.id.ring01);
		}
		else
		{
			intent.putExtra("checkedId", R.id.ring02);
		}
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, RingBroadcastReceiver.REQUEST_CODE, intent, 0);
		Log.e(TAG, "" + intent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, getTime(), alarmIntent);
	}


	// 震动
	protected void vibrate()
	{
		Intent intent = new Intent(RingBroadcastReceiver.VIBRATE_CHANGED);
		if ( mTab == 0 )
		{
			intent.putExtra("checkedId", R.id.vibrate01);
		}
		else
		{
			intent.putExtra("checkedId", R.id.vibrate02);
		}
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, RingBroadcastReceiver.REQUEST_CODE, intent, 0);
		Log.e(TAG, "" + intent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, getTime(), alarmIntent);
	}


	// 静音
	protected void silent()
	{
		Intent intent = new Intent(RingBroadcastReceiver.SILENT_CHANGED);
		if ( mTab == 0 )
		{
			intent.putExtra("checkedId", R.id.silent01);
		}
		else
		{
			intent.putExtra("checkedId", R.id.silent02);
		}
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, RingBroadcastReceiver.REQUEST_CODE, intent, 0);
		Log.e(TAG, "" + intent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, getTime(), alarmIntent);
	}
	
	// 计算切换时间
	private long getTime()
	{
		Date dateNow = new Date();
		long hour = mTimePicker.getCurrentHour() - dateNow.getHours();
		long min = mTimePicker.getCurrentMinute() - dateNow.getMinutes();
		long second = dateNow.getSeconds();
		return dateNow.getTime() + (hour * 60 + min) * 60 * 1000 - second * 1000;
	}
	
	/***********************************************************************************/
}
