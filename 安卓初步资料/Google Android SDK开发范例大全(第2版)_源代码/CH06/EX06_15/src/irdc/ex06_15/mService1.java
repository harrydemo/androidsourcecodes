package irdc.ex06_15;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class mService1 extends Service
{
  /* ����Handler���� */
  private Handler objHandler = new Handler();
  
  /* ������ĵ�������counter */
  private int intCounter=0;
  
  /* �Զ���Ҫ���˵Ĺ㲥ѶϢ(DavidLanz) */
  public static final String HIPPO_SERVICE_IDENTIFIER = "DavidLanz"; 
  
  /* �߳�Tasksÿ1��ִ�м��� */
  private Runnable mTasks = new Runnable() 
  { 
    public void run() 
    { 
      intCounter++;
      
      /* ����̨Serviceִ��5��󣬷����Զ���Ĺ㲥ѶϢ */
      if(intCounter==5)
      {
        /* DavidLanz */
        Intent i = new Intent(HIPPO_SERVICE_IDENTIFIER);
        
        /* ͸��putExtra������װ��������Activity */
        i.putExtra
        (
          "STR_PARAM1",
          getResources().getText(R.string.str_message_from_service).toString()
        );
        sendBroadcast(i);
      }
      Log.i("HIPPO", "Service Running Counter:"+Integer.toString(intCounter));
      objHandler.postDelayed(mTasks, 1000); 
    } 
  };
  
  @Override
  public void onStart(Intent intent, int startId)
  {
    // TODO Auto-generated method stub
    
    /* ����ʼִ�У������߳� */
    objHandler.postDelayed(mTasks, 1000);
    super.onStart(intent, startId);
  }

  @Override
  public void onCreate()
  {
    // TODO Auto-generated method stub
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
    
    /* �رշ���ʱ���ر��߳� */
    objHandler.removeCallbacks(mTasks);
    super.onDestroy();
  }
}

