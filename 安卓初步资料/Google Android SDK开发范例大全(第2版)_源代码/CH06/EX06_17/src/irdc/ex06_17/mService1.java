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

/* ����ϵͳ���񣬵�������ѶϢ�¼�ʱ����㲥�Զ�����Ϣ */
public class mService1 extends Service
{
  /* Handler��� */
  private Handler objHandler = new Handler();
  private int intCounter=0;
  
  /* �Զ���㲥ʶ��ACTIOIN���� */
  public static final String HIPPO_SERVICE_IDENTIFIER = "HIPPO_ON_SERVICE_001";
  
  /* ϵͳ���ն��ŵĹ㲥ACTION���� */
  private static final String HIPPO_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
  
  /* �Զ������mSMSReceiver���� */
  private mSMSReceiver mReceiver01;
  
  /* �Ƿ�Ϊdebugģʽ����Ҫ��console�����Log */
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
    
    /* ��ϵͳע��receiver������ϵͳ���Ź㲥�¼� */
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
  
  /* ���յ�ϵͳ���Ź㲥�¼�����¼����� */
  public class mSMSReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      // TODO Auto-generated method stub
      
      /* �ж��Ƿ�Ϊϵͳ�㲥�Ķ���ACTION */
      if (intent.getAction().equals(HIPPO_SMS_ACTION))
      {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        
        if (bundle != null)
        {
          /* �����ʶ��SMS�ж� */
          Object[] myOBJpdus = (Object[]) bundle.get("pdus");
          SmsMessage[] messages = new SmsMessage[myOBJpdus.length]; 
          for (int i = 0; i<myOBJpdus.length; i++)
          { 
            messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]); 
          }
          //Log.i(LOG_TAG, "[SMSApp Bundle] " + bundle.toString()); 
           
          /* ���������жϰ����Զ�����Ϣ��StringBuilder���� */ 
          for (SmsMessage currentMessage : messages)
          {
            sb.append(currentMessage.getDisplayOriginatingAddress());
            /* �ڵ绰��SMS����BODY֮�䣬���Ϸָ�TAG */
            sb.append(EX06_17.strDelimiter1);
            sb.append(currentMessage.getDisplayMessageBody());
          }
          
          /* ��ϵͳ�㲥�Զ�����Ϣ */
          Intent i = new Intent(HIPPO_SERVICE_IDENTIFIER);
          i.putExtra("STR_PARAM01", sb.toString());
          
          /* ��sendBroadcast�����㲥��Ϣ */
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

