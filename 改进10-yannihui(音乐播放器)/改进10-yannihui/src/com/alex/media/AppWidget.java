package com.alex.media;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {
	private static final String PLAY_ACTION = "com.alex.playmusic";
	private static final String NEXT_ACTION = "com.alex.nextone";
	private static final String lAST_ACTION = "com.alex.lastone";
	private static final String START_APP = "com.alex.startapp";
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i("info", "onDeleted...");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		Log.i("info", "onDisabled...");
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		Log.i("info", "onEnabled...");
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidgetlayout);
		if (intent.getAction().equals("com.alex.pause")){
			views.setImageViewResource(R.id.playButton, R.drawable.play_selecor);
		} else if (intent.getAction().equals("com.alex.play")){
			views.setImageViewResource(R.id.playButton, R.drawable.pause_selecor);
		} else if (intent.getAction().equals("com.alex.musictitle")){
			String musicName = intent.getExtras().getString("title");
			if (musicName.length()>6){
				musicName = musicName.substring(0, 5)+"...";
			}
			views.setTextViewText(R.id.title, musicName);
		}
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context); 
        ComponentName componentName = new ComponentName(context,AppWidget.class); 
        appWidgetManager.updateAppWidget(componentName, views);
		Log.i("info", "onReceive...");
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i("info", "onUpdate...");
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidgetlayout);
		/*设置播放键的动作*/
		views.setImageViewResource(R.id.playButton, R.drawable.play_selecor);
		Intent playIntent = new Intent(PLAY_ACTION);
		PendingIntent playPending = PendingIntent.getBroadcast(context, 0, playIntent, 0);
		views.setOnClickPendingIntent(R.id.playButton, playPending);
		/*设置上一首按钮的动作*/
		Intent lastIntent = new Intent(lAST_ACTION);
		PendingIntent lastPending = PendingIntent.getBroadcast(context, 0, lastIntent, 0);
		views.setOnClickPendingIntent(R.id.lastButton, lastPending);
		/*设置下一首按钮的动作*/
		Intent nextIntent = new Intent(NEXT_ACTION);
		PendingIntent nextPending = PendingIntent.getBroadcast(context, 0, nextIntent, 0);
		views.setOnClickPendingIntent(R.id.nextButton, nextPending);
		
		/*获取正在播放的音乐名*/
		Intent intent = new Intent();
		intent.setAction(START_APP);
		context.sendBroadcast(intent);
		
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}

}
