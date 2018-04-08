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
       
      /* ��I��Service����5���A�e�X�s���T�� */ 
      if(intCounter==5) 
      { 
        Intent i = new Intent(Test_SERVICE_IDENTIFIER); 
         
        /* �z�LputExtra��k�ʸ˰ѼƦ^��Activity */ 
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