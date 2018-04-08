package com.cn.daming;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmAgainSetting extends Activity{

	private DataBaseHelper dbHelper;
	private Cursor cursor;
	Intent getAlarmAgainSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getAlarmAgainSetting = AlarmAgainSetting.this.getIntent();
		String getAlarmAgainSettingStr = getAlarmAgainSetting.getExtras().getString("STR_RESULT");
		
		dbHelper = new DataBaseHelper(this);
		
		if(getAlarmAgainSettingStr.equals("alarm1")){
			Intent againIntent = new Intent(AlarmAgainSetting.this, CallAlarm.class);
			againIntent.putExtra("RESULT", "alarm1");
			PendingIntent sender=PendingIntent.getBroadcast(
					AlarmAgainSetting.this,0, againIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am;
			am = (AlarmManager)getSystemService(ALARM_SERVICE);
			int nowDay = Contants.getNowWeek();//�õ������ܼ�
			int setDay = 0;
			
			cursor = dbHelper.selectAlarmColock();
		//	Log.w("ss",cursor.getString(0));
			String str1 = null;
			int count1 = cursor.getCount();
			if(count1 > 0){
				for(int i=0;i<count1;i++){
	                  if(i == 0){
	                	 cursor.moveToPosition(i);
				          str1 = cursor.getString(2);
	                  }
				}
			}
			if(!(str1.equals("Ŀǰ������"))){
				String[] setStr = str1.split(",");//�ܼ�ѭ��
	    		int[] dayOfNum = Contants.getDayOfNum(setStr);//���1��2��3
	    		Log.v("xxxx",""+dayOfNum.length);
	    		setDay = Contants.getResultDifferDay1(dayOfNum, nowDay);//�õ���һ�����õ�ѭ���ܼ� nowDay ��һ��7
	    		int differDay = Contants.compareDayNowToNext(nowDay, setDay);//�õ����������ѭ�����ڵĲ������
	    		if(differDay == 0){ //���ǽ���
	    			am.set(AlarmManager.RTC_WAKEUP,Contants.getNowTimeMinuties() + Contants.getDifferMillis(7),sender);//7�����ѭ��
	    		}else{
	    			am.set(AlarmManager.RTC_WAKEUP,Contants.getNowTimeMinuties() + Contants.getDifferMillis(differDay),sender);  
	    		}
	    	}
		}
		
		if(getAlarmAgainSettingStr.equals("alarm2")){
			/* allAlarm.class */
			Intent againIntent = new Intent(AlarmAgainSetting.this, CallAlarm.class);
			againIntent.putExtra("RESULT", "alarm2");
			/* PendingIntent */
			PendingIntent sender=PendingIntent.getBroadcast(
					AlarmAgainSetting.this,1, againIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			/* AlarmManager.RTC_WAKEUP
			 * */
			AlarmManager am;
			am = (AlarmManager)getSystemService(ALARM_SERVICE);
			int nowDay = Contants.getNowWeek();
			int setDay = 0;
			
			cursor = dbHelper.selectAlarmColock();
			String str2 = null;
			int count2 = cursor.getCount();
		//	Log.w("ss2",cursor.getString(0));

			if(count2 > 0){
				for(int i=0;i<count2;i++){
	                  if(i == 1){
	                	  cursor.moveToPosition(i);
				          str2 = cursor.getString(2);

	                  }
				}
			}
			if(!(str2.equals("Ŀǰ������"))){
				String[] setStr = str2.split(",");
	    		int[] dayOfNum = Contants.getDayOfNum(setStr);
	    		setDay = Contants.getResultDifferDay1(dayOfNum, nowDay);
	    		int differDay = Contants.compareDayNowToNext(nowDay, setDay);
	    		if(differDay == 0){
	    		   am.set(AlarmManager.RTC_WAKEUP,Contants.getNowTimeMinuties() + Contants.getDifferMillis(7),sender);
	    		}else{
	    			am.set(AlarmManager.RTC_WAKEUP,Contants.getNowTimeMinuties() + Contants.getDifferMillis(differDay),sender);  
	    		}
	    	}
		}
		cursor.close();
		dbHelper.close();
		
		Toast.makeText(this, R.string.alarm_time_come, Toast.LENGTH_SHORT).show();
		cursor = dbHelper.selectAlarmColock();

		if(getAlarmAgainSettingStr.equals("alarm2")){
			 cursor.moveToPosition(1);

		}
		else{
			 cursor.moveToPosition(0);
		}
        String  str1 = cursor.getString(0);
		 new AlertDialog.Builder(this)
	     .setIcon(R.drawable.clock)
	     .setTitle("�ճ�����")
	     .setMessage(str1)
	     .setPositiveButton("ȷ��",
	      new DialogInterface.OnClickListener()
	     {
	       public void onClick(DialogInterface dialog, int whichButton)
	       {
	         /*AlarmAlertActivity */
	         finish();
	       }
	     }).setNegativeButton("ȡ��", 
	     		 new DialogInterface.OnClickListener()
	     {
	       public void onClick(DialogInterface dialog, int whichButton)
	       {
	         /*AlarmAlertActivity */
		       finish();
	       }
	     }).show();
	}

}
