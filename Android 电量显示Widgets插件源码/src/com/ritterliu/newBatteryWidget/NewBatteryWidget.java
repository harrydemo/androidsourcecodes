package com.ritterliu.newBatteryWidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NewBatteryWidget extends AppWidgetProvider{
	
	private static int currentBatteryLevel;
	private static int currentBatteryStatus;

	private static boolean firstTimeToCreate=true;
	
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	
		/** 启动自动更新电池信息的service */
		context.startService(new Intent(context,updateService.class));
	
		/** 为AppWidget设置点击事件的响应，启动显示电池信息详情的activity */ 
	    Intent startActivityIntent = new Intent(context,NewBatteryInfoActivity.class);
	    PendingIntent Pintent = PendingIntent.getActivity(context,0,startActivityIntent,0);
	    RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.newrelativelayout);
	    views.setOnClickPendingIntent(R.id.imageView,Pintent);
	    appWidgetManager.updateAppWidget(appWidgetIds,views);
			
	}
	
	/** 自动更新电池信息的service,通过AlarmManager实现定时不间断地发送电池信息 */
	public static class updateService extends Service{
		Bitmap bmp;		//定义机器人图片
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		/** 定义一个接收电池信息的broascastReceiver */
		
		private BroadcastReceiver batteryReceiver=new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				currentBatteryLevel=intent.getIntExtra("level", 0);
				currentBatteryStatus=intent.getIntExtra("status", 0);
			}
			
		};
		
		private BroadcastReceiver powerConnectedReceiver=new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				setViews();	
			}
		};
		
		private BroadcastReceiver powerDisconnectedReceiver=new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				setViews();

			}
		};
		
		/** 设置Widget的显示 */
		private void setViews()
		{
			/** 定义一个AppWidgetManager */
			AppWidgetManager manager=AppWidgetManager.getInstance(this);
			
			/** 定义一个RemoteViews，实现对AppWidget界面控制 */
			RemoteViews views=new RemoteViews(getPackageName(),R.layout.newrelativelayout);
			
			if(currentBatteryStatus==2||currentBatteryStatus==5)	//当正在充电或充满电时，显示充电的图片
			{
			    if(currentBatteryLevel>=95)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.jcharge);
			    }
			    else if(currentBatteryLevel>=85&& currentBatteryLevel<95)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.icharge);
			    }
			    else if(currentBatteryLevel>=75&& currentBatteryLevel<85)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.hcharge);
			    }
			    else if(currentBatteryLevel>=65&& currentBatteryLevel<75)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.gcharge);
			    }
			    else if(currentBatteryLevel>=55&& currentBatteryLevel<65)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.fcharge);
			    }
			    else if(currentBatteryLevel>=45&& currentBatteryLevel<55)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.echarge);	
			    }
			    else if(currentBatteryLevel>=35&& currentBatteryLevel<45)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.dcharge);	
			    }
			    else if(currentBatteryLevel>=25&& currentBatteryLevel<35)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.ccharge);	
			    }
			    else if(currentBatteryLevel>=15&& currentBatteryLevel<25)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.bcharge);
			    }
			    else
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.acharge);	
			    }   
			}
			else	//未在充电时，显示不在充电状态的系列图片
			{
			    if(currentBatteryLevel>=95)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.j);
			    }
			    else if(currentBatteryLevel>=85&& currentBatteryLevel<95)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.i);
			    }
			    else if(currentBatteryLevel>=75&& currentBatteryLevel<85)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.h);
			    }
			    else if(currentBatteryLevel>=65&& currentBatteryLevel<75)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.g);
			    }
			    else if(currentBatteryLevel>=55&& currentBatteryLevel<65)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.f);
			    }
			    else if(currentBatteryLevel>=45&& currentBatteryLevel<55)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.e);	
			    }
			    else if(currentBatteryLevel>=35&& currentBatteryLevel<45)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.d);	
			    }
			    else if(currentBatteryLevel>=25&& currentBatteryLevel<35)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.c);	
			    }
			    else if(currentBatteryLevel>=15&& currentBatteryLevel<25)
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.b);
			    }
			    else
			    {
			    	bmp=BitmapFactory.decodeResource(getResources(),R.drawable.a);	
			    }   
			}   
		
			/** 设置AppWidget上显示的图片和文字的内容 */
		    views.setImageViewBitmap(R.id.imageView,bmp);
			views.setTextViewText(R.id.tv,currentBatteryLevel+"%");
			
			ComponentName thisWidget=new ComponentName(this,NewBatteryWidget.class);

			/** 更新AppWidget */
            manager.updateAppWidget(thisWidget, views);
			
		}
		
		public void onStart(Intent intent,int startId)
		{
			super.onStart(intent, startId);

			/** 注册接收器 */
			registerReceiver(batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			
			/** 增加了对于POWER_CONNECTED和DISCONNECTED事件的监听，
			 * 以实现充电时信息的动态变化，避免了原来需要依赖AlarmManager
			 * 每隔一秒发送检测信息来实现，节约了电量，用原来隔一秒更新的方法，
			 * 经过一夜测试，插件耗电量居然占到了8%，汗。。。
			 * 
			 * */
			registerReceiver(powerConnectedReceiver,new IntentFilter(Intent.ACTION_POWER_CONNECTED));
			registerReceiver(powerDisconnectedReceiver,new IntentFilter(Intent.ACTION_POWER_DISCONNECTED ));
			
			/** 使用AlarmManager实现，第一次启动Widget时隔一秒立即更新，
			 * 以后均为一分钟发送一次更新提示信息，实现信息实时动态变化，
			 * 实现节电功能
			 *  */
			long now=System.currentTimeMillis();
			long pause;
			
			if(firstTimeToCreate)
			{
				firstTimeToCreate=false;
				pause=1000;
			}
			else
			{
				pause=1000*60*2;
			}
			
			Intent alarmIntent=new Intent();
			alarmIntent=intent;
			
			PendingIntent pendingIntent=PendingIntent.getService(this, 0, alarmIntent, 0);
			AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
			alarm.set(AlarmManager.RTC_WAKEUP,now+pause,pendingIntent);
			
			setViews();

		}	
	}
}
