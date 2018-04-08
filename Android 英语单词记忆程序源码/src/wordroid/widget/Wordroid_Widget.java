package wordroid.widget;

import java.util.ArrayList;

import wordroid.activitys.EditWord;
import wordroid.database.DataAccess;
import wordroid.model.R;
import wordroid.model.Word;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class Wordroid_Widget extends AppWidgetProvider {
	final String next_action ="NEXT";
	final String last_action = "LAST";
	final String add_action = "ADD";
	static int num = 0;
	static ArrayList<Word> words ;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		Intent nextIntent= new Intent(context,Wordroid_Widget.class);
		nextIntent.setAction(next_action);
		PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, 0);
		views.setOnClickPendingIntent(R.id.widget_next, nextPendingIntent);
		
		Intent lastIntent= new Intent(context,Wordroid_Widget.class);
		lastIntent.setAction(last_action);
		PendingIntent lastPendingIntent = PendingIntent.getBroadcast(context, 0, lastIntent, 0);
		views.setOnClickPendingIntent(R.id.widget_last, lastPendingIntent);
		
		Intent addIntent= new Intent(context,EditWord.class);
		Bundle bundle = new Bundle();
		bundle.putString("action", "add");
		addIntent.putExtras(bundle);
		PendingIntent addPendingIntent = PendingIntent.getActivity(context, 0, addIntent, 0);
		views.setOnClickPendingIntent(R.id.widget_add, addPendingIntent);
		
		DataAccess data = new DataAccess(context);
		words = new ArrayList<Word>();
		words=data.QueryAttention(null, null);
		if (words.size()>0){
			views.setTextViewText(R.id.widget_text, words.get(num).getSpelling()+"\n"+words.get(num).getMeanning());
			
		}
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		DataAccess data = new DataAccess(context);
		words = new ArrayList<Word>();
		words=data.QueryAttention(null, null);
		if(intent.getAction().equals(next_action)){
			if (num==words.size()-1)num=0;
			else num++;
		}
		if (intent.getAction().equals(last_action)){
			if (num==0)num=words.size()-1;
			else num--;
		}
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		if (words.size()>0){
			views.setTextViewText(R.id.widget_text, words.get(num).getSpelling()+"\n"+words.get(num).getMeanning());
			
		}
		ComponentName thewidget = new ComponentName(context, Wordroid_Widget.class);
		AppWidgetManager manager=AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thewidget, views);
	}

}
