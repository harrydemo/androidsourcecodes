/*******************************************************************************
 * Copyright (c) 2009 Ferenc Hechler - ferenc_hechler@users.sourceforge.net
 * 
 * This file is part of the Android Battery Dog
 *
 * The Android Battery Dog is free software;
 * you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version.
 * 
 * The Android Battery Dog is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Android Battery Dog;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 *******************************************************************************/
package net.sf.andbatdog.batterydog;

import java.io.File;
import java.io.FileWriter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class BatteryDog_Service extends Service {

	private final static String TAG = "BATDOG.service";
	
	public static final String LOGFILEPATH = "BatteryDog/battery.csv";
	
	private final static String[] batteryExtraKeys = {"level", "scale", "voltage", "temperature", "plugged", "status", "health", "present", "technology", "icon-small"};

	private File mBatteryLogFile;
	private int mCount;
	private Intent mLastBatteryIntent;
    private boolean mQuitThread;
    private boolean mThreadRunning;


    @Override
    public void onCreate() {
    	super.onCreate();
		if (!mThreadRunning) {
			mCount = 0;
			mLastBatteryIntent = null;
			mQuitThread = false;
	        Thread thr = new Thread(null, mTask, "BatteryDog_Service");
	        thr.start();
			registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	        Toast.makeText(this, "BatteryDog Service started", Toast.LENGTH_SHORT).show();
		}
    }
	
	@Override
	public void onDestroy() {
    	Log.i(TAG, "onDestroy");
        mQuitThread = true;
        notifyService();
        
    	super.onDestroy();
    	unregisterReceiver(mBatInfoReceiver);
        Toast.makeText(this, "BatteryDog Service stopped", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			try {
            	mCount += 1;
				mLastBatteryIntent = (Intent) intent.clone();
				notifyService();
			}
			catch (Exception e) {
				Log.e(TAG,e.getMessage(), e);
			}
		}

	};

	private void logBattery(Intent batteryChangeIntent) {
		if (batteryChangeIntent == null)
			return;
		try {
			FileWriter out = null;
			if (mBatteryLogFile != null) {
				try {
					out = new FileWriter(mBatteryLogFile, true);
				}
				catch (Exception e) {}
			}
			if (out == null) {
				File root = Environment.getExternalStorageDirectory();
				if (root == null)
					throw new Exception("external storage dir not found");
				mBatteryLogFile = new File(root,BatteryDog_Service.LOGFILEPATH);
				boolean fileExists = mBatteryLogFile.exists();
				if (!fileExists) {
					mBatteryLogFile.getParentFile().mkdirs();
					mBatteryLogFile.createNewFile();
				}
				if (!mBatteryLogFile.exists()) 
					throw new Exception("creation of file '"+mBatteryLogFile.toString()+"' failed");
				if (!mBatteryLogFile.canWrite()) 
					throw new Exception("file '"+mBatteryLogFile.toString()+"' is not writable");
				out = new FileWriter(mBatteryLogFile, true);
				if (!fileExists) {
					String header = createHeadLine();
					out.write(header);
					out.write("\n");
				}
			}
			if (mLastBatteryIntent != null) {
				String extras = createBatteryInfoLine(mLastBatteryIntent);
				out.write(extras);
				out.write("\n");
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.e(TAG,e.getMessage(),e);
		}
	}

	
    private String createHeadLine() {
    	StringBuffer result = new StringBuffer();
    	result.append("Nr;TimeMillis");
    	for (String key : batteryExtraKeys)
			result.append(";").append(key);
		return result.toString();
	}

	private String createBatteryInfoLine(Intent batteryIntent) {
    	StringBuffer result = new StringBuffer();
    	result.append(Integer.toString(mCount)).append(";").append(Long.toString(System.currentTimeMillis()));
    	Bundle extras = batteryIntent.getExtras();
    	for (String key : batteryExtraKeys)
			result.append(";").append(extras.get(key));
		return result.toString();
	}


	/**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {

		public void run() {
            mThreadRunning = true;
            Log.i(TAG,"STARTING BATTERYDOG TASK");
            while (!mQuitThread) {
				logBattery(mLastBatteryIntent);
                synchronized (BatteryDog_Service.this) {
                	try {
                    	BatteryDog_Service.this.wait();
                	} catch (Exception ignore) {}
                }
            }
            mThreadRunning = false;
			logBattery(mLastBatteryIntent);
            Log.i(TAG,"LEAVING BATTERYDOG TASK");
        }

    };
	

	public void notifyService() {
		synchronized (BatteryDog_Service.this) {
			BatteryDog_Service.this.notifyAll();
		}
	}
}

