package com.lqf.gerenriji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GeRenRiJiActivity extends Activity {
	// 声明一个Handler线程
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 新建一个线程，过3秒钟后向handler发送一个消息
		Runnable runnable = new Runnable() {

			public void run() {
				//try catch 异常处理
				try {
					//跳转时间毫秒
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//给Handler发信息
				handler.sendEmptyMessage(0);
			}
		};
		//实列化一个线程
		Thread thread = new Thread(runnable);
		//开始执行
		thread.start();
		
		// 初始化实例对象
		handler = new Handler() {
			//重写构造函数
			public void handleMessage(Message message) {
				//信使跳转
				Intent intent = new Intent(GeRenRiJiActivity.this,
						ZhuJieMian.class);
				//开始执行
				startActivityForResult(intent, 0);
				//加入动画
				overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);	
				//退出
				finish();
			}
		};

	}

}