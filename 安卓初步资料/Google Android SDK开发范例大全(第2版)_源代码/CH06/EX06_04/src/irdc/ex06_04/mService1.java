package irdc.ex06_04;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/* 自定义mService类继承Service类 */
public class mService1 extends Service
{
  /* 建立Handler对象，作为线程传递 postDelayed之用 */
  private Handler objHandler = new Handler();
  
  /* 为确认系统服务执行情况 */
  private int intCounter=0;
  
  /* 成员变量mTasks为Runnable对象，作为Timer之用 */
  private Runnable mTasks = new Runnable() 
  {
    /* 执行线程 */
    public void run()
    {
      /* 递增counter整数，作为后台服务运行时间识别 */
      intCounter++;
      
      /* 以Log对象LogCat里输出log信息，监看服务执行情况 */
      Log.i("HIPPO", "Counter:"+Integer.toString(intCounter));
      
      /* 每1秒调用Handler.postDelayed方法反复执行 */
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
    /* 服务开始，调用每1秒mTasks线程 */
    objHandler.postDelayed(mTasks, 1000);
    super.onCreate();
  }
  
  @Override
  public IBinder onBind(Intent intent)
  {
    // TODO Auto-generated method stub
    
    /* IBinder方法为Service建构必须重写的方法 */
    return null;
  }

  @Override
  public void onDestroy()
  {
    // TODO Auto-generated method stub
    
    /* 当服务结束，移除mTasks线程 */
    objHandler.removeCallbacks(mTasks);
    super.onDestroy();
  }  
}

