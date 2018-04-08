package com.worldchip.apk;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class USBDisconnectReceiver extends BroadcastReceiver{

	
	private static final String SAVEIMAGE = "com.worldchip.apk.SavingImage";
	private final String TAG="USBDisconnectReceiver";
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "the usb disconnected!") ;
		Intent serviceIntent = new Intent();
		serviceIntent.setAction(SAVEIMAGE);
		context.startService(serviceIntent);
	}
	
}
