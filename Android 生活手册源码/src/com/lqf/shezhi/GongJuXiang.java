package com.lqf.shezhi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.lqf.gerenriji.R;

public class GongJuXiang extends Activity {
	//����ؼ�
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// ���ò��ֱ���
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gongjuxiang);
		// ��ȡ�Զ�����Ⲽ��
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.gongjubiaoti);
		//��ȡ�ؼ�
	
	}

}
