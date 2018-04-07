/**
 * 
 */
package com.tilltheendwjx.airplus;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * @author wjx
 * 
 */
public class AirModeController {
	public static void setAirplaneMode(Context context, boolean enabling) {
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, enabling ? 1 : 0);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", enabling);
		context.sendBroadcast(intent);
	}

	public static boolean IsAirModeOn(Context context) {
		return (Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);
	}
}
