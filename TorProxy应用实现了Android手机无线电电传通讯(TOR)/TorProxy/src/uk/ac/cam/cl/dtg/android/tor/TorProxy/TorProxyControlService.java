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

import java.io.IOException;

import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.HiddenServiceDescriptor;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.ITorProxyControl;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.TorProxyLib;
import TorJava.DefaultHiddenServiceRequestHandler;
import TorJava.HiddenServiceKeySet;
import TorJava.HiddenServiceProperties;
import TorJava.OnHiddenServiceStatusChange;
import TorJava.TorKeeper;
import TorJava.TorStatusChange;
import TorJava.Common.TorException;
import TorJava.Proxy.HTTPProxy;
import TorJava.Proxy.SocksProxy;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;

/**
 * Maintains the anonymous data connection and profiles
 * @author cmg47
 *
 */
public class TorProxyControlService extends Service implements TorStatusChange, OnHiddenServiceStatusChange {
	
	// Profile stuff
	private static final String PROFILE_PREFERENCE = "profile";
	private int mProfile = 0;
	private int mStatus = 0;
	
	// Notifications
	private TorNotifications mNotifications = null;
	
	// Network stuff
	private ConnectivityManager mConnectivityManager = null;
	private final IntentFilter mNetworkStatusFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	
	// Tor stuff
	private boolean mTorProcessActive = false;
	private HTTPProxy httpProxy = null;
	private SocksProxy socksProxy = null;
	private int mTimeRemaining = 0;
	
	private final ITorProxyControl.Stub binder = new ITorProxyControl.Stub() {

		/**
		 * Get the percentage completion of Tor startup
		 * @return an integer between 0 and 100 inclusive
		 */
		@Override
		public int getStartProgress() throws RemoteException {
			if (mTorProcessActive) return TorKeeper.getProgressPerCent();
			else return 0;
		}

		/**
		 * Returns the port number that the HTTP proxy is running on
		 */
		@Override
		public int getHTTPPort() throws RemoteException {
			return httpProxy.getPort();
		}

		/**
		 * Returns the port number that the SOCKS proxy is running on
		 */
		@Override
		public int getSOCKSPort() throws RemoteException {
			return socksProxy.getPort();
		}


		@Override
		public HiddenServiceDescriptor createHiddenService()
				throws RemoteException {
			HiddenServiceKeySet keys = new HiddenServiceKeySet();
			HiddenServiceDescriptor h = new HiddenServiceDescriptor(keys.getPublicKey(), keys.getPrivateKey(), keys.getUrl());
			return h; 
		}

		@Override
		public boolean startHiddenService(int port, HiddenServiceDescriptor h)
				throws RemoteException {
			return doStartHiddenService(port, h);
		}

		@Override
		public int getHiddenServiceStatusPercent(String url)
				throws RemoteException {
			
			if (!mTorProcessActive) return 0; // Hidden service can't be running
			
			try {
				return TorKeeper.getTor().getHiddenServiceProvisionStatusPercent(url);
			} catch (IllegalArgumentException e) {
				// Probably not started yet
				return 0;
			} catch (IOException e) {
				// Should never be here
				return 0;
			}
			
		}
		
		@Override
		public int getProfile() throws RemoteException {
			return mProfile;
		}
		
		@Override
		public void setProfile(int profile) throws RemoteException {
			TorProxyControlService.this.setProfile(profile);
		}

		@Override
		public int getStatus() throws RemoteException {
			return mStatus;
		}

		@Override
		public void registerDemand() throws RemoteException {
			if (!mTorProcessActive) doStartTor();
			updateProfile();
		}

		@Override
		public int getEstimatedTimeRemaining() throws RemoteException {
			return mTimeRemaining;
		}

	};
	
