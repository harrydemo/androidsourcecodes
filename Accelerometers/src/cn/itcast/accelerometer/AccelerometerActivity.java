package cn.itcast.accelerometer;

import cn.itcast.accelerometer.view.BallView;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/**
 * �����������ٶȴ�����ʵ�ֹ�������Ϸ
 * �ο����ϣ�
 * 1> http://ophonesdn.com/article/show/183
 * 2> �������ٶ�ԭ��ѧϰ���� http://www.riameeting.com/node/538
 * �����������ٶȴ�������Ҫ��������ܲ��ԣ�Ϊ������ģ�����в��ԣ����԰��������´���Ի�����
 * http://www.xiaojiayi.com/
 *
 */
public class AccelerometerActivity extends Activity {
	private static final float MAX_ACCELEROMETER = 9.81f;
	private SensorManager sensorManager; 
	private BallView ball; 
	private boolean success = false; 
	private boolean init = false; 
	private int container_width = 0;
	private int container_height = 0;
	private int ball_width = 0;
	private int ball_height = 0;
	private TextView prompt;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //��ȡ��Ӧ��������
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        prompt = (TextView) findViewById(R.id.ball_prompt);   
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {//ball_container�ؼ���ʾ��������ܻ�ȡ���͸ߣ������ڴ˷����õ�����
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && !init){
			View container = findViewById(R.id.ball_container);
		    container_width = container.getWidth();
		    container_height = container.getHeight();
		    ball = (BallView) findViewById(R.id.ball);
		    ball_width = ball.getWidth();
		    ball_height = ball.getHeight();
		    moveTo(0f, 0f);
		    init = true;
		}
	}

	@Override
	protected void onResume() {
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//��ȡ�������ٶȸ�Ӧ��
		success = sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);//ע��listener�������������Ǽ��ľ�ȷ�� 
		super.onResume();
	} 
    
    @Override
	protected void onPause() {
    	if(success) sensorManager.unregisterListener(listener);
		super.onPause();
	}

	private SensorEventListener listener = new SensorEventListener() {		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (!init) return ;
			float x = event.values[SensorManager.DATA_X];      
        	float y = event.values[SensorManager.DATA_Y];      
        	float z = event.values[SensorManager.DATA_Z];  
        	prompt.setText("Sensor: " + x + ", " + y + ", " + z); 
        	//������x,yΪ0ʱ����������λ�ã���yΪ���ģ��̶���������ת���ֻ���x���ڣ�0-9.81)֮��仯�����Ŵ�����
        	moveTo(-x, y);//x����ȡ��
		}		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {			
		}
	};
	
	private void moveTo(float x, float y) {
        int max_x = (container_width - ball_width) / 2;//��x����ƶ������ֵ
        int max_y = (container_height - ball_height) / 2;//��y����ƶ������ֵ
        //�ֻ���x��y�ᴹֱ�ڷ�ʱ������������ٶ����Ϊ9.81�����ֻ���x��y���ĳ���ǶȰڷ�ʱ������x��y��Ϊ�ýǶȵļ��ٶ�
        float percentageX = x / MAX_ACCELEROMETER;//�õ���ǰ���ٶȵı��ʣ�����ֻ���x�ᴹֱ�ڷţ�����Ϊ100%��������x�����ƶ������ֵ
        float percentageY = y / MAX_ACCELEROMETER;
        int pixel_x = (int) (max_x * percentageX);//�õ�x��ƫ����
        int pixel_y = (int) (max_y * percentageY);//�õ�y��ƫ����
        //����������λ�õ�����Ϊ�ο��㣬����ƫ�������õ���Ķ�Ӧλ�ã�Ȼ���ƶ��򵽸�λ��
        ball.moveTo(max_x + pixel_x, max_y + pixel_y);
    }
}