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
  /* ����SensorManager���� */
  private SensorManager mSensorManager01;
  private TextView mTextView01;
  
  /* ��˽�����Ա����AudioManagerģʽ */
  private int strRingerMode;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* ����SensorManager����ȡ��SENSOR_SERVICE���� */
    /* ���г�����1.5��AVDģ�����У��ᵼ��Hang������
     * ������?��Ӧ��Android Issue��
     * http://code.google.com/p/android/issues/detail?id=2566
     * ��?�����Ⲣ���У������ѷ���??װCupCake��ʵ��û������
     * */
    mSensorManager01 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    
    /* ȡ�����е�AudioManagerģʽ */
    GetAudioManagerMode();
    
    /* �������е�AudioManagerģʽ����ʾ��TextView���� */
    switch(strRingerMode)
    {
      /* ����ģʽ */
      case AudioManager.RINGER_MODE_NORMAL:
        mTextView01.setText(R.string.str_normal_mode);
        break;
      /* ����ģʽ */
      case AudioManager.RINGER_MODE_SILENT:
        mTextView01.setText(R.string.str_silent_mode);
        break;
      /* ��ģʽ */
      case AudioManager.RINGER_MODE_VIBRATE:
        mTextView01.setText(R.string.str_vibrate_mode);
        break;
    }
  }
  
  /* ����SensorListener��׽onSensorChanged�¼� */
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
        /* ȡ��Yƽ����б��Pitch�Ƕ� */
        float fPitchAngle = event.values[SensorManager.DATA_Y];
        
        /* ����(Y����ת)����ʵ����ΪС��-120Ϊ������ */
        if(fPitchAngle<-120)
        {
          /* ���趨Ϊ����ģʽ */
          ChangeToSilentMode();
          
          /* �趨Ϊ��ģʽ */
          ChangeToVibrateMode();
          
          /* �ж�����ģʽ */
          switch(strRingerMode)
          {
            /* ����ģʽ */
            case AudioManager.RINGER_MODE_NORMAL:
              mTextView01.setText(R.string.str_normal_mode);
              break;
            /* ����ģʽ */
            case AudioManager.RINGER_MODE_SILENT:
              mTextView01.setText(R.string.str_silent_mode);
              break;
            /* ��ģʽ */
            case AudioManager.RINGER_MODE_VIBRATE:
              mTextView01.setText(R.string.str_vibrate_mode);
              break;
          }
        }
        else
        {
          /* ��������(Y����ת)����ʵ����Ϊ����-120Ϊ������ */
          /* ���Ϊ����ģʽ */
          ChangeToNormalMode();
          
          /* ���ø���ģʽ�� */
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
  
  /* ȡ�õ�ǰ��AudioManagerģʽ */
  private void GetAudioManagerMode()
  {
    try
    {
      /* ����AudioManager����ȡ��AUDIO_SERVICE */
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
  
  /* ���Ϊ����ģʽ */
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
  
  /* ���Ϊ��ģʽ */
  private void ChangeToVibrateMode()
  {
    try
    {
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* ����setRingerMode�������趨ģʽ */
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
  
  /* ���Ϊ����ģʽ */
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
    
    /* ע�Ἰ��SensorListener��Listener */ 
    /* ����Sensorģʽ��rate */
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
    
    /* ��дonPause�¼���ȡ��mSensorListener */
    mSensorManager01.unregisterListener(mSensorListener);
    super.onPause();
  }
}

