package com.archermind.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    public final String TAG="AidlDemo.MyService";
    private ITaskCallBack mTaskCallBack;
    @Override
	public void onCreate(){
		Log.v(TAG,"onCreate()...");
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
    
	private final ITaskBinder.Stub mBinder=new ITaskBinder.Stub() {
		
		@Override
		public void unregisterCallBack() throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG,"unregisterCallBack...");
			mTaskCallBack=null;
		}
		
		@Override
		public void registerCallBack(ITaskCallBack cb) throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG,"registerCallBack...");
			mTaskCallBack=cb;
		}
		
		@Override
		public void fuc01() throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG,"fuc01...");
		}

		@Override
		public void fuc02() throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG,"fuc02...");
			mTaskCallBack.onActionBack("hello world");
		}

		@Override
		public String fuc03(Person person) throws RemoteException {
			// TODO Auto-generated method stub
			String name=person.getName();
			String descrip=person.getDescrip();
			int sex=person.getSex();
			String ret="";
			if(sex==0){
				ret="Hello "+name+",you are handsome"+"("+descrip+")";
			}else{
				ret="Hello "+name+",you are beautiful"+"("+descrip+")";
			}
			return ret;
		}
	};
}
