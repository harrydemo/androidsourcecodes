package com.pixtas.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

public class DialogHelper {
	public static Builder showAlertBuilder(final Activity activity,int msgId,int titleId){
		Builder builder = new AlertDialog.Builder(activity).setMessage(activity
				 .getString(msgId).toString())
				 .setTitle(activity.getString(titleId).toString())
				 .setCancelable(false);	 
		return builder;

	}
}
