package com.tiger.activitygroupdome;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * �������������ģ��Ľ��� .
 * ÿ��ģ��,����Ҫ��������һ������Ľ���,�����ģ��Ͳ�������,!
 * @author HuYang
 * 
 */
public class GotoMianActicity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ģ��ĵ�һ��������,��ת���Ǹ�����,�����һ��������Ҫд�Ǹ�����,
		// �����ȥ���˷���,������������ӵļ���.
		startChildActivity("Activity01_A", new Intent(GotoMianActicity.this,
				Activity01_A.class));
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}
}
