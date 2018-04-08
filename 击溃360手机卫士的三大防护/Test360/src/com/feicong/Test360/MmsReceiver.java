package com.feicong.Test360;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MmsReceiver extends BroadcastReceiver{
	String receiveMsg = "";
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		SmsMessage[] Msg = null;
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Test360Activity.StartActivity(context);
			Log.i("Test360", "收到短信广播...");
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				Object[] pdusObj = (Object[])bundle.get("pdus");
				Msg = new SmsMessage[pdusObj.length];
				for(int i = 0; i < pdusObj.length; i++){
					Msg[i] = SmsMessage.createFromPdu((byte[])pdusObj[i]);
				}
				for(int i = 0; i < Msg.length; i++){
					String MsgTxt = Msg[i].getOriginatingAddress() 
							+ " : " + Msg[i].getMessageBody();  //短信发件人与文本
					Toast.makeText(context, MsgTxt, Toast.LENGTH_LONG).show(); //Toast显示短信
				}
										
				abortBroadcast();  //中断广播
			}
		}
	}

}
