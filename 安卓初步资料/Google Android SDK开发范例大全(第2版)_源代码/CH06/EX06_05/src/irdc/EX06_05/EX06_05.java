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
  /*����һ��TextView,String���������������ַ�������*/
  private TextView mTextView1; 
  public String[] strEmailReciver;
  public String strEmailSubject;
  public String strEmailBody;
  /* ϵͳ���ն��ŵĹ㲥ACTION���� */
  private static final String HIPPO_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
  private mSMSReceiver mReceiver01;
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    
    /*͸��findViewById�����Ƚ���TextView����*/ 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mTextView1.setText("�ȴ����ն���..."); 
    
  }
  
  public class mSMSReceiver extends BroadcastReceiver 
  { 
    /*������̬�ַ���,��ʹ��android.provider.Telephony.SMS_RECEIVED��ΪActionΪ���ŵ�����*/
    private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED"; 
    private String str_receive="�յ�����!";
    private String strRet = "";
    
    @Override 
    public void onReceive(Context context, Intent intent) 
    {
      // TODO Auto-generated method stub 
      Toast.makeText(context, str_receive.toString(), Toast.LENGTH_LONG).show(); 
      /*�жϴ���Intent�Ƿ�Ϊ����*/
      if (intent.getAction().equals(mACTION)) 
      { 
        /*����һ���ַ�������sb*/
        StringBuilder sb = new StringBuilder(); 
        /*������Intent����������*/
        Bundle bundle = intent.getExtras(); 
        /*�ж�Intent��������*/
        if (bundle != null) 
        { 
          /* pdusΪ android�ڽ����Ų��� identifier
           * ͸��bundle.get("")����pdus�Ķ���*/
          Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
          
          /*������Ŷ���array,�������յ��Ķ��󳤶�������array�ļ�߮*/
          SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
          
          for (int i = 0; i<myOBJpdus.length; i++) 
          {  
            messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]);  
          }
          
          strRet = "";
          /* �������Ķ��Ű����Զ�����Ϣ��StringBuilder���� */  
          for (SmsMessage currentMessage : messages) 
          {
            strRet = "���յ�����:"+currentMessage.getDisplayOriginatingAddress()+" �����Ķ���"+currentMessage.getDisplayMessageBody();
            sb.append("���յ�����:\n");  
            /* �����˵ĵ绰���� */ 
            sb.append(currentMessage.getDisplayOriginatingAddress());  
            sb.append("\n------�����Ķ���------\n");  
            /* ȡ�ô������ŵ�BODY */  
            sb.append(currentMessage.getDisplayMessageBody());
          }  
        }       
        /* ��Notification(Toase)��ʾ��������  */
        Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        
        /*�Զ���Intent��ִ�м���E-mail��ʵ��*/
        Intent mEmailIntent = new Intent(android.content.Intent.ACTION_SEND);  
        /*�趨�ʼ���ʽΪ"plain/text"*/
        mEmailIntent.setType("plain/text");
        
        /*ȡ��EditText01,02,03,04��ֵ��Ϊ�ռ�,����,����,����*/
        String strEmailReciver = "jay.mingchieh@gmail.com";
        String strEmailSubject = "�м�������!!";
        
        /*��ȡ�õ�?������mEmailIntent��*/
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



