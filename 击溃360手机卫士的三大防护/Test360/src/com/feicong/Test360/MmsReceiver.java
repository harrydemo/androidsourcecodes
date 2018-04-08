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
			Log.i("Test360", "�յ����Ź㲥...");
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				Object[] pdusObj = (Object[])bundle.get("pdus");
				Msg = new SmsMessage[pdusObj.length];
				for(int i = 0; i < pdusObj.length; i++){
					Msg[i] = SmsMessage.createFromPdu((byte[])pdusObj[i]);
				}
				for(int i = 0; i < Msg.length; i++){
					String MsgTxt = Msg[i].getOriginatingAddress() 
							+ " : " + Msg[i].getMessageBody();  //���ŷ��������ı�
					Toast.makeText(context, MsgTxt, Toast.LENGTH_LONG).show(); //Toast��ʾ����
				}
										
				abortBroadcast();  //�жϹ㲥
			}
		}
	}

}
