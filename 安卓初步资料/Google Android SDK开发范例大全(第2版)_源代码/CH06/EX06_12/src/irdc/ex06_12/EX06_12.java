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
  /* 建立SensorManager对象 */
  private SensorManager mSensorManager01;
  private TextView mTextView01;
  
  /* 以私有类成员保存AudioManager模式 */
  private int strRingerMode;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* 建立SensorManager对象，取得SENSOR_SERVICE服务 */
    /* 否行程序若1.5的AVD模拟器中，会导致Hang的问题
     * 否问题?反应吹Android Issue：
     * http://code.google.com/p/android/issues/detail?id=2566
     * 困?否问题并覆中，有网友发现??装CupCake的实机没有问题
     * */
    mSensorManager01 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    
    /* 取得现有的AudioManager模式 */
    GetAudioManagerMode();
    
    /* 依据现有的AudioManager模式，显示于TextView当中 */
    switch(strRingerMode)
    {
      /* 正常模式 */
      case AudioManager.RINGER_MODE_NORMAL:
        mTextView01.setText(R.string.str_normal_mode);
        break;
      /* 静音模式 */
      case AudioManager.RINGER_MODE_SILENT:
        mTextView01.setText(R.string.str_silent_mode);
        break;
      /* 震动模式 */
      case AudioManager.RINGER_MODE_VIBRATE:
        mTextView01.setText(R.string.str_vibrate_mode);
        break;
    }
  }
  
  /* 建立SensorListener捕捉onSensorChanged事件 */
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
        /* 取得Y平面倾斜的Pitch角度 */
        float fPitchAngle = event.values[SensorManager.DATA_Y];
        
        /* 正面(Y轴旋转)，经实验结果为小于-120为翻背面 */
        if(fPitchAngle<-120)
        {
          /* 因设定为静音模式 */
          ChangeToSilentMode();
          
          /* 设定为震动模式 */
          ChangeToVibrateMode();
          
          /* 判断铃声模式 */
          switch(strRingerMode)
          {
            /* 正常模式 */
            case AudioManager.RINGER_MODE_NORMAL:
              mTextView01.setText(R.string.str_normal_mode);
              break;
            /* 静音模式 */
            case AudioManager.RINGER_MODE_SILENT:
              mTextView01.setText(R.string.str_silent_mode);
              break;
            /* 震动模式 */
            case AudioManager.RINGER_MODE_VIBRATE:
              mTextView01.setText(R.string.str_vibrate_mode);
              break;
          }
        }
        else
        {
          /* 正面守勺(Y轴旋转)，经实验结果为大于-120为翻正面 */
          /* 变更为正常模式 */
          ChangeToNormalMode();
          
          /* 调用更改模式后 */
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
  
  /* 取得当前的AudioManager模式 */
  private void GetAudioManagerMode()
  {
    try
    {
      /* 建立AudioManager对象，取得AUDIO_SERVICE */
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
  
  /* 变更为静音模式 */
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
  
  /* 变更为震动模式 */
  private void ChangeToVibrateMode()
  {
    try
    {
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* 调用setRingerMode方法，设定模式 */
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
  
  /* 变更为正常模式 */
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
    
    /* 注册几个SensorListener的Listener */ 
    /* 传入Sensor模式与rate */
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
    
    /* 重写onPause事件，取消mSensorListener */
    mSensorManager01.unregisterListener(mSensorListener);
    super.onPause();
  }
}

