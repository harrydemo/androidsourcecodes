package com.amaker.ch07.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.amaker.ch07.app.IPerson.Stub;

/**
 * @author ����־
 * ʹ��Service���ӿڱ�¶���ͻ���
 */
public class MyRemoteService extends Service{
	// ����IPerson�ӿ�
	private Binder iPerson = new IPersonImpl();
	@Override
	public IBinder onBind(Intent intent) {
		return iPerson;
	}
}
