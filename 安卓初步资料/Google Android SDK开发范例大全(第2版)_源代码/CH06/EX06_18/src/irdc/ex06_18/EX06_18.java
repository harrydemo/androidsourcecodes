package irdc.ex06_18;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class EX06_18 extends Activity
{
  private TextView TextView01;
  private SensorManager mSensorManager;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) findViewById(R.id.TextView01);

    /* 取得SensorManager对象 */
    mSensorManager =
    (SensorManager) getSystemService(Context.SENSOR_SERVICE);
  }

  @Override
  protected void onPause()
  {
    /* 取消注册 mSensorEventListener */
    mSensorManager.unregisterListener(mSensorEventListener);
    super.onPause();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    /* 注册温度计的Sensor */
    mSensorManager.registerListener
    (
      mSensorEventListener,
      mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
      SensorManager.SENSOR_DELAY_FASTEST
    );
  }
  
  private final SensorEventListener mSensorEventListener = 
  new SensorEventListener()
  {

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
      // TODO Auto-generated method stub
    }
    
    @Override
    public void onSensorChanged(SensorEvent event)
    {
      /* 如果取得的SensorEvent Type为Sensor.TYPE_TEMPERATURE */
      if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE)
      {
        /* 取得温度 */
        TextView01.setText("" + event.values[SensorManager.DATA_X]);
      }
    }
  };
}

