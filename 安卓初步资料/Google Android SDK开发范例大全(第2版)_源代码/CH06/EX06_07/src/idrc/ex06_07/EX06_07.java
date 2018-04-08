package idrc.ex06_07; 
import android.app.Activity;
import android.content.Intent; 
import android.os.Bundle; 
import android.telephony.PhoneStateListener; 
import android.telephony.TelephonyManager;
import android.widget.TextView; 
public class EX06_07 extends Activity 
{
  private TextView mTextView1;
  private String mEditText01 ="IRDC@gmail.com"; 
  private String strEmailSubject = "You have phone!!"; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    mPhoneCallListener phoneListener=new mPhoneCallListener();
    /*设定TelephonyManager去抓取系统TELEPHONY_SERVICE*/ 
    TelephonyManager telMgr = (TelephonyManager)getSystemService (TELEPHONY_SERVICE);
telMgr.listen(phoneListener, mPhoneCallListener. LISTEN_CALL_STATE); 
mTextView1 = (TextView)findViewById(R.id.myTextView1);
}
  /*使用PhoneCallListener来启动事件*/
  public class mPhoneCallListener extends PhoneStateListener 
  { 
    @Override public void onCallStateChanged(int state, String incomingNumber) 
    {
      // TODO Auto-generated method stub 
      switch(state) 
      { 
        /*取得电话待机状态*/ 
        case TelephonyManager.CALL_STATE_IDLE: mTextView1.setText(R.string.str_CALL_STATE_IDLE);
        break; 
        /*取得电话通话状态*/
        case TelephonyManager.CALL_STATE_OFFHOOK: mTextView1.setText(R.string.str_CALL_STATE_OFFHOOK);
        break;
        /*取得来电状态*/
        case TelephonyManager.CALL_STATE_RINGING: mTextView1.setText (
            /*显示来电号码*/
            getResources().getText(R.string.str_CALL_STATE_RINGING)+ incomingNumber );
        /*设定有电话来电时，发送E-mail*/
        Intent mEmailIntent = new Intent(android.content.Intent .ACTION_SEND);
        mEmailIntent.setType("plain/text");
        /*设定E-mail收件人的信箱*/
        mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mEditText01.toString()}); 
        /*设定E-mail的标题*/ 
        mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);
        /*设定E-mail的内容*/ mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.str_EmailBody+incomingNumber); 
        /*显示发信中*/
        startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.str_message))); 
        break; default: break;
        } 
      super.onCallStateChanged(state, incomingNumber); 
      }
    }
  }
   

  
 
