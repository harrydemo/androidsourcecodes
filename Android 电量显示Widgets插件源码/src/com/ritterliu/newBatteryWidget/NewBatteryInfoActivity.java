package com.ritterliu.newBatteryWidget;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class NewBatteryInfoActivity extends Activity{
	/** 定义电池信息变量  */
	private static int currentBatteryPlugged=0;
	private static int currentBatteryStatus=0;
	private static int currentBatteryLevel=0;
	private static int currentBatteryHealth=0;
	private static int currentBatteryTemperature=0;
	private static int currentBatteryVoltage=0;
	private static String currentBatteryTechnology="";

	
	/** TextView 声明 */
    private static TextView tvBatteryStatus;
    private static TextView tvBatteryLevel;
    private static TextView tvBatteryHealth;
    private static TextView tvBatteryTemperature;
    private static TextView tvBatteryVoltage;
    private static TextView tvBatteryTechnology;
	
    /** 定义好字符串以备使用 */
	private static String batteryStatus="电池状态: ";
	private static String batteryLevel="电池电量: ";
	private static String batteryHealth="电池健康: ";
	private static String batteryTemperature="电池温度: ";
	private static String batteryVoltage="电池电压: ";
	private static String  batteryTechnology="电池技术: ";
	
    private static String  batteryStatusCharging="正在充电";
    private static String  batteryStatusDischarging="正在放电";
    private static String  batteryStatusFull="已充满";
    private static String  batteryStatusNotCharging="未在充电";
    private static String  batteryStatusUnknown="状态未知";
        
    private static String  batteryPluggedAC="(AC)";
    private static String  batteryPluggedUSB="(USB)";
        
    private static String  batteryHealthCold="过冷";
    private static String  batteryHealthDead="损坏";
    private static String  batteryHealthGood="良好";
    private static String  batteryHealthOverheat="过热";
    private static String  batteryHealthOverVoltage="过压";
    private static String  batteryHealthUnknown="未知";
    private static String  batteryHealthUnspecifiedFailure="未知的故障";
	
    /** 提示Service启动标志位 */
	private static boolean flag;
	
	/** 提示信息接收器 */
	BroadcastReceiver infoReceiver;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		//设置activity无标题
		setContentView(R.layout.newlayout);		//使用newlayout的布局
		
		tvBatteryStatus=(TextView)findViewById(R.id.tvBatteryStatus);
		tvBatteryLevel=(TextView)findViewById(R.id.tvBatteryLevel);
		tvBatteryHealth=(TextView)findViewById(R.id.tvBatteryHealth);
		tvBatteryTemperature=(TextView)findViewById(R.id.tvBatteryTemperature);
		tvBatteryVoltage=(TextView)findViewById(R.id.tvBatteryVoltage);
		tvBatteryTechnology=(TextView)findViewById(R.id.tvBatteryTechnology);
		
		flag=true;		//提示service的标志位置为true
		
		infoReceiver=new BroadcastReceiver()	//提示信息接收器的定义
		{
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				setText();		//收到intent，就及时修改TextView信息，使得Activity显示时，电池信息也能动态显示
			}
		};
		
		/** 注册提示信息的intentFilter */
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.ritterliu.newBatteryWidget");
		registerReceiver(infoReceiver,filter);

		/** 启动提示service */
		Intent startService=new Intent(this,updateService.class);
		startService(startService);
		
	}
	
	/** 点击屏幕任意位置，关闭电池信息Activity */
	public boolean onTouchEvent(MotionEvent event)
	{
		this.finish();
	//	onDestroy();
	//	System.exit(0);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		flag=false;
		unregisterReceiver(infoReceiver);
		super.onDestroy();
	}

	/** 及时动态修改Activity上文字信息的函数 */
	public static  void setText()
	{
		String plugState="";
		switch(currentBatteryPlugged)
		{
		case 0:
			plugState="";
			break;
		case 1:
			plugState=batteryPluggedAC;
			break;
		case 2:
			plugState=batteryPluggedUSB;
			break;
		default:
			plugState="";
		}

		switch(currentBatteryStatus)
		{
		case 1:
			tvBatteryStatus.setText(batteryStatus+batteryStatusUnknown);
			break;
		case 2:
			tvBatteryStatus.setText(batteryStatus+batteryStatusCharging+plugState);
			break;
		case 3:
			tvBatteryStatus.setText(batteryStatus+batteryStatusDischarging);
			break;
		case 4:
			tvBatteryStatus.setText(batteryStatus+batteryStatusNotCharging);
			break;
		case 5:
			tvBatteryStatus.setText(batteryStatus+batteryStatusFull+plugState);
			break;
		default:
			tvBatteryStatus.setText(batteryStatus+batteryStatusUnknown);
		}
		
		tvBatteryLevel.setText(batteryLevel+String.valueOf(currentBatteryLevel)+"%");
		
		switch(currentBatteryHealth)
		{
		case 1:
			tvBatteryHealth.setText(batteryHealth+batteryHealthUnknown);
			break;
		case 2:
			tvBatteryHealth.setText(batteryHealth+batteryHealthGood);
			break;
		case 3:
			tvBatteryHealth.setText(batteryHealth+batteryHealthOverheat);
			break;
		case 4:
			tvBatteryHealth.setText(batteryHealth+batteryHealthDead);
			break;
		case 5:
			tvBatteryHealth.setText(batteryHealth+batteryHealthOverVoltage);
			break;
		case 6:
			tvBatteryHealth.setText(batteryHealth+batteryHealthUnspecifiedFailure);
			break;
		case 7:
			tvBatteryHealth.setText(batteryHealth+batteryHealthCold);
			break;
		default:
			tvBatteryHealth.setText(batteryHealth+batteryHealthUnknown);
		}
		
		tvBatteryTemperature.setText(batteryTemperature+currentBatteryTemperature/10f+"℃");
		tvBatteryVoltage.setText(batteryVoltage+currentBatteryVoltage+"mv");
		tvBatteryTechnology.setText(batteryTechnology+currentBatteryTechnology);
	}
	
	/** 提示信息变化的service，约每隔一秒，就发送intent，
	 * 提醒activity更新电池信息，主要为了检测电池状态的变化，
	 * 例如连上充电时，状态会从“未在充电”变为“正在充电”
	 * 通过调用plugged方式，还能判断是AC方式充电还是USB方式充电
	 */
	public static class updateService extends Service{
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		/** 定义得到电池信息的BroadcastReceiver，提取出关键信息，存入变量中 */
		private BroadcastReceiver batteryReceiver=new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				currentBatteryStatus=intent.getIntExtra("status", 0);
				currentBatteryLevel=intent.getIntExtra("level", 0);
				currentBatteryHealth=intent.getIntExtra("health", 0);
				currentBatteryTemperature=intent.getIntExtra("temperature",0);
				currentBatteryVoltage=intent.getIntExtra("voltage",0);
				currentBatteryTechnology=intent.getStringExtra("technology");
				currentBatteryPlugged=intent.getIntExtra("plugged",0);
			}
		};
		
		
		public void onStart(Intent intent,int startId)
		{
			registerReceiver(batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册BroadcastReceiver
		
			/** 启动一个线程，约每隔一秒就发送intent提醒Activity更新电池信息 */
			new Thread()
			{
				public void run()
				{
					while(flag)
					{
			            Intent sendInfoToActivity=new Intent();
			            sendInfoToActivity.setAction("com.ritterliu.newBatteryWidget");
			            sendBroadcast(sendInfoToActivity);
		
						try
						{
							Thread.sleep(1000);
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			}.start();
			super.onStart(intent, startId);
		}
	}
}
