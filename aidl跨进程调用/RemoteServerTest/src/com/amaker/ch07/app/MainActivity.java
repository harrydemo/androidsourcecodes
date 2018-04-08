package com.amaker.ch07.app;

import com.amaker.ch07.app.IPerson;
import com.amaker.ch07.app.R;
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
 * 
 * @author ����־
 * RPC ����
 */
public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent();
		// ����Intent Action ����
		intent.setAction("com.amaker.ch09.app.action.MY_REMOTE_SERVICE");
		// �󶨷���
		startService(intent);
	}
	
}