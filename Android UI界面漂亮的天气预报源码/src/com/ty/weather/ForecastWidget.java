/**
 * 
 */
package com.ty.weather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import com.ty.weather.util.ForecastUtil;
import com.ty.weather.util.WidgetEntity;

/**
 * @author TANGYONG
 * @version 1.0 2010-05-21 桌面小控件入口类
 */
public class ForecastWidget extends AppWidgetProvider {
	private static final String TAG = "ForecastWidget";
	private static Time TIME_WIDGET = new Time();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		// 获取需要更新的桌面小控件
		ForecastService.addWidgetIDs(appWidgetIds);

		// 启动获取天气预报信息的服务
		context.startService(new Intent(context, ForecastService.class));

		// 启动时间信息的服务
		context.startService(new Intent(context, ForecastTimeService.class));
	}

	public static RemoteViews updateViews(Context context, Uri uri) {
		Log.d(TAG, "Building medium widget update");

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.weather);
		WidgetEntity widgetEntity = WebServiceHelper.getWidgetEntity(context, uri, true);

		if (widgetEntity != null) {
			Log.d(TAG, "update View from widgetEntity");
			views.setImageViewResource(R.id.forecastImage, ForecastUtil
					.getForecastImage(widgetEntity.getIcon(), ForecastUtil.isDaytime()));
			views.setTextViewText(R.id.cityText, widgetEntity.getCity());
			views.setTextViewText(R.id.conditionText, widgetEntity
					.getCondition());
			views.setTextViewText(R.id.tempCText, widgetEntity.getTempC()
					.toString()
					+ "°");
			views.setTextViewText(R.id.hightText, "H:"
					+ widgetEntity.getDetails().get(0).getHight().toString()
					+ "°");
			views.setTextViewText(R.id.lowText, "L:"
					+ widgetEntity.getDetails().get(0).getLow().toString()
					+ "°");
		}

		 // Connect click intent to launch details dialog
        Intent detailIntent = new Intent(context, DetailForecastActivity.class);
        detailIntent.setData(uri);

        PendingIntent pending = PendingIntent.getActivity(context, 0, detailIntent, 0);

        views.setOnClickPendingIntent(R.id.absoluteLayout, pending);
		
		return views;
	}

	public static RemoteViews updateTime(Context context) {
		// TODO Auto-generated method stub
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.weather);
		
		TIME_WIDGET.setToNow();
		int hour01 = TIME_WIDGET.hour / 10;
		int hour02 = TIME_WIDGET.hour % 10;
		int minute01 = TIME_WIDGET.minute / 10;
		int minute02 = TIME_WIDGET.minute % 10;
		int dayOfWeek = TIME_WIDGET.weekDay;
		CharSequence dt = DateFormat.format(context.getString(R.string.dateFormat), TIME_WIDGET.toMillis(false));
		
		views.setTextViewText(R.id.dateText, dt);		
		views.setTextViewText(R.id.dayText, ForecastUtil.getDayofWeek(context, dayOfWeek));
		views.setImageViewResource(R.id.hour01Image, ForecastUtil.getImageNumber(hour01));
		views.setImageViewResource(R.id.hour02Image, ForecastUtil.getImageNumber(hour02));
		views.setImageViewResource(R.id.minute01Image, ForecastUtil.getImageNumber(minute01));
		views.setImageViewResource(R.id.minute02Image, ForecastUtil.getImageNumber(minute02));
		
		return views;
	}

	
}
