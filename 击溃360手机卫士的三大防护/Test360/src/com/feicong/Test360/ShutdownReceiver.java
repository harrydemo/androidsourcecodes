package com.feicong.Test360;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("Test360", "系统关闭中...");
        //RootUtils.EnableApp("com.feicong.Test360/Test360Activity", true); 
        //RootUtils.EnableApp("com.qihoo360.mobilesafe", false);
    }
}
