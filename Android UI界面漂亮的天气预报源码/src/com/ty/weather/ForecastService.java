/**
 * 
 */
package com.ty.weather;

import java.util.LinkedList;
import java.util.Queue;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.ty.weather.ForecastProvider.WeatherWidgets;
import com.ty.weather.WebServiceHelper.ForecastParseException;
import com.ty.weather.util.WidgetEntity;

/**
 * @author TANGYONG
 * @version 1.0 2010-05-21
 */
public class ForecastService extends Service implements Runnable {
	private static String TAG = "ForecastService";

	// 所需更新的桌面小控件的ID队列
	private static Queue<Integer> requestWidgetIDs = new LinkedList<Integer>();
	// 用于维护桌面小控件的ID队列
	private static Object sLock = new Object();
	// 控制线程运行的变量
	private static boolean isThreadRun = false;
	// 所需更新的Widget
	public static final String ACTION_UPDATE_ALL = "com.ty.weather.UPDATE_ALL";
	// projection
	public static final String[] widgetProjection = new String[] {
			WidgetEntity.IS_CONFIGURED, WidgetEntity.LAST_UPDATE_TIME,
			WidgetEntity.UPDATE_MILIS };

	/**
	 * function addWedgetIDs 向队列中添加桌面小控件ID
	 */
	public static void addWidgetIDs(int[] widgetIDs) {
		synchronized (sLock) {
			for (int id : widgetIDs) {
				Log.d(TAG, "add widget ID:" + id);
				requestWidgetIDs.add(id);
			}
		}
	}

	/**
	 * function hasMoreWidgetIds 判断队列中是否还有桌面小控件ID
	 */
	public static boolean hasMoreWidgetIDs() {
		synchronized (sLock) {
			boolean hasMore = !requestWidgetIDs.isEmpty();
			if (!hasMore) {
				isThreadRun = hasMore;
			}
			return hasMore;
		}
	}

	/**
	 * 获取队列中的桌面小控件ID
	 */
	public static Integer nextWidgetIDs() {
		synchronized (sLock) {
			if (requestWidgetIDs.peek() != null) {
				return requestWidgetIDs.poll();
			} else {
				return AppWidgetManager.INVALID_APPWIDGET_ID;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Log.d(TAG, "service started");
		
		if (ACTION_UPDATE_ALL.equals(intent.getAction())) {
			Log.d(TAG, "Requested UPDATE_ALL action");
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			addWidgetIDs(manager.getAppWidgetIds(new ComponentName(this,
					ForecastWidget.class)));
		}

		// Only start processing thread if not already running
		synchronized (sLock) {
			if (!isThreadRun) {
				isThreadRun = true;
				new Thread(this).start();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		Log.d(TAG, "the ForecastService Thread Run!");
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
		ContentResolver resolver = getContentResolver();

		long now = System.currentTimeMillis();
		long updateMilis = 2 * 60 * 60 * 1000;

		while (hasMoreWidgetIDs()) {
			Log.d(TAG, "run across very widiget Ids");
			int widgetId = nextWidgetIDs();
			Uri uri = ContentUris.withAppendedId(WeatherWidgets.CONTENT_URI,
					widgetId);

			Cursor cursor = null;
			boolean isConfigured = false;
			boolean shouldUpdate = false;

			try {
				cursor = resolver
						.query(uri, widgetProjection, null, null, null);

				if (cursor != null && cursor.moveToFirst()) {
					Log.d(TAG, "query the widgets infomation");
					isConfigured = cursor.getInt(0) == 1;
					long lastUpdateTime = cursor.getLong(1);
					updateMilis = cursor.getLong(2) * 60 * 60 * 1000;

					shouldUpdate = Math.abs(now - updateMilis) > lastUpdateTime;
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}

			if (!isConfigured) {
				Log.d(TAG, "no configured , next Widget");
				continue;
			} else if (shouldUpdate) {
				try {
					WebServiceHelper.updateForecasts(this, uri);
				} catch (ForecastParseException e) {
					Log.e(TAG, "Problem parsing forecast", e);
				}
			}
			
			Log.d(TAG, "update Views");
			RemoteViews updateViews = ForecastWidget.updateViews(this, uri);
			// Push this update to surface
            if (updateViews != null) {
                manager.updateAppWidget(widgetId, updateViews);
            }
		}
		
		Log.d(TAG, "set next time update");
		// 设置下一次的更新时间
		// 每隔30分钟执行一次
		now = System.currentTimeMillis();		
		long nextUpdate = now + 30 * 60 * 1000;

		Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
		updateIntent.setClass(this, ForecastService.class);

		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				updateIntent, 0);

		// Schedule alarm, and force the device awake for this update
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdate, pendingIntent);

		// No updates remaining, so stop service
		stopSelf();
	}

}
