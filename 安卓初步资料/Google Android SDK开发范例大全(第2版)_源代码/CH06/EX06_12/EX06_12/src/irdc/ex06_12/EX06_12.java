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
  /* 建立SensorManager物件 */
  private SensorManager mSensorManager01;
  private TextView mTextView01;
  
  /* 以私有類別成員儲存AudioManager模式 */
  private int strRingerMode;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* 建立SensorManager物件，取得SENSOR_SERVICE服務 */
    /* 此行程式若在1.5的AVD模擬器中，會導致當機的問題
     * 此問題已反應至Android Issue：
     * http://code.google.com/p/android/issues/detail?id=2566
     * 但在此問題回覆中，發現在安裝Eclair的實機沒有問題
     * */
    mSensorManager01 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    
    /* 取得現在的AudioManager模式 */
    GetAudioManagerMode();
    
    /* 依據現在的AudioManager模式，顯示於TextView當中 */
    switch(strRingerMode)
    {
      /* 正常模式 */
      case AudioManager.RINGER_MODE_NORMAL:
        mTextView01.setText(R.string.str_normal_mode);
        break;
      /* 靜音模式 */
      case AudioManager.RINGER_MODE_SILENT:
        mTextView01.setText(R.string.str_silent_mode);
        break;
      /* 震動模式 */
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
        /* 取得Y平面左右傾斜的Pitch角度 */
        float fPitchAngle = event.values[SensorManager.DATA_Y];
        
        /* 正面向下(Y軸旋轉)，經實驗結果為小於-120為翻背面 */
        if(fPitchAngle<-120)
        {
          /* 先設定為靜音模式 */
          ChangeToSilentMode();
          
          /* 再設定為震動模式 */
          ChangeToVibrateMode();
          
          /* 判斷鈴聲模式 */
          switch(strRingerMode)
          {
            /* 正常模式 */
            case AudioManager.RINGER_MODE_NORMAL:
              mTextView01.setText(R.string.str_normal_mode);
              break;
            /* 靜音模式 */
            case AudioManager.RINGER_MODE_SILENT:
              mTextView01.setText(R.string.str_silent_mode);
              break;
            /* 震動模式 */
            case AudioManager.RINGER_MODE_VIBRATE:
              mTextView01.setText(R.string.str_vibrate_mode);
              break;
          }
        }
        else
        {
          /* 正面向上(Y軸旋轉)，經實驗結果為大於-120為翻正面 */
          /* 變更為正常模式 */
          ChangeToNormalMode();
          
          /* 呼叫變更模式後，再一次確認手機的模式為何 */
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
  
  /* 取得當下的AudioManager模式 */
  private void GetAudioManagerMode()
  {
    try
    {
      /* 建立AudioManager物件，取得AUDIO_SERVICE */
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
  
  /* 變更為靜音模式 */
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
  
  /* 變更為震動模式 */
  private void ChangeToVibrateMode()
  {
    try
    {
      AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null)
      {
        /* 呼叫setRingerMode方法，設定模式 */
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
  
  /* 變更為正常模式 */
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
    
    /* 註冊一個SensorListener的Listener */ 
    /* 傳入Sensor模式與rate */
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
    
    /* 於覆寫onPause事件，取消mSensorListener */
    mSensorManager01.unregisterListener(mSensorListener);
    super.onPause();
  }
}