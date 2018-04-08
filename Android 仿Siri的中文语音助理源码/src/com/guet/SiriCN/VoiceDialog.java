/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

public class VoiceDialog implements VoiceActionListener {
	public static final String[] YES_TEXT = { "��", "�ǵ�","yes","ȷ��" };
	public static final String[] NO_TEXT = { "��", "����", "no" };
	MainActivity mActivity;
	FunctionPointer mCancel;
	String mMsg;
	FunctionPointer mRight;
	FunctionPointer mWrong;

	public VoiceDialog(MainActivity activity, String msg,
			FunctionPointer funRight, FunctionPointer funWrong,
			FunctionPointer funCancel) {
		mActivity = activity;
		mRight = funRight;
		mWrong = funWrong;
		mCancel = funCancel;
		mMsg = msg;
		Log.e("hmg","msg= "+msg);
		//mActivity.registerVoiceListener(this);
		mActivity.speak(msg,true);
	//	mActivity.isrDialog.show();
		dialog(msg);
	}

	public void onVoiceResult(String result) {
		if ((result == null) && (mCancel != null)) {
			mCancel.callback();
			mActivity.unregisterVoiceListener();
			return;
		}
		String answer;
		// �����ȼ��no�������Ѳ�����ƥ��Ϊ��
		answer = result;
		for (int i = 0; i < NO_TEXT.length; i++) {
			answer = answer.replaceAll(NO_TEXT[i], "no");
		}
		if ((answer.contains("no")) && (mWrong != null)) {
			mWrong.callback();
			mActivity.unregisterVoiceListener();
			return;
		}
		answer = result;
		for (int i = 0; i < YES_TEXT.length; i++) {
			answer = answer.replaceAll(YES_TEXT[i], "yes");
		}
		if ((answer.contains("yes")) && (mRight != null)) {
			mRight.callback();
			mActivity.unregisterVoiceListener();
			return;
		}

	}
	
	protected void dialog(String msg) {
		  AlertDialog.Builder builder = new Builder(mActivity);
		  builder.setMessage(msg);
		  builder.setTitle("��ʾ");
		  builder.setPositiveButton("ȷ��", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    mRight.callback();
		   }
		  });
		  builder.setNegativeButton("ȡ��", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    mWrong.callback();
		   }
		  });
		  builder.create().show();
		 }

}
