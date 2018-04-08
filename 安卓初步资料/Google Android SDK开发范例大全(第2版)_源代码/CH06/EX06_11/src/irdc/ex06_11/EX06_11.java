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
    
    /*设定PhoneCallListener*/
    mPhoneCallListener phoneListener=new mPhoneCallListener(); 
    /*设定TelephonyManager去抓取Telephony Severice*/
    TelephonyManager telMgr = (TelephonyManager)getSystemService(
        TELEPHONY_SERVICE);
    /*设定Listen Call*/
    telMgr.listen(phoneListener, mPhoneCallListener.
        LISTEN_CALL_STATE); 
    
    /*设定找寻TextView、EditText*/ 
    mTextView01 = (TextView)findViewById(R.id.myTextView1); 
    mTextView03 = (TextView)findViewById(R.id.myTextView3);
    mEditText1 = (EditText)findViewById(R.id.myEditText1);
    
  } 
  /*判断PhoneStateListener现在的状态*/
  public class mPhoneCallListener extends PhoneStateListener 
  { 
    @Override 
    public void onCallStateChanged(int state, String incomingNumber) 
    { 
      // TODO Auto-generated method stub 
      switch(state)  
      {  
        /*判断手机目前是待机状态*/ 
        case TelephonyManager.CALL_STATE_IDLE: 
          mTextView01.setText(R.string.str_CALL_STATE_IDLE); 
          
          try
          { 
            AudioManager audioManager = (AudioManager)
                           getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) 
            { 
              /*设定手机为待机时，响铃模式为正常*/ 
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
          
        /*判断状态为通话中*/  
        case TelephonyManager.CALL_STATE_OFFHOOK: 
          mTextView01.setText(R.string.str_CALL_STATE_OFFHOOK); 
          break;
          
        /*判断状态为来电*/   
        case TelephonyManager.CALL_STATE_RINGING: 
          /*显示现在来电的讯息*/
          mTextView01.setText( 
              getResources().getText(R.string.str_CALL_STATE_RINGING)+ 
            incomingNumber);
         
          /*判断与输入电话是否为一样，当一样时响铃模式转为静音*/ 
          if(incomingNumber.equals(mTextView03.getText().toString()))
          {                 
          try
          {             
            AudioManager audioManager = (AudioManager)
                            getSystemService(Context.AUDIO_SERVICE);
          if (audioManager != null)
            {
            /*设定响铃模式为静音*/ 
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
          /*设定在EditText里所输入的数据同步显示在TextView*/
          mTextView03.setText(mEditText1.getText());
          return false;
        }
      });
    }
  }
}

