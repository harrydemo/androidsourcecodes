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
	
		/** �����Զ����µ����Ϣ��service */
		context.startService(new Intent(context,updateService.class));
	
		/** ΪAppWidget���õ���¼�����Ӧ��������ʾ�����Ϣ�����activity */ 
	    Intent startActivityIntent = new Intent(context,NewBatteryInfoActivity.class);
	    PendingIntent Pintent = PendingIntent.getActivity(context,0,startActivityIntent,0);
	    RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.newrelativelayout);
	    views.setOnClickPendingIntent(R.id.imageView,Pintent);
	    appWidgetManager.updateAppWidget(appWidgetIds,views);
			
	}
	
	/** �Զ����µ����Ϣ��service,ͨ��AlarmManagerʵ�ֶ�ʱ����ϵط��͵����Ϣ */
	public static class updateService extends Service{
		Bitmap bmp;		//���������ͼƬ
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		/** ����һ�����յ����Ϣ��broascastReceiver */
		
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
		
		/** ����Widget����ʾ */
		private void setViews()
		{
			/** ����һ��AppWidgetManager */
			AppWidgetManager manager=AppWidgetManager.getInstance(this);
			
			/** ����һ��RemoteViews��ʵ�ֶ�AppWidget������� */
			RemoteViews views=new RemoteViews(getPackageName(),R.layout.newrelativelayout);
			
			if(currentBatteryStatus==2||currentBatteryStatus==5)	//�����ڳ��������ʱ����ʾ����ͼƬ
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
			else	//δ�ڳ��ʱ����ʾ���ڳ��״̬��ϵ��ͼƬ
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
		
			/** ����AppWidget����ʾ��ͼƬ�����ֵ����� */
		    views.setImageViewBitmap(R.id.imageView,bmp);
			views.setTextViewText(R.id.tv,currentBatteryLevel+"%");
			
			ComponentName thisWidget=new ComponentName(this,NewBatteryWidget.class);

			/** ����AppWidget */
            manager.updateAppWidget(thisWidget, views);
			
		}
		
		public void onStart(Intent intent,int startId)
		{
			super.onStart(intent, startId);

			/** ע������� */
			registerReceiver(batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			
			/** �����˶���POWER_CONNECTED��DISCONNECTED�¼��ļ�����
			 * ��ʵ�ֳ��ʱ��Ϣ�Ķ�̬�仯��������ԭ����Ҫ����AlarmManager
			 * ÿ��һ�뷢�ͼ����Ϣ��ʵ�֣���Լ�˵�������ԭ����һ����µķ�����
			 * ����һҹ���ԣ�����ĵ�����Ȼռ����8%����������
			 * 
			 * */
			registerReceiver(powerConnectedReceiver,new IntentFilter(Intent.ACTION_POWER_CONNECTED));
			registerReceiver(powerDisconnectedReceiver,new IntentFilter(Intent.ACTION_POWER_DISCONNECTED ));
			
			/** ʹ��AlarmManagerʵ�֣���һ������Widgetʱ��һ���������£�
			 * �Ժ��Ϊһ���ӷ���һ�θ�����ʾ��Ϣ��ʵ����Ϣʵʱ��̬�仯��
			 * ʵ�ֽڵ繦��
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
