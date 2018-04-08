package com.msi.manning.weather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.msi.manning.weather.Constants;

public class WeatherAlertServiceReceiver extends BroadcastReceiver {

    private static final String CLASSTAG = WeatherAlertServiceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.v(Constants.LOGTAG, " " + WeatherAlertServiceReceiver.CLASSTAG
                + " received intent via BOOT, starting service");
            context.startService(new Intent(context, WeatherAlertService.class));
        }
    }
}
