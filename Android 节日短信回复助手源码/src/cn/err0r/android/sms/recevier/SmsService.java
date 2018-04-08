package cn.err0r.android.sms.recevier;

import cn.err0r.android.sms.R;
import cn.err0r.android.sms.ReceiverType;
import cn.err0r.android.sms.view.Main;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SmsService extends Service {
	private static final String TAG = "LocalService"; 
	private NotificationManager catchMsgNotification;
    private SmsReceiver recevier;
    private boolean isregiset = false;
    private static final String MSG_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
        Log.i(TAG, "onBind");
        return null;
	}


    @Override
    public void onCreate() {
            Log.i(TAG, "onCreate");
            super.onCreate();
    		  
    }
    
    @Override
    public void onDestroy() {
            Log.i(TAG, "onDestroy");
            if (recevier != null && isregiset) {
                unregisterReceiver(recevier);
                isregiset = false;
                Toast.makeText(this, getResources().getString(R.string.unbind_service), 0).show();
            }
            super.onDestroy();
            displayNotificationMessage(getResources().getString(R.string.unbind_service));  
    }

    @Override
    public void onStart(Intent intent, int startId) {
            Log.i(TAG, "onStart");
            super.onStart(intent, startId);
            try{
            	recevier = new SmsReceiver((ReceiverType)intent.getSerializableExtra("TYPE"));
            }catch (Exception e) {
				// TODO: handle exception
            	recevier = new SmsReceiver(ReceiverType.Standard);
			}
    	    IntentFilter intentFilter = new IntentFilter(MSG_RECEIVED);
    	    intentFilter.setPriority(1000);//设置优先级最大
    	    registerReceiver(recevier , intentFilter);
    	    isregiset=true;
            Toast.makeText(this, getResources().getString(R.string.start_succeed), 0).show();
            catchMsgNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
            displayNotificationMessage(getResources().getString(R.string.bind_service));
    } 
	
    private void displayNotificationMessage(String message) {  
        Notification notification = new Notification(android.R.drawable.sym_action_chat, message,  
                System.currentTimeMillis());  
        
        if(message.equals(getResources().getString(R.string.bind_service)))
        	notification.flags = Notification.FLAG_NO_CLEAR;
        	else{
        		notification.flags = Notification.FLAG_AUTO_CANCEL;
        	}
        
        Intent i = new Intent(this, Main.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        //PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(
        		this, 
        		0,  
                new Intent(this, Main.class), 
                PendingIntent.FLAG_UPDATE_CURRENT);  
        notification.setLatestEventInfo(
        		this, 
        		getResources().getString(R.string.app_name), 
        		message,  
                contentIntent);  
        
        catchMsgNotification.notify(R.string.app_name, notification);  
        if(message.equals(getResources().getString(R.string.unbind_service)))
        	catchMsgNotification.cancel(R.string.app_name);
    }  
    
    public void regiset(View v) {
        IntentFilter filter = new IntentFilter(MSG_RECEIVED);
        filter.setPriority(1000);//设置优先级最大
        registerReceiver(recevier, filter);
        isregiset = true;
        Toast.makeText(this, getResources().getString(R.string.start_succeed), 0).show();
    }

    public void unregiset(View v) {
        if (recevier != null && isregiset) {
            unregisterReceiver(recevier);
            isregiset = false;
            Toast.makeText(this, getResources().getString(R.string.stop_succeed), 0).show();
        } else
            Toast.makeText(this, getResources().getString(R.string.stop_failed), 0).show();
    }
}