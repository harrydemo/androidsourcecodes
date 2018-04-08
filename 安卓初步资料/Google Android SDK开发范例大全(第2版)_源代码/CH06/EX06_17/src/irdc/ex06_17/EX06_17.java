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
    
  /* 双向短信识别关键字 */
  private static String strSecretWord="IRDC";
  
  /* 广播信息加Delimiter上回传标识TAG */
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
      /* 建立Bundle对象，判断是否有传入封装参数 */
      Bundle mBundle01 = this.getIntent().getExtras();
      if (mBundle01 != null)
      {
        /* 取得参数名称STR_PARAM01 */
        String strParam01 = mBundle01.getString("STR_PARAM01");     
        String[] aryTemp01 = null;
        
        /* 发现为来自广播的讯息参数，表示来自自己的广播事件 */
        if(eregi(strDelimiter1,strParam01))
        {
          /* 判断strDelimiter，并以数组存放 */
          aryTemp01 = strParam01.split(strDelimiter1);
          
          /*判断数组元素[0]是否为中国台湾地区的电话号码以及对短信关键字进行检查 */
          if(isTWCellPhone(aryTemp01[0]) && eregi(strSecretWord,aryTemp01[1]) && aryTemp01.length==2)
          {
            /* 显示已捕捉到双向短信关键字 */
            mMakeTextToast
            (
              getResources().getText(R.string.str_cmd_sms_catched).toString(), false
            );
            
            /* 原发送短信User的电话，亦是并传短信的电话号码 */
            String strDestAddress = aryTemp01[0];
            
            /* 测试模拟器之间是否顺利发生的Port Number */
            //String strDestAddress = "5556";
            
            /* 要回传的SMS BODY内容 */
            String strMessage = getResources().getText(R.string.str_cmd_sms_returned).toString();
            
            /* 建立SmsManager对象 */
            SmsManager smsManager = SmsManager.getDefault();
            
            // TODO Auto-generated method stub
            try
            {
              /* 建立PendingIntent囫为sentIntent参数 */
              PendingIntent mPI = PendingIntent.getBroadcast(EX06_17.this, 0, new Intent(), 0);
              
              /* 直接传送短信 */
              smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
              
              /* 系统告动并传短信之后，以Toast显示结果 */
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
            /* 若没有发现标识的来电电话号码 */
            /* 判断是否为来自自己的自定义SMS Receiver广播讯息 */
            if(eregi(strDelimiter1,strParam01))
            {
              aryTemp01 = strParam01.split(strDelimiter1);
              mTextView01.setText(aryTemp01[1].toString());
            }
            else
            {
              /* 没有自己的广播事件，纯粹为一般SMS短信 */
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
            /* 没有自己的广播事件，纯粹为几般SMS短信 */
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
    
    /* 开始聆听双向短信服务(mService1)启动 */
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
    
    /* 终止聆听双向短信服务(mService1) */
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
  
  /* 判断接收的短信是否为有关键字的短信 */
  public static boolean isCommandSMS(String strPat, String strSMS)
  {
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strSMS);
    return m.find();
  }
  
  /* 自定义正则表达式，无区分大小写比对字符串 */
  public static boolean eregi(String strPat, String strUnknow)
  {
    /* 方法一 */
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.find();
    
    /* 方法二*/
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
  
  /* 判断简讯发送者的来电，是否为中国台湾地区的移动电话格式 */
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
  
  /* 判断短信发送者的来电，是否为美国移动电话格式 */
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

