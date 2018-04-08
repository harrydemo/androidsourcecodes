package zhinanzheng.com;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
/**
 * @author  �������� troyjie@yahoo.cn
 * @version  1.0
 */
public class Zhinanzheng extends Activity implements SensorEventListener{
	
	ImageView image;  //ָ����ͼƬ
	float currentDegree = 0f; //ָ����ͼƬת���ĽǶ�
	
	SensorManager mSensorManager; //������
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        image = (ImageView)findViewById(R.id.znzImage);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE); //��ȡ�������
    }
    
    @Override 
    protected void onResume(){
    	super.onResume();
    	//ע�������
    	mSensorManager.registerListener(this
    			, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }
    
    //ȡ��ע��
    @Override
    protected void onPause(){
    	mSensorManager.unregisterListener(this);
    	super.onPause();
    	
    }
    
    @Override
    protected void onStop(){
    	mSensorManager.unregisterListener(this);
    	super.onStop();
    	
    }

    //������ֵ�ı�
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
		
	}
    //���ȸı�
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//��ȡ����event�Ĵ���������
		int sensorType = event.sensor.getType();
		
		switch(sensorType){
		case Sensor.TYPE_ORIENTATION:
			float degree = event.values[0]; //��ȡzת���ĽǶ�
			//������ת����
			RotateAnimation ra = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF,0.5f
					,Animation.RELATIVE_TO_SELF,0.5f);
		 ra.setDuration(100);//��������ʱ��
		 image.startAnimation(ra);
		 currentDegree = -degree;
		 break;
		
		}
	}
}