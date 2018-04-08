package irdc.ex06_04;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/* �Զ���mService��̳�Service�� */
public class mService1 extends Service
{
  /* ����Handler������Ϊ�̴߳��� postDelayed֮�� */
  private Handler objHandler = new Handler();
  
  /* Ϊȷ��ϵͳ����ִ����� */
  private int intCounter=0;
  
  /* ��Ա����mTasksΪRunnable������ΪTimer֮�� */
  private Runnable mTasks = new Runnable() 
  {
    /* ִ���߳� */
    public void run()
    {
      /* ����counter��������Ϊ��̨��������ʱ��ʶ�� */
      intCounter++;
      
      /* ��Log����LogCat�����log��Ϣ���࿴����ִ����� */
      Log.i("HIPPO", "Counter:"+Integer.toString(intCounter));
      
      /* ÿ1�����Handler.postDelayed��������ִ�� */
      objHandler.postDelayed(mTasks, 1000);
    } 
  };
  
  @Override
  public void onStart(Intent intent, int startId)
  {
    // TODO Auto-generated method stub
    super.onStart(intent, startId);
  }

  @Override
  public void onCreate()
  {
    // TODO Auto-generated method stub
    /* ����ʼ������ÿ1��mTasks�߳� */
    objHandler.postDelayed(mTasks, 1000);
    super.onCreate();
  }
  
  @Override
  public IBinder onBind(Intent intent)
  {
    // TODO Auto-generated method stub
    
    /* IBinder����ΪService����������д�ķ��� */
    return null;
  }

  @Override
  public void onDestroy()
  {
    // TODO Auto-generated method stub
    
    /* ������������Ƴ�mTasks�߳� */
    objHandler.removeCallbacks(mTasks);
    super.onDestroy();
  }  
}

