package com.amaker.ch07.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.amaker.ch07.app.IPerson.Stub;

/**
 * @author 郭宏志
 * 使用Service将接口暴露给客户端
 */
public class MyRemoteService extends Service{
	// 声明IPerson接口
	private Binder iPerson = new IPersonImpl();
	@Override
	public IBinder onBind(Intent intent) {
		return iPerson;
	}
}
