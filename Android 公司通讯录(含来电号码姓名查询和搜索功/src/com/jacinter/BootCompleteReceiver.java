package com.jacinter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 这个类是用来在手机启动后，接收到手机启动的信息，然后启动电话监听服务的
		Intent service = new Intent(context, FloatingWindowService.class);
		context.startService(service);
		Log.e("PhoneService","服务已经成功启动");
	}

}
