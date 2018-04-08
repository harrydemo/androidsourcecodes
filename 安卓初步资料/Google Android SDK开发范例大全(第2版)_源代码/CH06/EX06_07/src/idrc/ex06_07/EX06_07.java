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
    /*�趨TelephonyManagerȥץȡϵͳTELEPHONY_SERVICE*/ 
    TelephonyManager telMgr = (TelephonyManager)getSystemService (TELEPHONY_SERVICE);
telMgr.listen(phoneListener, mPhoneCallListener. LISTEN_CALL_STATE); 
mTextView1 = (TextView)findViewById(R.id.myTextView1);
}
  /*ʹ��PhoneCallListener�������¼�*/
  public class mPhoneCallListener extends PhoneStateListener 
  { 
    @Override public void onCallStateChanged(int state, String incomingNumber) 
    {
      // TODO Auto-generated method stub 
      switch(state) 
      { 
        /*ȡ�õ绰����״̬*/ 
        case TelephonyManager.CALL_STATE_IDLE: mTextView1.setText(R.string.str_CALL_STATE_IDLE);
        break; 
        /*ȡ�õ绰ͨ��״̬*/
        case TelephonyManager.CALL_STATE_OFFHOOK: mTextView1.setText(R.string.str_CALL_STATE_OFFHOOK);
        break;
        /*ȡ������״̬*/
        case TelephonyManager.CALL_STATE_RINGING: mTextView1.setText (
            /*��ʾ�������*/
            getResources().getText(R.string.str_CALL_STATE_RINGING)+ incomingNumber );
        /*�趨�е绰����ʱ������E-mail*/
        Intent mEmailIntent = new Intent(android.content.Intent .ACTION_SEND);
        mEmailIntent.setType("plain/text");
        /*�趨E-mail�ռ��˵�����*/
        mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mEditText01.toString()}); 
        /*�趨E-mail�ı���*/ 
        mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);
        /*�趨E-mail������*/ mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.str_EmailBody+incomingNumber); 
        /*��ʾ������*/
        startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.str_message))); 
        break; default: break;
        } 
      super.onCallStateChanged(state, incomingNumber); 
      }
    }
  }
   

  
 
