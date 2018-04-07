package com.easymorse.autosendsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	PendingIntent paIntent;

	SmsManager smsManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		paIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
		smsManager = SmsManager.getDefault();

		findViewById(R.id.sendButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				smsManager.sendTextMessage("15210133976", null, "这条短信是自动发送的", paIntent,
						null);
			}
		});

	}
}