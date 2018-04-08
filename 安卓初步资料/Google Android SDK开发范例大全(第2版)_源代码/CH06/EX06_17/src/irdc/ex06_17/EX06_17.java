package irdc.ex06_17;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EX06_17 extends Activity
{
  private Button mButton01,mButton02;
  private TextView mTextView01;
    
  /* ˫�����ʶ��ؼ��� */
  private static String strSecretWord="IRDC";
  
  /* �㲥��Ϣ��Delimiter�ϻش���ʶTAG */
  public static String strDelimiter1="<delimiter1>";
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    try
    {
      /* ����Bundle�����ж��Ƿ��д����װ���� */
      Bundle mBundle01 = this.getIntent().getExtras();
      if (mBundle01 != null)
      {
        /* ȡ�ò�������STR_PARAM01 */
        String strParam01 = mBundle01.getString("STR_PARAM01");     
        String[] aryTemp01 = null;
        
        /* ����Ϊ���Թ㲥��ѶϢ��������ʾ�����Լ��Ĺ㲥�¼� */
        if(eregi(strDelimiter1,strParam01))
        {
          /* �ж�strDelimiter������������ */
          aryTemp01 = strParam01.split(strDelimiter1);
          
          /*�ж�����Ԫ��[0]�Ƿ�Ϊ�й�̨������ĵ绰�����Լ��Զ��Źؼ��ֽ��м�� */
          if(isTWCellPhone(aryTemp01[0]) && eregi(strSecretWord,aryTemp01[1]) && aryTemp01.length==2)
          {
            /* ��ʾ�Ѳ�׽��˫����Źؼ��� */
            mMakeTextToast
            (
              getResources().getText(R.string.str_cmd_sms_catched).toString(), false
            );
            
            /* ԭ���Ͷ���User�ĵ绰�����ǲ������ŵĵ绰���� */
            String strDestAddress = aryTemp01[0];
            
            /* ����ģ����֮���Ƿ�˳��������Port Number */
            //String strDestAddress = "5556";
            
            /* Ҫ�ش���SMS BODY���� */
            String strMessage = getResources().getText(R.string.str_cmd_sms_returned).toString();
            
            /* ����SmsManager���� */
            SmsManager smsManager = SmsManager.getDefault();
            
            // TODO Auto-generated method stub
            try
            {
              /* ����PendingIntent��ΪsentIntent���� */
              PendingIntent mPI = PendingIntent.getBroadcast(EX06_17.this, 0, new Intent(), 0);
              
              /* ֱ�Ӵ��Ͷ��� */
              smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
              
              /* ϵͳ�涯��������֮����Toast��ʾ��� */
              mMakeTextToast
              (
                getResources().getText(R.string.str_cmd_sms_sending).toString()+
                strDestAddress,
                true
              );
            }
            catch(Exception e)
            {
              e.printStackTrace();
            }
            finish();
          }
          else
          {
            /* ��û�з��ֱ�ʶ������绰���� */
            /* �ж��Ƿ�Ϊ�����Լ����Զ���SMS Receiver�㲥ѶϢ */
            if(eregi(strDelimiter1,strParam01))
            {
              aryTemp01 = strParam01.split(strDelimiter1);
              mTextView01.setText(aryTemp01[1].toString());
            }
            else
            {
              /* û���Լ��Ĺ㲥�¼�������Ϊһ��SMS���� */
              mTextView01.setText(strParam01);
            }
          }
        }
        else
        {
          if(eregi(strDelimiter1,strParam01))
          {
            aryTemp01 = strParam01.split(strDelimiter1);
            mTextView01.setText(aryTemp01[1].toString());
          }
          else
          {
            /* û���Լ��Ĺ㲥�¼�������Ϊ����SMS���� */
            mTextView01.setText(strParam01);
          }
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    
    /* ��ʼ����˫����ŷ���(mService1)���� */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        Intent i = new Intent( EX06_17.this, mService1.class ); 
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
        startService(i);
        mMakeTextToast(getResources().getText(R.string.str_service_online).toString(),true);
        finish();
      }
    });
    
    mButton02 = (Button)findViewById(R.id.myButton2);
    
    /* ��ֹ����˫����ŷ���(mService1) */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        Intent i = new Intent( EX06_17.this, mService1.class );
        stopService(i);
        mMakeTextToast(getResources().getText(R.string.str_service_offline).toString(),true);
      }
    });
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX06_17.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX06_17.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  /* �жϽ��յĶ����Ƿ�Ϊ�йؼ��ֵĶ��� */
  public static boolean isCommandSMS(String strPat, String strSMS)
  {
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strSMS);
    return m.find();
  }
  
  /* �Զ���������ʽ�������ִ�Сд�ȶ��ַ��� */
  public static boolean eregi(String strPat, String strUnknow)
  {
    /* ����һ */
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.find();
    
    /* ������*/
    /*
    if(strUnknow.toLowerCase().indexOf(strPat.toLowerCase())>=0)
    {
      return true;
    }
    else
    {
      return false;
    }
    */
  }
  
  /* �жϼ�Ѷ�����ߵ����磬�Ƿ�Ϊ�й�̨��������ƶ��绰��ʽ */
  public static boolean isTWCellPhone(String strUnknow)
  {
    /*
     * (0935)456-789, 0935-456-789, 1234567890, (0935)-456-789
     * */
    String strPattern = "^\\(?(\\d{4})\\)?[-]?(\\d{3})[-]?(\\d{3})$";
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.matches();
  }
  
  /* �ж϶��ŷ����ߵ����磬�Ƿ�Ϊ�����ƶ��绰��ʽ */
  public static boolean isUSCellPhone(String strUnknow)
  {
    /*
     * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
     * */
    String strPattern = "^\\(?(\\d{3})\\)?[-]?(\\d{3})[-]?(\\d{4})$";
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.matches();
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    super.onPause();
  }
}

