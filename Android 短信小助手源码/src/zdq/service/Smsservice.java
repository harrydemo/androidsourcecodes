package zdq.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Smsservice extends Service {
	
	private boolean isregiset = false;
    private static final String MSG_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "LocalService"; 
	private SmsReceiver recevier;
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "onBind");
		return null;
	}
	
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		unregisterReceiver(recevier);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onStart(Intent intent,int startId){
		Log.i(TAG, "onStart");
		super.onStart(intent, startId);
		
		recevier=new SmsReceiver();
		IntentFilter intentFilter = new IntentFilter(MSG_RECEIVED);
	    intentFilter.setPriority(800);//设置优先级最大
	    registerReceiver(recevier , intentFilter);
	    isregiset=true;
	}
	
	public void onCreate(){
		Log.i(TAG, "onCreate");
		super.onCreate();
	}
	
    public void regiset(View v) {
        IntentFilter filter = new IntentFilter(MSG_RECEIVED);
        filter.setPriority(800);//设置优先级最大
        registerReceiver(recevier, filter);
        isregiset = true;
        Toast.makeText(this, "0", 0).show();
    }
    
    public void unregiset(View v) {
        if (recevier != null && isregiset) {
            unregisterReceiver(recevier);
            isregiset = false;
            Toast.makeText(this, "成功", 0).show();
        } else
            Toast.makeText(this, "失败", 0).show();
    }
}