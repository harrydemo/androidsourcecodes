package com.cellcom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RelativeAndLinearActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//�������Բ���LinearLayout����
		LinearLayout layoutMain=new LinearLayout(this);
		//����ˮƽ����
		layoutMain.setOrientation(LinearLayout.HORIZONTAL);
		setContentView(layoutMain);
		
		/**
		 * LayoutInflater������������ findViewById(),��ͬ����LayoutInflater��������layout��xml�����ļ�������ʵ��������findViewById()���Ҿ���xml�µľ��� widget�ؼ�(��:Button,TextView��)�� 
		 */
		LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout layoutLeft=(RelativeLayout)inflater.inflate(R.layout.relative_linear_left, null);
		RelativeLayout layoutRight=(RelativeLayout)inflater.inflate(R.layout.relative_linear_right, null);
		
		RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutMain.addView(layoutLeft, 100, 100);
		layoutMain.addView(layoutRight,layoutParams);
	}

}
