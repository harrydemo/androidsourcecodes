package com.himi;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 *@author Himi
 *@Sensor 加速度传感器 ,也称为重力传感器 
 *@SDK 1.5(api 3)就支持传感器了
 *@解释：此传感器不仅对玩家反转手机的动作可以检测到，而且会根据反转手机的程度,得到传感器的值也会不同！
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private Thread th = new Thread(this);
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private SensorManager sm;
	private Sensor sensor;
	private SensorEventListener mySensorListener;
	private int arc_x, arc_y;// 圆形的x,y位置
	private float x = 0, y = 0, z = 0;

	public MySurfaceView(Context context) {
		super(context);
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		//通过服务得到传感器管理对象 
		sm = (SensorManager) MainActivity.ma.getSystemService(Service.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到一个重力传感器实例
		//TYPE_ACCELEROMETER    加速度传感器(重力传感器)类型。
		//TYPE_ALL              描述所有类型的传感器。
		//TYPE_GYROSCOPE        陀螺仪传感器类型
		//TYPE_LIGHT            光传感器类型
		//TYPE_MAGNETIC_FIELD   恒定磁场传感器类型。
		//TYPE_ORIENTATION      方向传感器类型。
		//TYPE_PRESSURE         描述一个恒定的压力传感器类型
		//TYPE_PROXIMITY        常量描述型接近传感器
		//TYPE_TEMPERATURE      温度传感器类型描述
		mySensorListener = new SensorEventListener() {
			@Override
			//传感器获取值发生改变时在响应此函数
			public void onSensorChanged(SensorEvent event) {//备注1 
				//传感器获取值发生改变，在此处理 
				x = event.values[0]; //手机横向翻滚
				//x>0 说明当前手机左翻 x<0右翻     
				y = event.values[1]; //手机纵向翻滚
				//y>0 说明当前手机下翻 y<0上翻
				z = event.values[2]; //屏幕的朝向
				//z>0 手机屏幕朝上 z<0 手机屏幕朝下
				arc_x -= x;//备注2
				arc_y += y;
			}

			@Override
			//传感器的精度发生改变时响应此函数
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		sm.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		//第一个参数是传感器监听器，第二个是需要监听的传感实例
		//最后一个参数是监听的传感器速率类型： 一共一下四种形式
		//SENSOR_DELAY_NORMAL  正常
		//SENSOR_DELAY_UI  适合界面
		//SENSOR_DELAY_GAME  适合游戏  (我们必须选这个呀 哇哈哈~)
		//SENSOR_DELAY_FASTEST  最快
	}

	public void surfaceCreated(SurfaceHolder holder) {
		arc_x = this.getWidth() / 2 - 25;
		arc_y = this.getHeight() / 2 - 25;
		th.start();
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				paint.setColor(Color.RED);
				canvas.drawArc(new RectF(arc_x, arc_y, arc_x + 50,
						arc_y + 50), 0, 360, true, paint);
				paint.setColor(Color.YELLOW);
				canvas.drawText("当前重力传感器的值:", arc_x - 50, arc_y-30, paint);
				canvas.drawText("x=" + x + ",y=" + y + ",z=" + z,
						arc_x - 50, arc_y, paint);
				String temp_str = "Himi提示： ";
				String temp_str2 = "";
				String temp_str3 = "";
				if (x < 1 && x > -1 && y < 1 && y > -1) {
					temp_str += "当前手机处于水平放置的状态";
					if (z > 0) {
						temp_str2 += "并且屏幕朝上";
					} else {
						temp_str2 += "并且屏幕朝下,提示别躺着玩手机，对眼睛不好哟~";
					}
				} else {
					if (x > 1) {
						temp_str2 += "当前手机处于向左翻的状态";
					} else if (x < -1) {
						temp_str2 += "当前手机处于向右翻的状态";
					}
					if (y > 1) {
						temp_str2 += "当前手机处于向下翻的状态";
					} else if (y < -1) {
						temp_str2 += "当前手机处于向上翻的状态";
					}
					if (z > 0) {
						temp_str3 += "并且屏幕朝上";
					} else {
						temp_str3 += "并且屏幕朝下,提示别躺着玩手机，对眼睛不好哟~";
					}
				}
				paint.setTextSize(20);
				canvas.drawText(temp_str, 0, 50, paint);
				canvas.drawText(temp_str2, 0, 80, paint);
				canvas.drawText(temp_str3, 0, 110, paint);
			}
		} catch (Exception e) {
			Log.v("Himi", "draw is Error!");
		} finally {
			sfh.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			draw();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}


//总结