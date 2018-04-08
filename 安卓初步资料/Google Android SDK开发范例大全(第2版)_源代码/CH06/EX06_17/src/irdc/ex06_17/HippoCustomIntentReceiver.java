package irdc.ex06_17;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/* �Զ���̳и�BroadcastReceiver�࣬�����Զ���ϵͳ����㲥��ѶϢ */
public class HippoCustomIntentReceiver extends BroadcastReceiver
{
  /* �Զ�������ΪIntent Filter��ACTION��Ϣ */
  public static final String HIPPO_SERVICE_IDENTIFIER = "HIPPO_ON_SERVICE_001";
  
  @Override
  public void onReceive(Context context, Intent intent)
  {
    // TODO Auto-generated method stub
    if(intent.getAction().toString().equals(HIPPO_SERVICE_IDENTIFIER))
    {
      /* ��Bundle����⿪�����Ĳ��� */
      Bundle mBundle01 = intent.getExtras();
      String strParam1="";
      
      /* ��Bundle����Ϊ��ֵ��ȡ������ */
      if (mBundle01 != null)
      {
        /* ��ȡ����STR_PARAM01�����������strParam1�ַ����� */
        strParam1 = mBundle01.getString("STR_PARAM01");
      }
      
      /* ����ĸ��Activity������ԭ�ӳ��� */
      Intent mRunPackageIntent = new Intent(context, EX06_17.class); 
      mRunPackageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if(strParam1!="")
      {
        /* ���·�װ������SMS��Ϣ������ */
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

