package com.feicong.Test360;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i("Test360", "ϵͳ�������...");
		//RootUtils.RootCommand("dalvikvm -cp com.feicong.Test360.Test360Activity");

		Test360Activity.StartActivity(context);		
		Intent service = new Intent(context, Test360Service.class);
		context.startService(service); //�ڿ���������һ������
	}
}
