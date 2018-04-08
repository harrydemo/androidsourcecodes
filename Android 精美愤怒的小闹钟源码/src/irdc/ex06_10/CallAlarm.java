package irdc.ex06_10;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;

/* 调用闹钟Alert的Receiver */
public class CallAlarm extends BroadcastReceiver
{
  @Override
  public void onReceive(Context context, Intent intent)
  {
    
    /* create Intent，调用AlarmAlert.class */
    Intent i = new Intent(context, AlarmAlert.class);
        
    Bundle bundleRet = new Bundle();    
    bundleRet.putString("STR_CALLER", "");
    i.putExtras(bundleRet);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
    
  }
}
 
