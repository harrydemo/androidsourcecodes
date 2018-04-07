/**
 * 
 */
package com.tilltheendwjx.airplus.receiver;

import com.tilltheendwjx.airplus.AirPlusActivity;
import com.tilltheendwjx.airplus.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * @author wjx
 * 
 */
public class AirAppWidgetReceiver extends BroadcastReceiver {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.air_app_widget);
			views.setOnClickPendingIntent(R.id.air_appwidget, PendingIntent
					.getActivity(context, 0, new Intent(context,
							AirPlusActivity.class), 0));
			int[] appWidgetIds = intent
					.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			AppWidgetManager gm = AppWidgetManager.getInstance(context);
			gm.updateAppWidget(appWidgetIds, views);
		}
	}

}
