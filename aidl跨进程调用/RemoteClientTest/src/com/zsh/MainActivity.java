package com.zsh;

import com.amaker.ch07.app.IPerson;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * IPerson.aidl
 * @author ����־
 * RPC ����
 */
public class MainActivity extends Activity {
	// ����IPerson�ӿ�
	private IPerson iPerson;
	// ���� Button
	private Button btn;
	// ʵ����ServiceConnection
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		synchronized public void onServiceConnected(ComponentName name, IBinder service) {
			// ���IPerson�ӿ�
			iPerson = IPerson.Stub.asInterface(service);
			System.out.println("iperson----------:"+iPerson);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			iPerson=null;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���õ�ǰ��ͼ����
		setContentView(R.layout.main);
		// ʵ����Button
		btn = (Button) findViewById(R.id.Button01);
		//ΪButton��ӵ����¼�������
		
		// ʵ����Intent
		Intent intent = new Intent("com.amaker.ch09.app.action.MY_REMOTE_SERVICE");
		// ����Intent Action ����
		bindService(intent, conn, Service.BIND_AUTO_CREATE);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				try{
					String msg = iPerson.display();
					// ��ʾ�������÷���ֵ
					Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		unbindService(conn);
		super.onDestroy();
	}
}