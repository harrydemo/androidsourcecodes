package irdc.ex06_12;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.TextView;

public class EX06_12 extends Activity
{
  /* �إ�SensorManager���� */
  private SensorManager mSensorManager01;
  private TextView mTextView01;
  
  /* �H�p�����O�����x�sAudioManager�Ҧ� */
  private int strRingerMode;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* �إ�SensorManager����A���oSENSOR_SERVICE�A�� */
    /* ����{���Y�b1.5��AVD���������A�|�ɭP��������D
     * �����D�w������Android Issue�G
     * http://code.google.com/p/android/issues/detail?id=2566
     * ���b�����D�^�Ф��A�o�{�b�w��Eclair������S�����D
     * */
    mSensorManager01 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    
    /* ���o�{�b��AudioManager�Ҧ� */
    GetAudioManagerMode();
    
    /* �̾ڲ{�b��AudioManager�Ҧ��A��ܩ�TextView�� */
    switch(strRingerMode)
    {
      /* ���`�Ҧ� */
      case AudioManager.RINGER_MODE_NORMAL:
        mTextView01.setText(R.string.str_normal_mode);
        break;
      /* �R���Ҧ� */
      case AudioManager.RINGER_MODE_SILENT:
        mTextView01.setText(R.string.str_silent_mode);
        break;
      /* �_�ʼҦ� */
      case AudioManager.RINGER_MODE_VIBRATE:
        mTextView01.setText(R.string.str_vibrate_mode);
        break;
    }
  }
  
  /* �إ�SensorListener����onSensorChanged�ƥ� */
  private final SensorEventListener mSensorListener = new SensorEventListener()
  {
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
      // TODO Auto-generated method stub
      if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
      {
        //float fRollAngle = event.values[SensorManager.DATA_X];
        /* ���oY�������k�ɱת�Pitch���� */
        float fPitchAngle = event.values[SensorManager.DATA_Y];
        
        /* �����V�U(Y�b����)�A�g���絲�G���p��-120��½�I�� */
        if(fPitchAngle<-120)
        {
          /* ���]�w���R���Ҧ� */
          ChangeToSilentMode();
          
          /* �A�]�w���_�ʼҦ� */
          ChangeToVibrateMode();
          
          /* �P�_�a�n�Ҧ� */
          switch(strRingerMode)
          {
            /* ���`�Ҧ� */
            case AudioManager.RINGER_MODE_NORMAL:
              mTextView01.setText(R.string.str_normal_mode);
              break;
            /* �R���Ҧ� */
            case AudioManager.RINGER_MODE_SILENT:
              mTextView01.setText(R.string.str_silent_mode);
              break;
            /* �_�ʼҦ� */
            case AudioManager.RINGER_MODE_VIBRATE:
              mTextView01.setText(R.string.str_vibrate_mode);
              break;
          }
        }
        else
        {
          /* �����V�W(Y�b����)�A�g���絲�G���j��-120��½���� */
          /* �ܧ󬰥��`�Ҧ� */
          ChangeToNormalMode();
          
          /* �I�s�ܧ�Ҧ���A�A�@���T�{������Ҧ����� */
          switch(strRingerMode)
          {
            case AudioManager.RINGER_MODE_NORMAL:
              mTextView01.setText(R.string.str_normal_mode);
              break;
            case AudioManager.RINGER_MODE_SILENT:
              mTextView01.setText(R.string.str_silent_mode);
              break;
            case AudioManager.RINGER_MODE_VIBRATE:
              mTextView01.setText(R.string.str_vibrate_mode);
              break;
          }
        }
      }
    }
  };
  
  /* ���o��U��AudioManager�Ҧ� */
  private void GetAudioManagerMode()
  {
    try
    {
      /* �إ�AudioManager����A���oAUDIO_SERVICE */
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* RINGER_MODE_NORMAL | RINGER_MODE_SILENT  | RINGER_MODE_VIBRATE */
        strRingerMode = audioManager.getRingerMode();
      }
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.printStackTrace();
    }
  }
  
  /* �ܧ��R���Ҧ� */
  private void ChangeToSilentMode()
  {
    try
    {
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* RINGER_MODE_NORMAL | RINGER_MODE_SILENT  | RINGER_MODE_VIBRATE */
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        strRingerMode = audioManager.getRingerMode();
      }
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.printStackTrace();
    }
  }
  
  /* �ܧ󬰾_�ʼҦ� */
  private void ChangeToVibrateMode()
  {
    try
    {
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* �I�ssetRingerMode��k�A�]�w�Ҧ� */
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        /* RINGER_MODE_NORMAL | RINGER_MODE_SILENT  | RINGER_MODE_VIBRATE */
        strRingerMode = audioManager.getRingerMode();
      }
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.printStackTrace();
    }
  }
  
  /* �ܧ󬰥��`�Ҧ� */
  private void ChangeToNormalMode()
  {
    try
    {
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* RINGER_MODE_NORMAL | RINGER_MODE_SILENT  | RINGER_MODE_VIBRATE */
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        strRingerMode = audioManager.getRingerMode();
      }
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.printStackTrace();
    }
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    
    /* ���U�@��SensorListener��Listener */ 
    /* �ǤJSensor�Ҧ��Prate */
    List<Sensor> sensors = mSensorManager01.getSensorList(Sensor.TYPE_ORIENTATION);
    mSensorManager01.registerListener
    ( 
      mSensorListener, 
      sensors.get(0), 
      SensorManager.SENSOR_DELAY_NORMAL
    );
    
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    
    /* ���мgonPause�ƥ�A����mSensorListener */
    mSensorManager01.unregisterListener(mSensorListener);
    super.onPause();
  }
}