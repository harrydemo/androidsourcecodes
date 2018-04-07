/**
 * TorProxy - Anonymous data communication for Android devices
 * Copyright (C) 2009 Connell Gauld
 * 
 * Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */


package uk.ac.cam.cl.dtg.android.tor.TorProxy;

import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.ITorProxyControl;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.TorProxyLib;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TorProxySettings extends Activity implements
		OnCheckedChangeListener {

	// Connection to Tor control service
	private ITorProxyControl mTorProxyControl = null;
	private final IntentFilter mTorProfileFilter = new IntentFilter(
			TorProxyLib.PROFILE_CHANGE_INTENT);

	// UI elements
	private RadioGroup mProfileRadioGroup = null;

	// Update thread things
	private boolean updateDisplay = true;
	private Updater updater = null;

	// Standard handler for updating UI in correct thread
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	/**
	 * Updates the UI every second
	 * 
	 * @author cmg47
	 * 
	 */
	private class Updater extends Thread {

		@Override
		public void run() {
			try {
				while (updateDisplay) {
					Thread.sleep(1000); // 1 second
					if (handler != null)
						handler.sendMessage(handler.obtainMessage());
				}
			} catch (InterruptedException e) {
				// Thread sleep interrupted
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Deal with the UI
		setContentView(R.layout.main);

		mProfileRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mProfileRadioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Start up the Tor control service
		// Use startService rather than bindService since we want
		// the service to continue even after the user leaves
		// this activity.
		startService(new Intent(this, TorProxyControlService.class));
	}

	@Override
	protected void onPause() {

		// Stop the UI update thread
		updateDisplay = false;
		updater = null;

		unregisterReceiver(mTorUpdates);
		unbindService(svcConn);
		
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Bind to the Tor control service
		bindService(new Intent(this, TorProxyControlService.class), svcConn, 0);
		registerReceiver(mTorUpdates, mTorProfileFilter);

		// Start the UI update thread
		updateDisplay = true;
		if (updater == null) {
			updater = new Updater();
			updater.setName("UIUpdater");
			updater.start();
		}
	}

	private ServiceConnection svcConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mTorProxyControl = ITorProxyControl.Stub.asInterface(service);
			updateProfile();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mTorProxyControl = null;
			updateProfile();
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem arg0) {
		switch (arg0.getItemId()) {
		case R.id.menuAbout:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		case R.id.menuSettings:
			startActivity(new Intent(this, EditPreferences.class));
			return true;
		/*case R.id.menuTesting:
			startActivity(new Intent(this, UnitTests.class));
			return true;*/
		}
		return false;
	}


	/**
	 * Set the profile 
	 * @param profile
	 */
	private void setProfile(final int profile) {
		new Thread() {
			@Override
			public void run() {
				try {
					if (mTorProxyControl != null)
						mTorProxyControl.setProfile(profile);
				} catch (RemoteException e) {
					// Oh well
				}
			}
		}.start();
	}

	/**
	 * Get an update of the current profile from the service and update the UI as appropriate
	 */
	private void updateProfile() {
		if (mTorProxyControl != null) {
			int i;
			try {
				i = mTorProxyControl.getProfile();
				switch (i) {
				case TorProxyLib.PROFILE_OFF:
					mProfileRadioGroup.check(R.id.radioOff);
					break;
				case TorProxyLib.PROFILE_ONDEMAND:
					mProfileRadioGroup.check(R.id.radioOnDemand);
					break;
				case TorProxyLib.PROFILE_ON:
					mProfileRadioGroup.check(R.id.radioAlwaysOn);
					break;
				}
			} catch (RemoteException e) {
				// Oh well
			}
		}
	}

	// Broadcast receiver for receiving updates from the control service
	BroadcastReceiver mTorUpdates = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateProfile();
		}

	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		int profile = 0;
		
		switch (checkedId) {
		case R.id.radioOff:
			profile = TorProxyLib.PROFILE_OFF;
			break;
		case R.id.radioOnDemand:
			profile = TorProxyLib.PROFILE_ONDEMAND;
			break;
		case R.id.radioAlwaysOn:
			profile = TorProxyLib.PROFILE_ON;
			break;
		}
		if (mTorProxyControl != null) {
			setProfile(profile);
		}
	}



}