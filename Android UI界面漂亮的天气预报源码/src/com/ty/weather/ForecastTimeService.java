/**
 * 
 */
package com.ty.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author 088926
 *
 */
public class ForecastTimeService extends Service {
	private static final String TAG = "ForecastTimeService";	

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d(TAG, "start service Time update");
        
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		
		RemoteViews updateViews = ForecastWidget.updateTime(this);
		
		if (updateViews != null){
			manager.updateAppWidget(manager.getAppWidgetIds(new ComponentName(this,
					ForecastWidget.class)), updateViews);
		}
		
		// 设置下次执行时间,每隔20秒刷新一次
		long now = System.currentTimeMillis();
		long updateMilis = 20 * 1000;
		
		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				intent, 0);

		// Schedule alarm, and force the device awake for this update
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, now + updateMilis, pendingIntent);

		// No updates remaining, so stop service
		stopSelf();
	}

}
