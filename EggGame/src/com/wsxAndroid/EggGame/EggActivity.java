package com.wsxAndroid.EggGame;



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class EggActivity extends Activity implements SensorListener{
	private final static String TAG="EggActivity";
	
	private MediaPlayer mPlayer=new MediaPlayer();
	private ImageView img1,img2;
	private float x, y, z,last_x,last_y,last_z;
    private long lastUpdate;
    int jd=1;
    int i=0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        img1=(ImageView)findViewById(R.id.img1);
        img2=(ImageView)findViewById(R.id.img2);
        SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE); 
        Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到一个重力传感器实例
        //TYPE_ACCELEROMETER    加速度传感器(重力传感器)类型。
		//TYPE_ALL              描述所有类型的传感器。
		//TYPE_GYROSCOPE        陀螺仪传感器类型
		//TYPE_LIGHT            光传感器类型
		//TYPE_MAGNETIC_FIELD   恒定磁场传感器类型。
		//TYPE_ORIENTATION      方向传感器类型。
		//TYPE_PRESSURE         描述一个恒定的压力传感器类型
		//TYPE_PROXIMITY        常量描述型接近传感器
		//TYPE_TEMPERATURE      温度传感器类型描述
        sensorMgr.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);
        //第一个参数是传感器监听器，第二个是需要监听的传感实例
		//最后一个参数是监听的传感器速率类型： 一共一下四种形式
		//SENSOR_DELAY_NORMAL  正常
		//SENSOR_DELAY_UI  适合界面
		//SENSOR_DELAY_GAME  适合游戏  (我们必须选这个呀 哇哈哈~)
		//SENSOR_DELAY_FASTEST  最快
        Toast.makeText(EggActivity.this, "温馨提示：摔坏手机与本人无关！",Toast.LENGTH_LONG).show();
    }	
	
	@Override
	//传感器的精度发生改变时响应此函数
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	//传感器获取值发生改变时在响应此函数
	public void onSensorChanged(int sensor, float[] values) {
		// TODO Auto-generated method stub
		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {   
			long curTime = System.currentTimeMillis();
			// 每100毫秒检测一次
			if ((curTime - lastUpdate) >= 200) { 
				long diffTime = (curTime - lastUpdate);  
				lastUpdate = curTime;
				x = values[SensorManager.DATA_X];   
				y = values[SensorManager.DATA_Y];   
				z = values[SensorManager.DATA_Z];   		  
					float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;
					//大于灵敏度5000的时候执行
					if (speed > 2000) {   
						innt(speed);
					}
					last_x = x;
					last_y = y;
					last_z = z;
			}   
		}
	}
	void innt(float a){
		AnimationDrawable anim = null;
		if (a > 3000 && i==3) {
			img1.setBackgroundResource(R.anim.da4);
			anim=(AnimationDrawable)img1.getBackground();
			mPlayer=MediaPlayer.create(EggActivity.this, R.raw.sound_dakai);
			mPlayer.start();
			anim.stop();
			anim.start();
			jd++;
			shuliang();
			i=0;
		}else if (a > 2500 && i==2) {
			img1.setBackgroundResource(R.anim.da3);
			anim=(AnimationDrawable)img1.getBackground();
			mPlayer=MediaPlayer.create(EggActivity.this, R.raw.sound_da);
			mPlayer.start();
			anim.stop();
			anim.start();
			i=3;
		}else if (a > 2000 && i==1) {
			img1.setBackgroundResource(R.anim.da2);
			anim=(AnimationDrawable)img1.getBackground();
			mPlayer=MediaPlayer.create(EggActivity.this, R.raw.sound_da);
			mPlayer.start();
			anim.stop();
			anim.start();
			i=2;
		}else if(a > 1500 && i==0){
			img1.setBackgroundResource(R.anim.da1);
			anim=(AnimationDrawable)img1.getBackground();
			mPlayer=MediaPlayer.create(EggActivity.this, R.raw.sound_da);
			mPlayer.start();
			anim.stop();
			anim.start();
			i=1;
		}

	}
	void shuliang(){
		img2.setBackgroundDrawable(getResources().getDrawable(imgkai[jd]));
		if(jd==imgkai.length-1){
			jd=0;
		}
	}
	
	public int[] imgkai=new int[]{
		R.drawable.kai_1,
		R.drawable.kai_2,
		R.drawable.kai_3,
		R.drawable.kai_4,
		R.drawable.kai_5,
		R.drawable.kai_6,
		R.drawable.kai_7,
		R.drawable.kai_8,
		R.drawable.kai_9,
		R.drawable.kai_10,
		R.drawable.kai_11,
		R.drawable.kai_12,
		R.drawable.kai_13,
		R.drawable.kai_14,
		R.drawable.kai_15,
	};
	 @Override
	public void onStart(){
		super.onStart();
		Log.i(TAG, "EggActivity-->onStart");
	}
	@Override
	public void onResume(){
		super.onResume();
		Log.i(TAG, "EggActivity-->onResume");
	}
	@Override
	public void onPause(){
		//不能进行比较耗时的操作
		//对数据进行保存，持久化操作
		super.onPause();
		Log.i(TAG, "EggActivity-->onPause");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void onStop(){
		super.onStop();
		Log.i(TAG, "EggActivity-->onStop");
	}
	@Override
	public void onRestart(){
		super.onRestart();
		Log.i(TAG, "EggActivity-->onRestart");
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.i(TAG, "EggActivity-->onDestroy");
	}
}