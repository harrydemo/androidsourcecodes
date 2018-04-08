package irdc.ex06_15;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class mService1 extends Service
{
  /* 建立Handler对象 */
  private Handler objHandler = new Handler();
  
  /* 服务里的递增整数counter */
  private int intCounter=0;
  
  /* 自定义要过滤的广播讯息(DavidLanz) */
  public static final String HIPPO_SERVICE_IDENTIFIER = "DavidLanz"; 
  
  /* 线程Tasks每1秒执行几次 */
  private Runnable mTasks = new Runnable() 
  { 
    public void run() 
    { 
      intCounter++;
      
      /* 当后台Service执行5秒后，发生自定义的广播讯息 */
      if(intCounter==5)
      {
        /* DavidLanz */
        Intent i = new Intent(HIPPO_SERVICE_IDENTIFIER);
        
        /* 透过putExtra方法封装参数并传Activity */
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
    
    /* 服务开始执行，启用线程 */
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
    
    /* 关闭服务时，关闭线程 */
    objHandler.removeCallbacks(mTasks);
    super.onDestroy();
  }
}

