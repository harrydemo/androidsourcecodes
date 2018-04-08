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
	/** ��������Ϣ����  */
	private static int currentBatteryPlugged=0;
	private static int currentBatteryStatus=0;
	private static int currentBatteryLevel=0;
	private static int currentBatteryHealth=0;
	private static int currentBatteryTemperature=0;
	private static int currentBatteryVoltage=0;
	private static String currentBatteryTechnology="";

	
	/** TextView ���� */
    private static TextView tvBatteryStatus;
    private static TextView tvBatteryLevel;
    private static TextView tvBatteryHealth;
    private static TextView tvBatteryTemperature;
    private static TextView tvBatteryVoltage;
    private static TextView tvBatteryTechnology;
	
    /** ������ַ����Ա�ʹ�� */
	private static String batteryStatus="���״̬: ";
	private static String batteryLevel="��ص���: ";
	private static String batteryHealth="��ؽ���: ";
	private static String batteryTemperature="����¶�: ";
	private static String batteryVoltage="��ص�ѹ: ";
	private static String  batteryTechnology="��ؼ���: ";
	
    private static String  batteryStatusCharging="���ڳ��";
    private static String  batteryStatusDischarging="���ڷŵ�";
    private static String  batteryStatusFull="�ѳ���";
    private static String  batteryStatusNotCharging="δ�ڳ��";
    private static String  batteryStatusUnknown="״̬δ֪";
        
    private static String  batteryPluggedAC="(AC)";
    private static String  batteryPluggedUSB="(USB)";
        
    private static String  batteryHealthCold="����";
    private static String  batteryHealthDead="��";
    private static String  batteryHealthGood="����";
    private static String  batteryHealthOverheat="����";
    private static String  batteryHealthOverVoltage="��ѹ";
    private static String  batteryHealthUnknown="δ֪";
    private static String  batteryHealthUnspecifiedFailure="δ֪�Ĺ���";
	
    /** ��ʾService������־λ */
	private static boolean flag;
	
	/** ��ʾ��Ϣ������ */
	BroadcastReceiver infoReceiver;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		//����activity�ޱ���
		setContentView(R.layout.newlayout);		//ʹ��newlayout�Ĳ���
		
		tvBatteryStatus=(TextView)findViewById(R.id.tvBatteryStatus);
		tvBatteryLevel=(TextView)findViewById(R.id.tvBatteryLevel);
		tvBatteryHealth=(TextView)findViewById(R.id.tvBatteryHealth);
		tvBatteryTemperature=(TextView)findViewById(R.id.tvBatteryTemperature);
		tvBatteryVoltage=(TextView)findViewById(R.id.tvBatteryVoltage);
		tvBatteryTechnology=(TextView)findViewById(R.id.tvBatteryTechnology);
		
		flag=true;		//��ʾservice�ı�־λ��Ϊtrue
		
		infoReceiver=new BroadcastReceiver()	//��ʾ��Ϣ�������Ķ���
		{
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				setText();		//�յ�intent���ͼ�ʱ�޸�TextView��Ϣ��ʹ��Activity��ʾʱ�������ϢҲ�ܶ�̬��ʾ
			}
		};
		
		/** ע����ʾ��Ϣ��intentFilter */
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.ritterliu.newBatteryWidget");
		registerReceiver(infoReceiver,filter);

		/** ������ʾservice */
		Intent startService=new Intent(this,updateService.class);
		startService(startService);
		
	}
	
	/** �����Ļ����λ�ã��رյ����ϢActivity */
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

	/** ��ʱ��̬�޸�Activity��������Ϣ�ĺ��� */
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
		
		tvBatteryTemperature.setText(batteryTemperature+currentBatteryTemperature/10f+"��");
		tvBatteryVoltage.setText(batteryVoltage+currentBatteryVoltage+"mv");
		tvBatteryTechnology.setText(batteryTechnology+currentBatteryTechnology);
	}
	
	/** ��ʾ��Ϣ�仯��service��Լÿ��һ�룬�ͷ���intent��
	 * ����activity���µ����Ϣ����ҪΪ�˼����״̬�ı仯��
	 * �������ϳ��ʱ��״̬��ӡ�δ�ڳ�硱��Ϊ�����ڳ�硱
	 * ͨ������plugged��ʽ�������ж���AC��ʽ��绹��USB��ʽ���
	 */
	public static class updateService extends Service{
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		/** ����õ������Ϣ��BroadcastReceiver����ȡ���ؼ���Ϣ����������� */
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
			registerReceiver(batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//ע��BroadcastReceiver
		
			/** ����һ���̣߳�Լÿ��һ��ͷ���intent����Activity���µ����Ϣ */
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
