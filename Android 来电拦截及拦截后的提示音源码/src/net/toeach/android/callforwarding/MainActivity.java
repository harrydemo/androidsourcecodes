package net.toeach.android.callforwarding;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.internal.telephony.ITelephony;

/**
 * 演示如何设置呼叫转移，拦截电话（拦截后提示为空号）的例子
 * @author Tony from ToEach.
 * @email wan1976@21cn.com
 */
public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
		
	private final static int OP_REGISTER = 100;
	private final static int OP_CANCEL = 200;
	
	private final static String BLOCKED_NUMBER = "1392501xxxx";//要拦截的号码
	//占线时转移，这里13800000000是空号，所以会提示所拨的号码为空号
    private final String ENABLE_SERVICE = "tel:**67*13800000000%23";
    //占线时转移
    private final String DISABLE_SERVICE = "tel:%23%2367%23";

	private IncomingCallReceiver mReceiver;
    private ITelephony iTelephony;
    private AudioManager mAudioManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        	    
        findViewById(R.id.btnEnable).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
		        //设置呼叫转移
		        Message message = mHandler.obtainMessage();
				message.what = OP_REGISTER;
				mHandler.dispatchMessage(message);
			}
        });
        
        findViewById(R.id.btnDisable).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//取消呼叫转移
	            Message message = mHandler.obtainMessage();
	    		message.what = OP_CANCEL;
	    		mHandler.dispatchMessage(message);
			}
        });
        
        mReceiver = new IncomingCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE");//拦截电话
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");//拦截短信
        registerReceiver(mReceiver, filter);// 注册BroadcastReceiver	
        
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //利用反射获取隐藏的endcall方法
        TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
			getITelephonyMethod.setAccessible(true);
			iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, (Object[]) null);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    }
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    }
    
    private Handler mHandler = new Handler() {
		public void handleMessage(Message response) {
	    	int what = response.what;	    	
	    	switch(what) {
		    	case OP_REGISTER:{
		    		Intent i = new Intent(Intent.ACTION_CALL);
		            i.setData(Uri.parse(ENABLE_SERVICE));
		            startActivity(i);
		    		break;
		    	}
		    	case OP_CANCEL:{
		    		Intent i = new Intent(Intent.ACTION_CALL);
		            i.setData(Uri.parse(DISABLE_SERVICE));
		            startActivity(i);
		    		break;
		    	}
	    	}
		}
	};
	
	private class IncomingCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG, "action:" + action);
			
			if("android.intent.action.PHONE_STATE".equals(action)){//拦截电话
				String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		        Log.i(TAG, "State: "+ state);
		        
				String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		        Log.d(TAG, "Incomng Number: " + number);
		        
		        if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){//电话正在响铃
		        	if(number.equals(BLOCKED_NUMBER)){//拦截指定的电话号码
		        		//先静音处理
		    	    	mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		    	    	Log.d(TAG, "Turn ringtone silent");
		    	    	
		        		try {
		        			//挂断电话
							iTelephony.endCall();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						//再恢复正常铃声
		                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		        	}
		        }
			}else if("android.provider.Telephony.SMS_RECEIVED".equals(action)){//拦截短信
				SmsMessage sms = getMessagesFromIntent(intent)[0];
				String number = sms.getOriginatingAddress();
				Log.d(TAG, "Incomng Number: " + number);
				number = trimSmsNumber("+86", number);//把国家代码去除掉
				if(number.equals(BLOCKED_NUMBER)){
					abortBroadcast();//这句很重要，中断广播后，其他要接收短信的应用都没法收到短信广播了
				}
			}
		}
	}
	
	/**
     * Read the PDUs out of an {@link #SMS_RECEIVED_ACTION} or a
     * {@link #DATA_SMS_RECEIVED_ACTION} intent.
     *
     * @param intent the intent to read from
     * @return an array of SmsMessages for the PDUs
     */
    public final static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
    
    public final static String trimSmsNumber(String prefix, String number){
		String s = number;
		
		if(prefix.length()>0 && number.startsWith(prefix)){
			s = number.substring(prefix.length());
		}
		
		return s;
	}
}