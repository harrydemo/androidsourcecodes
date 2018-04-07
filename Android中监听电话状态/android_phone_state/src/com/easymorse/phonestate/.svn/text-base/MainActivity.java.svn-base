package com.easymorse.phonestate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TelephonyManager manager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

		manager.listen(new MyPhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

				Toast.makeText(MainActivity.this, "结束", Toast.LENGTH_LONG)
						.show();
				break;
			case TelephonyManager.CALL_STATE_RINGING:

				Toast.makeText(MainActivity.this,
						"手机铃声响了，来电号码:" + incomingNumber, Toast.LENGTH_LONG)
						.show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				Toast.makeText(MainActivity.this, "电话被挂起了", Toast.LENGTH_LONG)
						.show();

			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

}