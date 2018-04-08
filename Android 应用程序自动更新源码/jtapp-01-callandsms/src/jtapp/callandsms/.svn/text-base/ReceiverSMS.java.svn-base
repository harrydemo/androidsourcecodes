package jtapp.callandsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceiverSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				"android.provider.Telephony.SMS_RECEIVED")) {
			StringBuilder sb = new StringBuilder();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] msgs = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					msgs[i] = SmsMessage
						.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage s : msgs) {
					sb.append("收到来自");
					sb.append(s.getDisplayOriginatingAddress());
					sb.append("的SMS, 内容:");
					sb.append(s.getDisplayMessageBody());
				}
				Toast.makeText(
						context, 
						"收到了短消息: " + sb.toString(),
						Toast.LENGTH_LONG).show();
			}
		
		}
	}
}
