package com.jacinter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// ��������������ֻ������󣬽��յ��ֻ���������Ϣ��Ȼ�������绰���������
		Intent service = new Intent(context, FloatingWindowService.class);
		context.startService(service);
		Log.e("PhoneService","�����Ѿ��ɹ�����");
	}

}
