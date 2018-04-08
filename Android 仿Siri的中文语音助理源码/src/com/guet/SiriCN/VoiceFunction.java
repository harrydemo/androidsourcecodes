/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import android.os.Handler;
import android.os.Message;

public class VoiceFunction implements VoiceActionListener{
	public static final int VOICE_RESPONSE = 100;
	  private MainActivity mActivity;
	  FunctionPointer mCancel;
	  Handler mHandler;
      
	  public VoiceFunction(MainActivity activity,FunctionPointer cancel)
	  {
	    mCancel = cancel;
	    mActivity = activity;
	  }

	  void getVoiceResponse(String ask,String type,Handler handler)
	  {
	    mHandler = handler;
	    mActivity.registerVoiceListener(this);
	    mActivity.speak(ask,true);
	    if(type.equals("contact"))
	   // mActivity.isrDialog.setEngine(null,null,mActivity.contact);
	    mActivity.isrDialog.show();
	  }

	  public void onVoiceResult(String msg)
	  {
	    mActivity.unregisterVoiceListener();
	    if ((msg == null) && (mCancel != null))
	      mCancel.callback();
	      Message message = new Message();
	      message.what = VOICE_RESPONSE;
	      message.obj = msg;
	      mHandler.sendMessage(message);
	      
	      mActivity.isrDialog.setEngine("sms", null, null);
	  }
}
