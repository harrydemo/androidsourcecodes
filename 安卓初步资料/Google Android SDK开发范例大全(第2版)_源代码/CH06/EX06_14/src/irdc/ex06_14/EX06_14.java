package irdc.ex06_14;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EX06_14 extends Activity
{
  /* ��������mServiceReceiver������Ϊ���Ա���� */
  private mServiceReceiver mReceiver01, mReceiver02;
  private Button mButton1;
  private TextView mTextView01;
  private EditText mEditText1, mEditText2;
  
  /* �Զ���ACTION��������Ϊ�㲥��Intent Filterʶ���� */
  private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
  private static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* �绰���� */
    mEditText1 = (EditText) findViewById(R.id.myEditText1);
    
    /* �������� */
    mEditText2 = (EditText) findViewById(R.id.myEditText2);
    mButton1 = (Button) findViewById(R.id.myButton1);
    
    //mEditText1.setText("+886935123456");
    /* �趨Ԥ��Ϊ5556��ʾ���ָ�ģ������Port */
    mEditText1.setText("5556");
    mEditText2.setText("Hello DavidLanz!");
    
    /* ����SMS���Ű�ť�¼����� */
    mButton1.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* �����͵ĵ绰���� */
        String strDestAddress = mEditText1.getText().toString();
        
        /* �����͵Ķ������� */
        String strMessage = mEditText2.getText().toString();
        
        /* ����SmsManager���� */
        SmsManager smsManager = SmsManager.getDefault();
        
        // TODO Auto-generated method stub
        try
        {
          /* �����Զ���Action������Intent(��PendingIntent����֮��) */
          Intent itSend = new Intent(SMS_SEND_ACTIOIN);
          Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);
          
          /* sentIntent����Ϊ���ͺ���ܵĹ㲥��ϢPendingIntent */
          PendingIntent mSendPI = PendingIntent.getBroadcast(getApplicationContext(), 0, itSend, 0);
          
          /* deliveryIntent����Ϊ�ʹ����ܵĹ㲥��ϢPendingIntent */
          PendingIntent mDeliverPI = PendingIntent.getBroadcast(getApplicationContext(), 0, itDeliver, 0);
          
          /* ����SMS���ţ�ע�⵹��������PendingIntent���� */
          smsManager.sendTextMessage(strDestAddress, null, strMessage, mSendPI, mDeliverPI);
          mTextView01.setText(R.string.str_sms_sending);
        }
        catch(Exception e)
        {
          mTextView01.setText(e.toString());
          e.printStackTrace();
        }
      }
    });
  }
  
  /* �Զ���mServiceReceiver��дBroadcastReceiver��������״̬��Ϣ */
  public class mServiceReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      // TODO Auto-generated method stub
      
      //mTextView01.setText(intent.getAction().toString());
      if (intent.getAction().equals(SMS_SEND_ACTIOIN))
      {
        try
        {
          /* android.content.BroadcastReceiver.getResultCode()���� */
          switch(getResultCode())
          {
            case Activity.RESULT_OK:
              /* ���Ͷ��ųɹ� */
              //mTextView01.setText(R.string.str_sms_sent_success);
              mMakeTextToast
              (
                getResources().getText(R.string.str_sms_sent_success).toString(),
                true
              );
              break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
              /* ���Ͷ���ʧ�� */
              //mTextView01.setText(R.string.str_sms_sent_failed);
              mMakeTextToast
              (
                getResources().getText(R.string.str_sms_sent_failed).toString(),
                true
              );
              break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
              break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
              break;
          }        
        }
        catch(Exception e)
        {
          mTextView01.setText(e.toString());
          e.getStackTrace();
        }
      }
      else if(intent.getAction().equals(SMS_DELIVERED_ACTION))
      {
        try
        {
          /* android.content.BroadcastReceiver.getResultCode()���� */
          switch(getResultCode())
          {
            case Activity.RESULT_OK:
              /* ���� */
              //mTextView01.setText(R.string.str_sms_sent_success);
              mMakeTextToast
              (
                getResources().getText(R.string.str_sms_rec_success).toString(),
                true
              );
              break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
              /* ����δ�ʹ� */
              //mTextView01.setText(R.string.str_sms_sent_failed);
              mMakeTextToast
              (
                getResources().getText(R.string.str_sms_rec_failed).toString(),
                true
              );
              break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
              break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
              break;
          }        
        }
        catch(Exception e)
        {
          mTextView01.setText(e.toString());
          e.getStackTrace();
        }
      }      
    }
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX06_14.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX06_14.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    
    /* �Զ���IntentFilterΪSENT_SMS_ACTIOIN Receiver */
    IntentFilter mFilter01;
    mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
    mReceiver01 = new mServiceReceiver();
    registerReceiver(mReceiver01, mFilter01);
    
    /* �Զ���IntentFilterΪDELIVERED_SMS_ACTION Receiver */
    mFilter01 = new IntentFilter(SMS_DELIVERED_ACTION);
    mReceiver02 = new mServiceReceiver();
    registerReceiver(mReceiver02, mFilter01);
    
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    
    /* ȡ��ע���Զ���Receiver */
    unregisterReceiver(mReceiver01);
    unregisterReceiver(mReceiver02);
    
    super.onPause();
  }
}

