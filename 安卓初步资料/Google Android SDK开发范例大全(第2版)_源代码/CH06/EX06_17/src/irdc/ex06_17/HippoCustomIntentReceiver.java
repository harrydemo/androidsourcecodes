package irdc.ex06_17;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/* 自定义继承告BroadcastReceiver类，监听自定义系统服务广播的讯息 */
public class HippoCustomIntentReceiver extends BroadcastReceiver
{
  /* 自定义欲作为Intent Filter的ACTION信息 */
  public static final String HIPPO_SERVICE_IDENTIFIER = "HIPPO_ON_SERVICE_001";
  
  @Override
  public void onReceive(Context context, Intent intent)
  {
    // TODO Auto-generated method stub
    if(intent.getAction().toString().equals(HIPPO_SERVICE_IDENTIFIER))
    {
      /* 以Bundle对象解开传来的参数 */
      Bundle mBundle01 = intent.getExtras();
      String strParam1="";
      
      /* 若Bundle对象不为空值，取出参数 */
      if (mBundle01 != null)
      {
        /* 将取出的STR_PARAM01参数，存放于strParam1字符串中 */
        strParam1 = mBundle01.getString("STR_PARAM01");
      }
      
      /* 调用母体Activity，唤醒原加程序 */
      Intent mRunPackageIntent = new Intent(context, EX06_17.class); 
      mRunPackageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if(strParam1!="")
      {
        /* 重新封装参数（SMS信息）并传 */
        mRunPackageIntent.putExtra("STR_PARAM01", strParam1);
      }
      else
      {
        mRunPackageIntent.putExtra("STR_PARAM01", "From Service notification...");
      }
      context.startActivity(mRunPackageIntent);
    }
  } 

}

