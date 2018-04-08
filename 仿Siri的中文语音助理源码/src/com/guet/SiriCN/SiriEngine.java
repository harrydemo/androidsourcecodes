/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

public class SiriEngine {
	public static final int SUCCESS = 0;
	public static final int ERROR_NO_TTS = 1;
	public static final int ERROR_XIAOI_INIT = 2;
	Handler mCallback;
	String results = "";
	private XiaoI xiaoi;
	private ChongDong chongd;
	jtTTS tts=null;
	LooperThread mThread;
	boolean isChatMode=true;
	private ProgressDialog mProgressDialog;
	Context mContext;
	boolean mIsTTsInstall=true;
	
	public SiriEngine(Context context) {
		mContext=context;
		
	}
	public void setSiriMode(boolean chatmode){
		isChatMode=chatmode;
	}
	
	
	public int SiriInit(){
		xiaoi = new XiaoI();
		if(!xiaoi.initialize()){
		//	Toast.makeText(mContext, "初始化异常，请重新启动应用!", Toast.LENGTH_SHORT);
			return ERROR_XIAOI_INIT;
		}		
		chongd = new ChongDong();	
				
		return TTSinit();
	}

	private int TTSinit(){
		tts = new jtTTS(mContext);
		if(tts==null){
			mIsTTsInstall=false;
			return ERROR_NO_TTS;
		}
		if(-1==tts.initTTSLib(1)){
			mIsTTsInstall=false;
			return ERROR_NO_TTS;
		}
		tts.SetParam(1, 10000);
		return SUCCESS;
	}
	
	public void handlerAnswer(String question, Handler handler) {
		mCallback = handler;
		results = "";
		if(mThread!=null&&mThread.isAlive())return;
		mProgressDialog =new ProgressDialog(mContext);
		if(isChatMode)
		  mProgressDialog.setMessage("稍等下^_^");
		else
		mProgressDialog.setMessage("这个问题有点难，让我思考下");
		mProgressDialog.show();	
		
		mThread=new LooperThread(question);
		mThread.start();
	}
	
	public void SiriStopSpeak(){
		if(mIsTTsInstall&&tts!=null){
		  tts.ttsStop();
		}
	}
	public void SiriStop(){
		if(mIsTTsInstall&&tts!=null){
		tts.ttsStop();
		tts.ttsEnd();
		}
	}
	
	public void SiriSpeak(String txt){
		if(mIsTTsInstall&&tts!=null){
			tts.ttsStop();
			if(txt.length()<150)
			 tts.playText(txt);
			else
			tts.playText("字太多了   你自己看吧");
		}		
	}

	class LooperThread extends Thread {
		String mQuestion;

		public LooperThread(String question) {
			mQuestion = question;
		}

		public void run() {
			if(isChatMode){
			xiaoi.sendMsg(mQuestion);
			results = xiaoi.revMsg();
			}else{
			results=chongd.getAnswer(mQuestion).toString();
			}
			Message message = new Message();
			message.what = 2012;
			message.obj = results;
			mCallback.sendMessage(message);	
			mProgressDialog.dismiss();
		}
	}
}
