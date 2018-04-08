package irdc.ex06_17;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

/* 定义系统服务，当监听到讯息事件时，则广播自定义信息 */
public class mService1 extends Service
{
  /* Handler物件 */
  private Handler objHandler = new Handler();
  private int intCounter=0;
  
  /* 自定义广播识别ACTIOIN常数 */
  public static final String HIPPO_SERVICE_IDENTIFIER = "HIPPO_ON_SERVICE_001";
  
  /* 系统接收短信的广播ACTION常数 */
  private static final String HIPPO_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
  
  /* 自定义短信mSMSReceiver对象 */
  private mSMSReceiver mReceiver01;
  
  /* 是否为debug模式，需要在console里输出Log */
  private static boolean bIfDebug = true;
  
  private Runnable mTasks = new Runnable() 
  { 
    public void run() 
    { 
      intCounter++;
      Log.i("HIPPO", "Counter:"+Integer.toString(intCounter));
      objHandler.postDelayed(mTasks, 1000); 
    } 
  };
  
  @Override
  public void onStart(Intent intent, int startId)
  {
    // TODO Auto-generated method stub
    
    if(bIfDebug)
    {
      objHandler.postDelayed(mTasks, 1000);
    }
    super.onStart(intent, startId);
  }

  @Override
  public void onCreate()
  {
    // TODO Auto-generated method stub
    
    /* 向系统注册receiver，监听系统短信广播事件 */
    IntentFilter mFilter01;
    mFilter01 = new IntentFilter(HIPPO_SMS_ACTION);
    mReceiver01 = new mSMSReceiver();
    registerReceiver(mReceiver01, mFilter01);
    
    super.onCreate();
  }
  
  @Override
  public IBinder onBind(Intent intent)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onDestroy()
  {
    // TODO Auto-generated method stub
    
    objHandler.removeCallbacks(mTasks);
    unregisterReceiver(mReceiver01);
    super.onDestroy();
  }
  
  /* 当收到系统短信广播事件后的事件处理 */
  public class mSMSReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      // TODO Auto-generated method stub
      
      /* 判断是否为系统广播的短信ACTION */
      if (intent.getAction().equals(HIPPO_SMS_ACTION))
      {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        
        if (bundle != null)
        {
          /* 拆解与识别SMS判断 */
          Object[] myOBJpdus = (Object[]) bundle.get("pdus");
          SmsMessage[] messages = new SmsMessage[myOBJpdus.length]; 
          for (int i = 0; i<myOBJpdus.length; i++)
          { 
            messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]); 
          }
          //Log.i(LOG_TAG, "[SMSApp Bundle] " + bundle.toString()); 
           
          /* 将送来的判断安并自定义信息在StringBuilder当中 */ 
          for (SmsMessage currentMessage : messages)
          {
            sb.append(currentMessage.getDisplayOriginatingAddress());
            /* 在电话与SMS短信BODY之间，加上分隔TAG */
            sb.append(EX06_17.strDelimiter1);
            sb.append(currentMessage.getDisplayMessageBody());
          }
          
          /* 向系统广播自定义信息 */
          Intent i = new Intent(HIPPO_SERVICE_IDENTIFIER);
          i.putExtra("STR_PARAM01", sb.toString());
          
          /* 向sendBroadcast发生广播信息 */
          sendBroadcast(i);
        }
      }
      else
      {
        Intent i = new Intent(HIPPO_SERVICE_IDENTIFIER);
        i.putExtra("STR_PARAM01", intent.getAction().toString());
        sendBroadcast(i);
      }
    }
  }
}