	private boolean doStartHiddenService(int port, HiddenServiceDescriptor h) {
		//Log.i("TorControlService", "Starting hidden service...");
		HiddenServiceKeySet keys = new HiddenServiceKeySet(h.getPublicKey(), h.getPrivateKey());
		HiddenServiceProperties p;
		try {
			p = new HiddenServiceProperties(port, new DefaultHiddenServiceRequestHandler(port), keys, this);
			TorKeeper.getTor().provideHiddenService(p);
			return true;
		} catch (TorException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	/**
	 * Returns true if the Tor system is running
	 * (note - Tor may not be ready yet though)
	 * @return true if Tor system is active
	 */
	public boolean isTorProcessActive() {
		return mTorProcessActive;
	}
	
	/**
	 * Start the HTTP proxy running
	 */
	private void startHttpProxy() {
		httpProxy = new HTTPProxy(this);
	}
	
	/**
	 * Stop the HTTP proxy running
	 */
	private void stopHttpProxy() {
		httpProxy.close();	
	}
	

	/**
	 * Start the SOCKS proxy
	 */
	private void startSocksProxy() {
		socksProxy = new SocksProxy(this);
	}
	
	/**
	 * Stop the SOCKS proxy
	 */
	private void stopSocksProxy() {
		socksProxy.close();
	}
	/**
	 * Receive notification of changes in Tor's status
	 */
	@Override
	public void statusChanged()
	{		
		updateProfile();
	}
	
	/**
	 * Check that the calling app has permission to change the profile
	 */
//	private void checkPermission() {
//		if (checkCallingPermission(TorProxyLib.CONTROL_PERMISSION) == PackageManager.PERMISSION_DENIED) {
//			throw new SecurityException("Tor control permission denied");
//		}		
//	}
//	
	/**
	 * Stop Tor
	 */
	private void doStopTor() {
		mTorProcessActive = false;
		mTimeRemaining = 0;
		stopHttpProxy();
		stopSocksProxy();
		TorKeeper.closeTor();
		
		// Tell other users of Tor that things have changed
		Intent i = new Intent(TorProxyLib.STATUS_CHANGE_INTENT);
		sendBroadcast(i);
	}
	
	/**
	 * Start Tor and begin the countdown timer
	 */
	private void doStartTor() {
		mTorProcessActive = true;
		// Do this in a new thread as it take a little while
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		new Thread() {
			@Override
			public void run() {
				try {
					TorKeeper.getTor(getFilesDir().getPath(), prefs.getBoolean("pref_faststartup", true)); // Allowed to touch the local filesystem
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startHttpProxy();
				startSocksProxy();
			}
		}.start();
		mTimeRemaining = TorKeeper.getStartupTimeEstimate(prefs.getBoolean("pref_faststartup", true));
		Thread mCountdownTimer = new Thread() {
			@Override
			public void run() {
				while(mTimeRemaining>0) {
					try {
						Thread.sleep(1000); // 1sec
					} catch (InterruptedException e) {
						// Yadda
					}
					mTimeRemaining--;
					mUpdateHandler.sendMessage(mUpdateHandler.obtainMessage());
				}
			}
		};
		mCountdownTimer.start();
	}

	/**
	 * When the status of a hidden service changes
	 */
	@Override
	public void onHiddenServiceStatusChange(String url, int newStatusPercent) {
		Intent i = new Intent(TorProxyLib.SERVICE_CHANGE_INTENT);
		i.putExtra("url", url);
		i.putExtra("status", newStatusPercent);
		sendBroadcast(i);
	}
	
	/**
	 * Makes sure that the service state is appropriate to the profile
	 */
	private void updateProfile() {
		switch (mProfile) {
		case TorProxyLib.PROFILE_OFF:
			// Deactivate Tor and remove status icon
			if (mTorProcessActive) doStopTor();
			mNotifications.hideNotification();
			setStatus(TorProxyLib.STATUS_UNAVAILABLE);
			break;
		case TorProxyLib.PROFILE_ONDEMAND:
			if (isNetworkAvailable()) {
				if (mTorProcessActive) {
					if (TorKeeper.isTorReady()) {
						mNotifications.setNotificationOn();
						setStatus(TorProxyLib.STATUS_ON);
					} else {
						mNotifications.setNotificationConnecting(mTimeRemaining);
						setStatus(TorProxyLib.STATUS_CONNECTING);
					}
				} else {
					mNotifications.hideNotification();
					setStatus(TorProxyLib.STATUS_REQUIRES_DEMAND);
				}
			} else {
				if (mTorProcessActive) doStopTor();
				mNotifications.hideNotification();
				setStatus(TorProxyLib.STATUS_UNAVAILABLE);
			}
			break;
		case TorProxyLib.PROFILE_ON:
			if (isNetworkAvailable()) {
				if (!mTorProcessActive) {
					doStartTor();
					mNotifications.setNotificationConnecting(mTimeRemaining);
					setStatus(TorProxyLib.STATUS_CONNECTING);
				} else {
					if (TorKeeper.isTorReady()) {
						mNotifications.setNotificationOn();
						setStatus(TorProxyLib.STATUS_ON);
					} else {
						mNotifications.setNotificationConnecting(mTimeRemaining);
						setStatus(TorProxyLib.STATUS_CONNECTING);
					}
				}
			} else {
				if (mTorProcessActive) doStopTor();
				mNotifications.setNotificationOff();
				setStatus(TorProxyLib.STATUS_UNAVAILABLE);
			}
			break;
		}
	}
	
	/**
	 * Set the current status of the anonymous connection.
	 * @param status
	 */
	private void setStatus(int status) {
		if (status != mStatus) {
			mStatus = status;
			
			// Send a change broadcast so that other users of
			// Tor know of the change
			Intent i = new Intent(TorProxyLib.STATUS_CHANGE_INTENT);
			i.putExtra("status", status);
			sendBroadcast(i);
		}
	}
	
	/**
	 * Return the status of the network connection
	 * @return true if a network connection is possible
	 */
	private boolean isNetworkAvailable() {
		NetworkInfo inf = mConnectivityManager.getActiveNetworkInfo();
		if (inf == null) return false;
		else return inf.isAvailable();
	}
	
	/**
	 * Broadcast receiver for getting changes to the network
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				updateProfile();
			}
		}
	};

	/**
	 * Service startup
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mNotifications = new TorNotifications(this);
		mConnectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.registerReceiver(mBroadcastReceiver, mNetworkStatusFilter);
		TorKeeper.setTorStatusChange(this);
		
		// Set the profile to what it was set at last
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setProfile(prefs.getInt(PROFILE_PREFERENCE, TorProxyLib.PROFILE_OFF));
	}
	
	/**
	 * Set the profile of the anonymous data connection
	 * @param profile one of the profiles constants in TorProxyLib
	 */
	private void setProfile(int profile) {
		// TODO check that profile is valid
		this.mProfile = profile;
		
		// Save this setting
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = prefs.edit();
		e.putInt(PROFILE_PREFERENCE, profile);
		e.commit();
		
		// Make the state changes required
		updateProfile();
		
		// Tell the rest of the system that a profile change has occurred
		Intent i = new Intent(TorProxyLib.PROFILE_CHANGE_INTENT);
		i.putExtra("profile", profile);
		sendBroadcast(i);
	}
	
	/**
	 * Handler for updating status notifications
	 */
	private final Handler mUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mNotifications.updateCountdown(mTimeRemaining);
		}

	};
	

}
