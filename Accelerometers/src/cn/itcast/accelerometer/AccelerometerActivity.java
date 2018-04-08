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
 * 利用重力加速度传感器实现滚桌球游戏
 * 参考资料：
 * 1> http://ophonesdn.com/article/show/183
 * 2> 重力加速度原理学习资料 http://www.riameeting.com/node/538
 * 由于重力加速度传感器需要在真机才能测试，为了能在模拟器中测试，可以按下面文章搭建测试环境：
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
        //获取感应器管理器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        prompt = (TextView) findViewById(R.id.ball_prompt);   
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {//ball_container控件显示出来后才能获取其宽和高，所以在此方法得到其宽高
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
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获取重力加速度感应器
		success = sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);//注册listener，第三个参数是检测的精确度 
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
        	//当重力x,y为0时，球处于中心位置，以y为轴心（固定不动），转动手机，x会在（0-9.81)之间变化，负号代表方向
        	moveTo(-x, y);//x方向取反
		}		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {			
		}
	};
	
	private void moveTo(float x, float y) {
        int max_x = (container_width - ball_width) / 2;//在x轴可移动的最大值
        int max_y = (container_height - ball_height) / 2;//在y轴可移动的最大值
        //手机沿x、y轴垂直摆放时，自由落体加速度最大为9.81，当手机沿x、y轴成某个角度摆放时，变量x和y即为该角度的加速度
        float percentageX = x / MAX_ACCELEROMETER;//得到当前加速度的比率，如果手机沿x轴垂直摆放，比率为100%，即球在x轴上移动到最大值
        float percentageY = y / MAX_ACCELEROMETER;
        int pixel_x = (int) (max_x * percentageX);//得到x轴偏移量
        int pixel_y = (int) (max_y * percentageY);//得到y轴偏移量
        //以球在中心位置的坐标为参考点，加上偏移量，得到球的对应位置，然后移动球到该位置
        ball.moveTo(max_x + pixel_x, max_y + pixel_y);
    }
}