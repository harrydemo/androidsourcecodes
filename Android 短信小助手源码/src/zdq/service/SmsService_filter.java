package zdq.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class SmsService_filter extends Service {
	private static final String MSG_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "Service_filter";
	private SmsFilterReceiver recevier;
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "onBind_filter");
		return null;
	}
	
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "onDestroy_filter");
		super.onDestroy();
		unregisterReceiver(recevier);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "onStartCommand_filter");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onStart(Intent intent,int startId){
		Log.i(TAG, "onStart_filter");
		super.onStart(intent, startId);
		
		recevier=new SmsFilterReceiver();
		IntentFilter filter = new IntentFilter(MSG_RECEIVED);
        filter.setPriority(1000);//设置优先级最大
		registerReceiver(recevier, filter);
	}
	
	public void onCreate(){
		Log.i(TAG, "onCreate_filter");
		super.onCreate();
	}
	
    public void regiset(View v) {
        IntentFilter filter = new IntentFilter(MSG_RECEIVED);
        filter.setPriority(1000);//设置优先级最大
        registerReceiver(recevier, filter);
    }
    
    public void unregiset(View v) {
        unregisterReceiver(recevier);
    }	
}