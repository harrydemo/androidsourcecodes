package com.renren.api.connect.android.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;

/**
 * @author 李勇(yong.li@opi-corp.com) 2010-7-19
 */
public class ConnectButtonListenerHelper implements ConnectButtonListener {
	private List<ConnectButtonListener> listeners = new ArrayList<ConnectButtonListener>();

	public void addConnectButtonListener(
			ConnectButtonListener connectButtonListener) {
		this.listeners.add(connectButtonListener);
	}

	public boolean removeConnectButtonListener(
			ConnectButtonListener connectButtonListener) {
		return this.listeners.remove(connectButtonListener);
	}

	@Override
	public void onException(Exception error) {
		for (ConnectButtonListener listener : listeners) {
			listener.onException(error);
		}
	}

	@Override
	public void onLogined(Bundle values) {
		for (ConnectButtonListener listener : listeners) {
			listener.onLogined(values);
		}
	}

	@Override
	public void onLogouted(Bundle values) {
		for (ConnectButtonListener listener : listeners) {
			listener.onLogouted(values);
		}
	}

	@Override
	public void onRenrenError(RenrenError renrenError) {
		for (ConnectButtonListener listener : listeners) {
			listener.onRenrenError(renrenError);
		}
	}

	/**
	 * 该类可以当作ConnectButtonListener接口的适配器来用；
	 * 继承该类可以只实现ConnectButtonListener接口中你感兴趣的方法。
	 * 
	 * @author yong.li@opi-corp.com
	 * 
	 */
	public static class DefaultConnectButtonListener implements
			ConnectButtonListener {

		@Override
		public void onLogined(Bundle values) {
			Log.i(Util.LOG_TAG, "onLogined called.");
		}

		@Override
		public void onLogouted(Bundle values) {
			Log.i(Util.LOG_TAG, "onLogouted called.");
		}

		@Override
		public void onRenrenError(RenrenError renrenError) {
			Log.w(Util.LOG_TAG, renrenError);
		}

		@Override
		public void onException(Exception error) {
			Log.w(Util.LOG_TAG, error);
		}
	}
}
