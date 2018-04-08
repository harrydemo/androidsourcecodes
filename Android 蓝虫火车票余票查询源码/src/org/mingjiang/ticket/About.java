package org.mingjiang.ticket;

import android.app.Activity;
import android.os.Bundle;

public class About extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);
		this.setTitle("关于蓝虫火车票助手");
	}
}
