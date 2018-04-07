package com.hrw.android.player;

import com.hrw.android.player.media.IPlayerEngine;
import com.hrw.android.player.media.PlayerEngineImpl;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class BelmotPlayer extends Application {
	public static String TAG = "BelmotPlayer";

	private IPlayerEngine playerEngine;

	public IPlayerEngine getPlayerEngine() {
		if (null == playerEngine) {
			playerEngine = new PlayerEngineImpl();
		}
		return playerEngine;
	}

	private static BelmotPlayer instance;

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
	}

	public static BelmotPlayer getInstance() {
		return instance;
	}

	/**
	 * Retrieves application's version number from the manifest
	 * 
	 * @return
	 */
	public String getVersion() {
		String version = "0.0.0";

		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}
}
