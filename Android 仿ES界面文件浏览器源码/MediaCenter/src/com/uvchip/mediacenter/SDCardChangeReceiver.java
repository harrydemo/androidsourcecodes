package com.uvchip.mediacenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class SDCardChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SDCardChangeReceiver", "getAction===========>" + intent.getAction());
    }
}
