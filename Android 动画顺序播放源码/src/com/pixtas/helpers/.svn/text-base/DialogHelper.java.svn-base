package com.pixtas.helpers;

import com.pixtas.yogapowervinyasa.YogaApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


public class DialogHelper {
	
	public static Builder showAlertBuilder(final Activity activity,int msgId,int titleId){
		Builder builder = new AlertDialog.Builder(activity).setMessage(activity
				 .getString(msgId).toString())
				 .setTitle(activity.getString(titleId).toString())
				 .setCancelable(false);	 
		return builder;

	}
	
	/*创建操作弹出窗口视图*/
	public static View showOptionDialog(Activity activity,int id){
		LayoutInflater factory = LayoutInflater.from(activity);
		View DialogView = factory.inflate(id, null);
		
		return DialogView;
	}
	
	public static Toast showToastAlert(YogaApp app,int id){
		Toast toast = Toast.makeText(app, app.getString(id), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 100);
		return toast;
	}
}
