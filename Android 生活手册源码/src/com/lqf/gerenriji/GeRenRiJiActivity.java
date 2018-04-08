package com.lqf.gerenriji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GeRenRiJiActivity extends Activity {
	// ����һ��Handler�߳�
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// �½�һ���̣߳���3���Ӻ���handler����һ����Ϣ
		Runnable runnable = new Runnable() {

			public void run() {
				//try catch �쳣����
				try {
					//��תʱ�����
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//��Handler����Ϣ
				handler.sendEmptyMessage(0);
			}
		};
		//ʵ�л�һ���߳�
		Thread thread = new Thread(runnable);
		//��ʼִ��
		thread.start();
		
		// ��ʼ��ʵ������
		handler = new Handler() {
			//��д���캯��
			public void handleMessage(Message message) {
				//��ʹ��ת
				Intent intent = new Intent(GeRenRiJiActivity.this,
						ZhuJieMian.class);
				//��ʼִ��
				startActivityForResult(intent, 0);
				//���붯��
				overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);	
				//�˳�
				finish();
			}
		};

	}

}