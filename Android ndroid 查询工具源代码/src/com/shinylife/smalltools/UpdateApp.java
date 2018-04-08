package com.shinylife.smalltools;

import com.shinylife.smalltools.api.ApiImpl;
import com.shinylife.smalltools.entity.AppUpdateInfo;
import com.shinylife.smalltools.helper.Constants;
import com.shinylife.smalltools.helper.InternetHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UpdateApp {
	private Activity mActivity;
	private Context mContext;
	private ProgressDialog pBar;
	private final int QUERY_MSG = 103;

	public UpdateApp(Activity activity) {
		mActivity = activity;
		mContext = mActivity.getApplicationContext();
	}

	public void checkVersion() {
		boolean hasNet = new InternetHelper(mActivity).getNetworkIsAvailable();
		if (!hasNet) {
			ShowToast(R.string.no_internet);
			return;
		}
		pBar = new ProgressDialog(mActivity);
		pBar.setMessage(mContext.getString(R.string.querying));
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pBar.show();
		new Thread() {
			@Override
			public void run() {
				ApiImpl impl = new ApiImpl();
				Message msg = new Message();
				msg.what = QUERY_MSG;
				msg.obj = impl.getNewVersion(Constants.VERSION);
				hander.sendMessage(msg);
			}
		}.start();
	}

	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (pBar.isShowing()) {
				pBar.dismiss();
			}
			if (msg.obj == null) {
				ShowToast(R.string.no_new_version);
				return;
			}
			switch (msg.what) {
			case QUERY_MSG: {
				final AppUpdateInfo pi = (AppUpdateInfo) msg.obj;
				if (!pi.isHasNewVersion()) {
					ShowToast(R.string.no_new_version);
					return;
				}
				new AlertDialog.Builder(mActivity)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage(
								mContext.getString(R.string.has_new_version,
										pi.getAppSize()))
						.setPositiveButton(
								R.string.confirm,
								new android.content.DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										downloadNewApp(pi.getUrl());
									}
								}).setNegativeButton(R.string.cancel, null)
						.create().show();
			}
				break;
			default:
				break;
			}
		}
	};

	private void downloadNewApp(String url) {
		Intent it = new Intent("android.intent.action.VIEW", Uri.parse(url));
		mActivity.startActivity(it);
	}

	private void ShowToast(int id) {
		ShowToast(mContext.getString(id));
	}

	private void ShowToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}
}
