package irdc.ex06_21;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class EX06_21 extends Activity
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
    /* ȡ��SensorManager */
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

  }

  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    super.onResume();
    /* ȡ�÷����Ե�Sensor����ע��SensorEventListener */
    mSensorManager.registerListener(mSensorEventListener, mSensorManager
        .getDefaultSensor(Sensor.TYPE_ORIENTATION),
        SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    /* ȡ��ע��SensorEventListener */
    mSensorManager.unregisterListener(mSensorEventListener);
    super.onPause();
  }

  private final SensorEventListener mSensorEventListener = new SensorEventListener()
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
      /* �ж�Sensor������ */
      if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
      {
        /* ȡ��Xֵ���� */
        float x_data = event.values[SensorManager.DATA_X];
        if ((x_data > 0 && x_data <= 22.5) || x_data > 337.5)
        {
          TextView01.setText("����" + String.valueOf(x_data));
        } else if (x_data > 22.5 && x_data <= 67.5)
        {
          TextView01.setText("������" + String.valueOf(x_data));
        } else if (x_data > 67.5 && x_data <= 112.5)
        {
          TextView01.setText("����" + String.valueOf(x_data));
        } else if (x_data > 112.5 && x_data <= 157.5)
        {
          TextView01.setText("���Ϸ�" + String.valueOf(x_data));
        } else if (x_data > 157.5 && x_data <= 202.5)
        {
          TextView01.setText("�Ϸ�" + String.valueOf(x_data));
        } else if (x_data > 202.5 && x_data <= 247.5)
        {
          TextView01.setText("���Ϸ�" + String.valueOf(x_data));
        } else if (x_data > 247.5 && x_data <= 292.5)
        {
          TextView01.setText("����" + String.valueOf(x_data));
        } else if (x_data > 292.5 && x_data <= 337.5)
        {
          TextView01.setText("������" + String.valueOf(x_data));
        }
      }
    }
  };
}

