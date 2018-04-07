/**
 * @author wjx
 *
 */
package com.tilltheendwjx.airplus.utils;

import android.widget.Toast;

public class ToastMaster {

	private static Toast sToast = null;

	private ToastMaster() {

	}

	public static void setToast(Toast toast) {
		if (sToast != null)
			sToast.cancel();
		sToast = toast;
	}

	public static void cancelToast() {
		if (sToast != null)
			sToast.cancel();
		sToast = null;
	}

}
