package irdc.EX06_05;

import android.app.Activity; 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle; 
import android.telephony.SmsMessage;
import android.widget.TextView; 
import android.widget.Toast;

public class EX06_05 extends Activity 
{
  /*声明一个TextView,String阵在与两个文字字符串变量*/
  private TextView mTextView1; 
  public String[] strEmailReciver;
  public String strEmailSubject;
  public String strEmailBody;
  /* 系统接收短信的广播ACTION常数 */
  private static final String HIPPO_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
  private mSMSReceiver mReceiver01;
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    
    /*透过findViewById构造巳建立TextView对象*/ 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mTextView1.setText("等待接收短信..."); 
    
  }
  
  public class mSMSReceiver extends BroadcastReceiver 
  { 
    /*声明静态字符串,并使用android.provider.Telephony.SMS_RECEIVED作为Action为短信的依据*/
    private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED"; 
    private String str_receive="收到短信!";
    private String strRet = "";
    
    @Override 
    public void onReceive(Context context, Intent intent) 
    {
      // TODO Auto-generated method stub 
      Toast.makeText(context, str_receive.toString(), Toast.LENGTH_LONG).show(); 
      /*判断传来Intent是否为短信*/
      if (intent.getAction().equals(mACTION)) 
      { 
        /*构造一个字符串变量sb*/
        StringBuilder sb = new StringBuilder(); 
        /*接收由Intent传来的数据*/
        Bundle bundle = intent.getExtras(); 
        /*判断Intent是有资料*/
        if (bundle != null) 
        { 
          /* pdus为 android内建短信参数 identifier
           * 透过bundle.get("")并传pdus的对象*/
          Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
          
          /*构造短信对象array,并依据收到的对象长度来建立array的己弋*/
          SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
          
          for (int i = 0; i<myOBJpdus.length; i++) 
          {  
            messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]);  
          }
          
          strRet = "";
          /* 将送来的短信安并自定义信息在StringBuilder当中 */  
          for (SmsMessage currentMessage : messages) 
          {
            strRet = "接收到来自:"+currentMessage.getDisplayOriginatingAddress()+" 传来的短信"+currentMessage.getDisplayMessageBody();
            sb.append("接收到来自:\n");  
            /* 发信人的电话号码 */ 
            sb.append(currentMessage.getDisplayOriginatingAddress());  
            sb.append("\n------传来的短信------\n");  
            /* 取得传来短信的BODY */  
            sb.append(currentMessage.getDisplayMessageBody());
          }  
        }       
        /* 以Notification(Toase)显示短信内容  */
        Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        
        /*自定义Intent来执行寄送E-mail的实现*/
        Intent mEmailIntent = new Intent(android.content.Intent.ACTION_SEND);  
        /*设定邮件格式为"plain/text"*/
        mEmailIntent.setType("plain/text");
        
        /*取得EditText01,02,03,04的值作为收件,附件,主题,内容*/
        String strEmailReciver = "jay.mingchieh@gmail.com";
        String strEmailSubject = "有几条短信!!";
        
        /*将取得的?串放丈mEmailIntent中*/
        mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, strEmailReciver); 
        mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);
        mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strRet);
        context.startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.str_message)));
        mTextView1.setText(getResources().getString(R.string.str_message));
      }
    } 
  }

  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    super.onPause();
    unregisterReceiver(mReceiver01);
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    IntentFilter mFilter01;
    mFilter01 = new IntentFilter(HIPPO_SMS_ACTION);
    mReceiver01 = new mSMSReceiver();
    registerReceiver(mReceiver01, mFilter01);
    super.onResume();
  }
  
}



