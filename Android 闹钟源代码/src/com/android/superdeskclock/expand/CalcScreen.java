/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.superdeskclock.expand;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.superdeskclock.Alarm;
import com.android.superdeskclock.AlarmAlert;
import com.android.superdeskclock.AlarmAlertFullScreen;
import com.android.superdeskclock.AlarmKlaxon;
import com.android.superdeskclock.Alarms;
import com.android.superdeskclock.Log;
import com.android.superdeskclock.R;
/**
 * Alarm Clock alarm alert: pops visible indicator and plays alarm
 * tone. This activity is the full screen version which shows over the lock
 * screen with the wallpaper as the background.
 */
public class CalcScreen extends Activity {

    // These defaults must match the values in res/xml/settings.xml
    protected static final String SCREEN_OFF = "screen_off";

    protected Alarm mAlarm;
    private Vibrator vibrator;  
	private AlarmKlaxon alarmKlaxonService;
	
	// Button 0~9
	private Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
	// Button ZF
	private Button btnZF,btnEqual;
	// EditText result
	private EditText resultTxt;
	private TextView expression,title;

	
	private boolean status=false,vibratorStatus=true,musicStatus=true,isOperate = false;
	private int times=1,result=0,total=10;
	
	//新增
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			alarmKlaxonService = ((AlarmKlaxon.LocalBinder)service).getService();			
		}
		public void onServiceDisconnected(ComponentName name) {
			alarmKlaxonService = null;
		}		
	};
	
	
	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.calc);
        
        //新增
        Intent i=new Intent(this, AlarmKlaxon.class);
        bindService(i, mConnection,Context.BIND_AUTO_CREATE);
        
        //振动器
        vibrator=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        
        final byte[] data = CalcScreen.this.getIntent().getByteArrayExtra(Alarms.ALARM_RAW_DATA);
        if (data != null) {
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            mAlarm = Alarm.CREATOR.createFromParcel(in);
        }

        if (mAlarm == null) {
            Log.v("AlarmReceiver failed to parse the alarm from the intent");
            return;
        }
        
        total=mAlarm.times;
        musicStatus=!mAlarm.silent;
        vibratorStatus=mAlarm.vibrate;
        
        if(vibratorStatus){
        	vibrator.vibrate( new long[]{1000,50,1000,50,1000},0);
        	Log.v("vibrator ok ......");
        }        
        
        
        Log.v("total:\t"+total);
        Log.v("interval:\t"+mAlarm.interval);
        Log.v("musicStatus:\t"+musicStatus);
        Log.v("vibratorStatus:\t"+vibratorStatus);
        
        if(total==0)
        	startNew();
        else{
        	createView();
    		setListeners();
        }
    }
	
	public boolean onKeyDown(int keyCode,KeyEvent keyEvent){
		switch (keyCode){
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_HOME:
			case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_DEL:
			case KeyEvent.KEYCODE_CAMERA:
			case KeyEvent.KEYCODE_CALL:
			case KeyEvent.KEYCODE_MUTE:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
			case KeyEvent.KEYCODE_POWER:
			case KeyEvent.KEYCODE_SOFT_RIGHT:
				return true;
			default:
				return false;
		}
	}


	// 设置监听器
	private void setListeners() {
		// 数字0～9监听
		btn0.setOnClickListener(showNumListener);
		btn1.setOnClickListener(showNumListener);
		btn2.setOnClickListener(showNumListener);
		btn3.setOnClickListener(showNumListener);
		btn4.setOnClickListener(showNumListener);
		btn5.setOnClickListener(showNumListener);
		btn6.setOnClickListener(showNumListener);
		btn7.setOnClickListener(showNumListener);
		btn8.setOnClickListener(showNumListener);
		btn9.setOnClickListener(showNumListener);

		// 正负号简体你
		btnZF.setOnClickListener(changeZFListener);
		//		btnP.setOnClickListener(addPointListener);

		// 等号监听
		btnEqual.setOnClickListener(doCalcListener);
	}

	// 创建控件
	private void createView() {
		// 数字0～9
		btn0 = (Button)findViewById(R.id.btn0);
		btn1 = (Button)findViewById(R.id.btn1);
		btn2 = (Button)findViewById(R.id.btn2);
		btn3 = (Button)findViewById(R.id.btn3);
		btn4 = (Button)findViewById(R.id.btn4);
		btn5 = (Button)findViewById(R.id.btn5);
		btn6 = (Button)findViewById(R.id.btn6);
		btn7 = (Button)findViewById(R.id.btn7);
		btn8 = (Button)findViewById(R.id.btn8);
		btn9 = (Button)findViewById(R.id.btn9);

		// 正负号、小数点
		btnZF = (Button)findViewById(R.id.btnZF);
		//		btnP = (Button)findViewById(R.id.btnP);

		// 输入显示栏
		resultTxt = (EditText)findViewById(R.id.result);

		expression = (TextView) findViewById(R.id.expression);
		Compute c=new Compute();
		Object[] objs=c.getFormula();
		expression.setText((String)objs[0]+"=");
		result=(Integer)objs[1];

		// 等号
		btnEqual = (Button)findViewById(R.id.btnEqual);
		
		title =(TextView) findViewById(R.id.title);
		title.setText("完成"+this.total+"个计算解除闹钟");
		resultTxt=(EditText) findViewById(R.id.result);
	}



	// 输入栏的内容显示
	private OnClickListener showNumListener = new OnClickListener() {
		public void onClick(View v) {
			if (isOperate) {
				resultTxt.setText("");
				isOperate = false;
			}

			String tmp = resultTxt.getText().toString().trim();
			switch (v.getId()) {
				case R.id.btn0:
					tmp += "0";
					break;
				case R.id.btn1:
					tmp += "1";
					break;
				case R.id.btn2:
					tmp += "2";
					break;
				case R.id.btn3:
					tmp += "3";
					break;
				case R.id.btn4:
					tmp += "4";
					break;
				case R.id.btn5:
					tmp += "5";
					break;
				case R.id.btn6:
					tmp += "6";
					break;
				case R.id.btn7:
					tmp += "7";
					break;
				case R.id.btn8:
					tmp += "8";
					break;
				case R.id.btn9:
					tmp += "9";
					break;
			}
			resultTxt.setText(tmp);
			//设置光标位置
			Editable etext = resultTxt.getText();
			Selection.setSelection(etext, tmp.length());
		}
	};

	// 正负数转换事件
	private OnClickListener changeZFListener = new OnClickListener() {
		public void onClick(View v) {
			String tmp = resultTxt.getText().toString().trim();
			if(tmp.indexOf("-")>=0){
				tmp = tmp.substring(1);
			}else{
				tmp = "-" + tmp;
			}
			resultTxt.setText(tmp);
			//设置光标位置
			Editable etext = resultTxt.getText();
			Selection.setSelection(etext, tmp.length());
		}
	};	

	// 运算操作	
	private OnClickListener doCalcListener = new OnClickListener() {
		public void onClick(View v){
			
			String str=resultTxt.getText().toString();
			resultTxt.setText("");
			Log.v(expression.getText().toString()+str);
			int temp=-100;
			try{
				temp = Integer.parseInt((str==null||"".equals(str))?"0":str);
			} catch (NumberFormatException e1){
				times=0;
				expression =(TextView) findViewById(R.id.expression);
				Compute c=new Compute();
				Object[] objs=c.getFormula();
				expression.setText((String)objs[0]+"=");
				result=(Integer)objs[1];
				Log.v("you are wrong....");
				title.setText("计算错误，重新计数");
				return ;
			}

			if(result==temp){
				if(times<total){
					expression =(TextView) findViewById(R.id.expression);
					Compute c=new Compute();
					Object[] objs=c.getFormula();
					expression.setText((String)objs[0]+"=");
					result=(Integer)objs[1];

					title.setText("剩余"+(total-times)+"个计算解除闹钟");
				}else{
					title.setText("闹钟解除");
					startNew();
					return;
				}
				times++;
			}else{
				times=1;
				expression =(TextView) findViewById(R.id.expression);
				Compute c=new Compute();
				Object[] objs=c.getFormula();
				expression.setText((String)objs[0]+"=");
				result=(Integer)objs[1];
				title.setText("计算错误，重新计数");
				Log.v("you are wrong....");
			}
		}
	};
	
	private void startNew(){
		Class c = AlarmAlert.class;
        KeyguardManager km = (KeyguardManager) CalcScreen.this.getSystemService(
                Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            // Use the full screen activity for security.
            c = AlarmAlertFullScreen.class;
        }
        Intent alarmAlert = new Intent(CalcScreen.this, c);
        alarmAlert.putExtra(Alarms.ALARM_INTENT_EXTRA, CalcScreen.this.mAlarm);
        alarmAlert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        CalcScreen.this.startActivity(alarmAlert);
        CalcScreen.this.unbindService(mConnection);
        CalcScreen.this.finish();
	}
}
