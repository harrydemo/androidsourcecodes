package irdc.ex06_03;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class mService1 extends Service
{
  private Handler objHandler = new Handler(); 
  private int intCounter=0; 
  public static final String Test_SERVICE_IDENTIFIER = "@string/str_Text";  
  
  private Runnable mTasks = new Runnable()  
  {  
    public void run()  
    {  
      intCounter++; 
       
      /* 當背景Service執行5秒後，送出廣播訊息 */ 
      if(intCounter==5) 
      { 
        Intent i = new Intent(Test_SERVICE_IDENTIFIER); 
         
        /* 透過putExtra方法封裝參數回傳Activity */ 
        i.putExtra("STR_PARAM1", "Service Message here..."); 
        sendBroadcast(i); 
      } 
      Log.i("IRDC", "Service Running Counter:"+Integer.toString(intCounter)); 
      objHandler.postDelayed(mTasks, 1000);  
    }  
  }; 
  @Override 
  public void onStart(Intent intent, int startId) 
  { 
    // TODO Auto-generated method stub 
     
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
     
    objHandler.removeCallbacks(mTasks); 
    super.onDestroy(); 
  } 
}