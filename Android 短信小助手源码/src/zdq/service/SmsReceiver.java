package zdq.service;

import zdq.msg.SMSToast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver{
	private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "LocalService"; 
	private String addr;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	/*
	* 判断是否是SMS_RECEIVED事件被触发
	*/
	if (intent.getAction().equals(strRes)) {
		
		Bundle bundle = intent.getExtras();
		
		if (bundle != null) {
			
			Object[] pdus = (Object[]) bundle.get("pdus");
			
			SmsMessage[] msg = new SmsMessage[pdus.length];
			
			for (int i = 0; i < pdus.length; i++) {
				msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			
			if(msg.length!=0){
				Log.i(TAG, "快速回复模式");
				for (SmsMessage currMsg : msg) {
					
					addr=currMsg.getDisplayOriginatingAddress();
					
					Intent mBootIntent = new Intent(context, SmsReceiver.class);
					mBootIntent = new Intent(context, SMSToast.class); 
			        mBootIntent.setAction("android.intent.action.MAIN"); 
			        mBootIntent.addCategory("android.intent.category.LAUNCHER");  
			        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			        
			        Bundle mBundle = new Bundle();  
			        mBundle.putSerializable("addr",addr);
			        mBootIntent.putExtras(mBundle); 	
			        
			        context.startActivity(mBootIntent); 
				} 
			}
		}
	}
	}
}