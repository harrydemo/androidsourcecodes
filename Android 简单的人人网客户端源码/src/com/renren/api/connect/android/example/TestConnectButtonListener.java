package com.renren.api.connect.android.example;

import android.os.Bundle;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.view.ConnectButtonListener;

/**
 * @author 李勇(yong.li@opi-corp.com) 2010-7-15
 */
public class TestConnectButtonListener implements ConnectButtonListener {
	private Example example;

	TestConnectButtonListener(Example example) {
		this.example = example;
	}

	@Override
	public void onLogined(Bundle values) {
		example.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Util.showAlert(example, "Logined", "Success logined!", true);
				example.display.setText("sessionKey:"
						+ example.renren.getSessionKey());
			}
		});
	}

	@Override
	public void onLogouted(final Bundle values) {
		example.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				example.display.setText("sessionKey:null");
				Util.showAlert(example, "Logouted", "Success logouted!");
			}
		});
	}

	@Override
	public void onRenrenError(final RenrenError error) {
		example.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Util.showAlert(example, "RenrenError", error.toString());
			}
		});
	}

	@Override
	public void onException(final Exception exception) {
		exception.printStackTrace();
		example.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Util.showAlert(example, "Exception", exception.toString());
			}
		});

	}
}
