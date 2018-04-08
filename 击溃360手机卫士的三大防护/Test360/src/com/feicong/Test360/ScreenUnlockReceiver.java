package com.feicong.Test360;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenUnlockReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		//Test360Activity.StartActivity(context);
		RootUtils.EnableApp("com.feicong.Test360/Test360Activity", true); //启用主Activity,开启开机自动运行
	}

}
