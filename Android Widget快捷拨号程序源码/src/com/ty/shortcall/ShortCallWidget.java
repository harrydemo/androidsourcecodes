/**
 * 
 */
package com.ty.shortcall;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 快速拔号小程序
 * @author TANGYONG
 * @version 1.0 2010-07-6-08
 */
public class ShortCallWidget extends AppWidgetProvider {
	private static final String TAG = "ShortCallWidget";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(TAG, "login the widget");
		
		int count = appWidgetIds.length;		
		
		for(int i = 0; i < count; i++){
			int widgetId = appWidgetIds[i];
			
			updateAppWidget(context, appWidgetManager, widgetId);
		}
	}
	
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetId){
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		SharedPreferences sp = context.getSharedPreferences(ConfiguresActivity.PRE_NAME + widgetId, 0);
		String telUri = "tel:" + sp.getString("phoneNumber", "");
		String title = sp.getString("title", "");
		
		views.setTextViewText(R.id.widNameText, title);
		Uri uri = Uri.parse(telUri);
		
		// Connect click intent to launch details dialog
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);        
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widImageCall, pending);	
		
        //更新控件
		appWidgetManager.updateAppWidget(widgetId, views);
		
	}
}
