package com.lqf.riji;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.lqf.gerenriji.R;
import com.lqf.riji.duanxin.Tab1;
import com.lqf.riji.duanxin.Tab2;
import com.lqf.riji.duanxin.Tab3;
import com.lqf.riji.duanxin.Tab4;

public class DuanXin extends ActivityGroup {
	// ��������Ŀؼ�
	private TabHost tabHost;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duanxin);
		tabHost = (TabHost) findViewById(R.id.tabhost);
		// ��ȡѡ�����
		tabHost.setup(this.getLocalActivityManager());

		// ����ѡ�����
		TabSpec tab1 = tabHost.newTabSpec("TAB1");
		// ѡ�����
		tab1.setIndicator("�ճ�");
		// ��ת
		tab1.setContent(new Intent(this, Tab1.class));
		// ����
		tabHost.addTab(tab1);

		// ����ѡ�����
		TabSpec tab2 = tabHost.newTabSpec("TAB2");
		// ѡ�����
		tab2.setIndicator("����");
		// ��ת
		tab2.setContent(new Intent(this, Tab2.class));
		// ����
		tabHost.addTab(tab2);

		// ����ѡ�����
		TabSpec tab3 = tabHost.newTabSpec("TAB3");
		// ѡ�����
		tab3.setIndicator("�ش�");
		// ��ת
		tab3.setContent(new Intent(this, Tab3.class));
		// ����
		tabHost.addTab(tab3);

		// ����ѡ�����
		TabSpec tab4 = tabHost.newTabSpec("TAB4");
		// ѡ�����
		tab4.setIndicator("����");
		// ��ת
		tab4.setContent(new Intent(this, Tab4.class));
		// ����
		tabHost.addTab(tab4);
		// ��һ����ʾ��Tab����
		tabHost.setCurrentTab(0);

	}
}
