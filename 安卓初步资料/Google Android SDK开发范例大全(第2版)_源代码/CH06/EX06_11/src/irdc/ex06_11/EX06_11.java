package irdc.ex06_11;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EX06_11 extends Activity 
{
  private TextView mTextView01;
  private TextView mTextView03;
  private EditText mEditText1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /*�趨PhoneCallListener*/
    mPhoneCallListener phoneListener=new mPhoneCallListener(); 
    /*�趨TelephonyManagerȥץȡTelephony Severice*/
    TelephonyManager telMgr = (TelephonyManager)getSystemService(
        TELEPHONY_SERVICE);
    /*�趨Listen Call*/
    telMgr.listen(phoneListener, mPhoneCallListener.
        LISTEN_CALL_STATE); 
    
    /*�趨��ѰTextView��EditText*/ 
    mTextView01 = (TextView)findViewById(R.id.myTextView1); 
    mTextView03 = (TextView)findViewById(R.id.myTextView3);
    mEditText1 = (EditText)findViewById(R.id.myEditText1);
    
  } 
  /*�ж�PhoneStateListener���ڵ�״̬*/
  public class mPhoneCallListener extends PhoneStateListener 
  { 
    @Override 
    public void onCallStateChanged(int state, String incomingNumber) 
    { 
      // TODO Auto-generated method stub 
      switch(state)  
      {  
        /*�ж��ֻ�Ŀǰ�Ǵ���״̬*/ 
        case TelephonyManager.CALL_STATE_IDLE: 
          mTextView01.setText(R.string.str_CALL_STATE_IDLE); 
          
          try
          { 
            AudioManager audioManager = (AudioManager)
                           getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) 
            { 
              /*�趨�ֻ�Ϊ����ʱ������ģʽΪ����*/ 
              audioManager.setRingerMode(AudioManager.
                  RINGER_MODE_NORMAL);               
              audioManager.getStreamVolume(
                  AudioManager.STREAM_RING);
            } 
          }         
          catch(Exception e)
          { 
           mTextView01.setText(e.toString()); 
           e.printStackTrace(); 
          }
          break;
          
        /*�ж�״̬Ϊͨ����*/  
        case TelephonyManager.CALL_STATE_OFFHOOK: 
          mTextView01.setText(R.string.str_CALL_STATE_OFFHOOK); 
          break;
          
        /*�ж�״̬Ϊ����*/   
        case TelephonyManager.CALL_STATE_RINGING: 
          /*��ʾ���������ѶϢ*/
          mTextView01.setText( 
              getResources().getText(R.string.str_CALL_STATE_RINGING)+ 
            incomingNumber);
         
          /*�ж�������绰�Ƿ�Ϊһ������һ��ʱ����ģʽתΪ����*/ 
          if(incomingNumber.equals(mTextView03.getText().toString()))
          {                 
          try
          {             
            AudioManager audioManager = (AudioManager)
                            getSystemService(Context.AUDIO_SERVICE);
          if (audioManager != null)
            {
            /*�趨����ģʽΪ����*/ 
            audioManager.setRingerMode(AudioManager.
                  RINGER_MODE_SILENT); 
            audioManager.getStreamVolume(
                  AudioManager.STREAM_RING);
            Toast.makeText(EX06_11.this, getString(R.string.str_msg) 
                ,Toast.LENGTH_SHORT).show(); 
            } 
          }
          catch(Exception e)
          { 
           mTextView01.setText(e.toString()); 
           e.printStackTrace();         
          break;
          }      
        } 
      }
      
      super.onCallStateChanged(state, incomingNumber);
      
      mEditText1.setOnKeyListener(new EditText.OnKeyListener()
      {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
          // TODO Auto-generated method stub
          /*�趨��EditText�������������ͬ����ʾ��TextView*/
          mTextView03.setText(mEditText1.getText());
          return false;
        }
      });
    }
  }
}

