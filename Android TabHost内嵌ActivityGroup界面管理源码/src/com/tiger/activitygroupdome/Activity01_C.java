package com.tiger.activitygroupdome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 模块的第3个界面
 * @author HuYang
 *
 */
public class Activity01_C extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a01_c);
		final TabGroupActivity parentActivity1 = (TabGroupActivity) getParent();
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parentActivity1.goBack();
			}
		});
	}

}
