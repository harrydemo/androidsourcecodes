package ws.denger.gpsdemo;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * @author Denger
 * 
 */
public class GPSHelper {
	private final static String TAG = "GPSHelper";

	/**
	 * 获取SDK版本号
	 */
	private static int getSDKVersion() {
		int sdk_int = Build.VERSION.SDK_INT;
		Log.d(TAG, "SDK版本：" + sdk_int);
		return sdk_int;
	}

	public static void toggleGPS(Context con) {
//		if (getSDKVersion() < 8) 
			toggleGPSBelowSDK8(con);
//		 else
//		toggleGPSAfterSDK8(con);	
	}
	
	public static void toggleGPSAfterSDK8(Context con){
//		Settings.Secure.setLocationProviderEnabled(con.getContentResolver(), provider, enabled)
	}

	/**
	 * Level8(SDK2.2)之前使用的方法
	 * <p>
	 * 开关GPS。 如果此时是关闭的则打开，否则相反
	 * 
	 * @param con
	 */
	public static void toggleGPSBelowSDK8(Context con) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(con, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看GPS状态
	 * 
	 * @param con
	 * @return true为打开,false为关闭
	 */
	public static boolean isGPSEnable(Context con) {
		/*
		 * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
		 * Settings.System.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 */
		String str = Settings.Secure.getString(con.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Log.d("GPS", "FUCK!!!!!!!!!!!!" + str);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}

}
