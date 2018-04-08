package irdc.ex06_16;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* 捕捉android.intent.action.BOOT_COMPLETED的Receiver类 */
public class HippoStartupIntentReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive(Context context, Intent intent)
  {
    // TODO Auto-generated method stub
    
    /* 当收到Receiver时，指定开启否程序（EX06_16.class） */
    Intent mBootIntent = new Intent(context, EX06_16.class);
    
    /* 设定Intent开启为FLAG_ACTIVITY_NEW_TASK */ 
    mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    
    /* 将Intent以startActivity传送给操作系统 */ 
    context.startActivity(mBootIntent);
  }
}

