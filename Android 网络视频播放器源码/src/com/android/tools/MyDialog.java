package com.android.tools;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import com.sph.player.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

//我的对话框页面
public class MyDialog
{

	private AlertDialog alertDialog;
	private Context context;
	public ProgressBar pageWaitDialog;

	//构造函数
	public MyDialog(Context context1){
		alertDialog = null;
		pageWaitDialog = null;
		context = context1;
	}

	//获取当前等待进度条
	private ProgressBar GetWaitProgressDialog(){
		pageWaitDialog = (ProgressBar)LayoutInflater.from(context).
			inflate(R.layout.waiting_alert_dialog, null).findViewById(R.id.circleProgressBarAlert);

		return pageWaitDialog;
	}

	//取消进度条
	public void HiddleDialgo(){
		if (alertDialog != null && alertDialog.isShowing()){
			alertDialog.dismiss();
			alertDialog = null;
		}
	}

	//取消进度条显示
	public void OpenPageWaitDialogDismiss(){
		if (pageWaitDialog != null && pageWaitDialog.isShown()){
			pageWaitDialog.setVisibility(View.INVISIBLE);
			pageWaitDialog = null;
		}
	}

	//显示进度条
	public void OpenPageWaitDialogShow(){
		if (pageWaitDialog == null || pageWaitDialog.isShown()){
			if (pageWaitDialog == null){
				pageWaitDialog = GetWaitProgressDialog();
				pageWaitDialog.setVisibility(View.VISIBLE);
			}
		}else {
			pageWaitDialog.setVisibility(View.VISIBLE);
		}
	}

	//显示对话框
	public void ShowDialog(){
		android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.
				Builder(context)).setTitle(R.string.request_timeout_title)
				.setMessage(R.string.request_timeout_content);
		alertDialog = builder.setPositiveButton(R.string.button_confirm,
				new  DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i){
						HiddleDialgo();
					}
				}).show();
	}

}
