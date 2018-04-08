/*
 * Copyright (C) 2010-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.network;

import org.geometerplus.android.util.UIUtil;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;
import org.geometerplus.zlibrary.core.resources.ZLResource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

class NetworkInitializer {
	static NetworkInitializer Instance;

	private Activity myActivity;

	public NetworkInitializer(Activity activity) {
		Instance = this;
		setActivity(activity);
	}

	public void setActivity(Activity activity) {
		myActivity = activity;
	}

	final DialogInterface.OnClickListener myListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				NetworkInitializer.this.start();
			} else if (myActivity != null) {
				myActivity.finish();
			}
		}
	};

	// run this method only if myActivity != null
	private void runInitialization() {
		UIUtil.wait("loadingNetworkLibrary", new Runnable() {
			public void run() {
				String error = null;
				try {
					NetworkView.Instance().initialize();
					if (myActivity instanceof NetworkTopLevelActivity) {
						((NetworkTopLevelActivity)myActivity).processSavedIntent();
					}
				} catch (ZLNetworkException e) {
					error = e.getMessage();
				}
				NetworkInitializer.this.end(error);
			}
		}, myActivity);
	}

	private void showTryAgainDialog(Activity activity, String error) {
		final ZLResource dialogResource = ZLResource.resource("dialog");
		final ZLResource boxResource = dialogResource.getResource("networkError");
		final ZLResource buttonResource = dialogResource.getResource("button");
		new AlertDialog.Builder(activity)
			.setTitle(boxResource.getResource("title").getValue())
			.setMessage(error)
			.setIcon(0)
			.setPositiveButton(buttonResource.getResource("tryAgain").getValue(), myListener)
			.setNegativeButton(buttonResource.getResource("cancel").getValue(), myListener)
			.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					myListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
				}
			})
			.create().show();
	}

	public void start() {
		if (myActivity != null) {
			myActivity.runOnUiThread(new Runnable() {
				public void run() {
					runInitialization();
				}
			});
		}
	}

	private void end(final String error) {
		if (myActivity != null) {
			myActivity.runOnUiThread(new Runnable() {
				public void run() {
					if (error == null) {
						if (myActivity instanceof NetworkTopLevelActivity) {
							final NetworkTopLevelActivity a = (NetworkTopLevelActivity)myActivity;
							a.startService(new Intent(a.getApplicationContext(), LibraryInitializationService.class));
							a.onModelChanged(); // initialization is complete successfully
						}
					} else {
						showTryAgainDialog(myActivity, error); // handle initialization error
					}
				}
			});
		}
	}
}
