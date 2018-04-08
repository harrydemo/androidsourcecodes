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
	
	//����TabHost����
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
		
		//ȡ��TabHost����
		mTabHost = getTabHost();
	    
		/* ΪTabHost��ӱ�ǩ */
		//�½�һ��newTabSpec(newTabSpec)
		//�������ǩ��ͼ��(setIndicator)
		//��������(setContent)
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
	    		.setIndicator("��ͨ�龰ģʽ",getResources().getDrawable(R.drawable.icon))
	    		.setContent(R.id.RadioGroup01));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
	    		.setIndicator("��ʱ�龰ģʽ",getResources().getDrawable(R.drawable.timeprofile))
	    		.setContent(R.id.RelativeLayout01));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
	    		.setIndicator("�Զ����龰ģʽ",getResources().getDrawable(R.drawable.addprofile))
	    		.setContent(R.id.AbsoluteLayout03));
        
	    //����TabHost�ı���ͼƬ��Դ
	    mTabHost.setBackgroundResource(R.drawable.bg);
	    
	    //���õ�ǰ��ʾ��һ����ǩ
	    mTabHost.setCurrentTab(0);
	    mTab = 0;
	    updateRadioGroup();
	    
	    //��ʼ����ȡ��AudioManager
	    audioMa = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	    myImage = (ImageView)findViewById(R.id.myImage); 
	    myProgress = (ProgressBar)findViewById(R.id.myProgress); 
	    downButton = (ImageButton)findViewById(R.id.downButton); 
	    upButton = (ImageButton)findViewById(R.id.upButton); 
	    normalButton = (ImageButton)findViewById(R.id.normalButton); 
	    muteButton = (ImageButton)findViewById(R.id.muteButton); 
	    vibrateButton = (ImageButton)findViewById(R.id.vibrateButton); 
	    
	    
	    //��ǩ�л��¼�����setOnTabChangedListener 
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
        //RadioButton��Ӽ�����
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
        //���onChangeListener
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
        //RadioButton��Ӽ�����
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
        
        //ȡ���ֻ��ĳ�ʼ����������ʼ��������
        volume=audioMa.getStreamVolume(AudioManager.STREAM_RING); 
        myProgress.setProgress(volume);
        //ȡ�ó�ʼģʽ�����ֱ�����ͼ��
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
        //������������
        downButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          {
        	//adjustVolume�������Ӻͽ�������
            audioMa.adjustVolume(AudioManager.ADJUST_LOWER, 0); 
            volume=audioMa.getStreamVolume(AudioManager.STREAM_RING);
            //���ý�����
            myProgress.setProgress(volume);
            //����ͼ��
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
        //�������
        upButton.setOnClickListener(new Button.OnClickListener() 
        { 
          @Override 
          public void onClick(View arg0) 
          { 
        	  //AudioManager.ADJUST_RAISE�������
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
        //����״̬
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
        //����״̬
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
        //��״̬
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

	// �����龰ģʽ
	protected void updateRadioGroup()
	{
		int checkedId = currentMode();
		RadioButton checked = (RadioButton) findViewById(checkedId);
		isChange = true;
		checked.setChecked(true);
		isChange = false;
	}


	// ȡ�õ�ǰ�龰ģʽ
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
	// ��������
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


	// ����
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


	// ��
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


	// ����
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
	
	// �����л�ʱ��
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
