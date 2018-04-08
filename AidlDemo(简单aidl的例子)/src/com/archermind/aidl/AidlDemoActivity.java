package com.archermind.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AidlDemoActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private ITaskBinder mService;
	private final String TAG="AidlDemo.AidlDemoActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button fuc01=(Button)findViewById(R.id.fuc01Button);
        fuc01.setOnClickListener(this);
        Button fuc02=(Button)findViewById(R.id.fuc02Button);
        fuc02.setOnClickListener(this);
        Button fuc03=(Button)findViewById(R.id.fuc03Button);
        fuc03.setOnClickListener(this);
        Button bindBtn=(Button)findViewById(R.id.bindButton);
        bindBtn.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		  case R.id.fuc01Button:
			  try {
				mService.fuc01();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  break;
		  case R.id.fuc02Button:
			  try {
				mService.fuc02();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  break;
		  case R.id.fuc03Button:
			  try {
				Person person=new Person();
				person.setSex(0);
				person.setName("MeiYi");
				person.setDescrip("CEO");
				String ret=mService.fuc03(person);
				Log.v(TAG,"ret="+ret);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  break;
		  case R.id.bindButton:
		      Intent intent=new Intent("com.archermind.aidl.myservice");
			  bindService(intent,mConnection,BIND_AUTO_CREATE);
			  break;
		  default:
			  break;
		}
	}
	
	private ServiceConnection mConnection=new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService=ITaskBinder.Stub.asInterface(service);
			try {
				mService.registerCallBack(mCallBack);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v(TAG,"onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			try {
				mService.unregisterCallBack();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mService=null;
			Log.v(TAG,"onServiceDisconnected");
		}
		
	};
	
	private final ITaskCallBack.Stub mCallBack=new ITaskCallBack.Stub() {

		@Override
		public void onActionBack(String str) throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG,"onActionBack str="+str);
		}
		
	};
}