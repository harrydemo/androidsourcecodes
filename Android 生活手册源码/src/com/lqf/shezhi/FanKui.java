package com.lqf.shezhi;

import com.lqf.gerenriji.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FanKui extends Activity {
	//����ؼ�
	private Button tijiao;
	private EditText yijian;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fankui);
		//��ȡ�ؼ�
		yijian = (EditText) findViewById(R.id.fankuiyijian);
		tijiao = (Button) findViewById(R.id.tijiao);
		tijiao.getBackground().setAlpha(90);
	}

}
