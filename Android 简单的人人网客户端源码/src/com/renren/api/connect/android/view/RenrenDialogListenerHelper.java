package com.renren.api.connect.android.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;

/**
 * 使RenrenDialog可以增加多个DialogListener；提供DialogListener缺省实现。
 * 
 * @author 李勇(yong.li@opi-corp.com) 2010-7-15
 */
public class RenrenDialogListenerHelper implements RenrenDialogListener {
	private List<RenrenDialogListener> listeners = new ArrayList<RenrenDialogListener>();

	public void addDialogListener(RenrenDialogListener dialogListener) {
		this.listeners.add(dialogListener);
	}

	public boolean removeDialogListener(RenrenDialogListener dialogListener) {
		return this.listeners.remove(dialogListener);
	}

	public void onComplete(Bundle values) {
		for (RenrenDialogListener listener : listeners) {
			listener.onComplete(values);
		}
	}

	public void onRenrenError(RenrenError renrenError) {
		for (RenrenDialogListener listener : listeners) {
			listener.onRenrenError(renrenError);
		}
	}

	public void onHttpError(int errorCode, String description, String failingUrl) {
		for (RenrenDialogListener listener : listeners) {
			listener.onHttpError(errorCode, description, failingUrl);
		}
	}

	@Override
	public void onCancel(Bundle values) {
		for (RenrenDialogListener listener : listeners) {
			listener.onCancel(values);
		}
	}

	/**
	 * 该类可以当作DialogListener接口的适配器来用；继承该类可以只实现DialogListener接口中你感兴趣的方法。
	 * 
	 * @author 李勇(yong.li@opi-corp.com) 2010-7-16
	 * 
	 */
	public static class DefaultRenrenDialogListener implements
			RenrenDialogListener {

		@Override
		public void onComplete(Bundle values) {
			Log.i(Util.LOG_TAG,
					"DefaultRenrenDialogListener.onComplete values:" + values);
		}

		@Override
		public void onCancel(Bundle values) {
			Log.w(Util.LOG_TAG, "DefaultRenrenDialogListener.onCancel values:"
					+ values);
		}

		@Override
		public void onRenrenError(RenrenError renrenError) {
			Log.w(Util.LOG_TAG,
					"DefaultRenrenDialogListener.onRenrenError renrenError: "
							+ renrenError);
		}

		@Override
		public void onHttpError(int errorCode, String description,
				String failingUrl) {
			Log.w(Util.LOG_TAG,
					"DefaultRenrenDialogListener.onError errorCode: "
							+ errorCode + " errorMsg:" + description
							+ " failurl:" + failingUrl);
		}
	}
}
