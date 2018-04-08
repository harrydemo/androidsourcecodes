package com.tiger.activitygroupdome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 模块的第2个界面
 * @author HuYang
 *
 */
public class Activity01_B extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a01_b);
		Button jump = (Button) findViewById(R.id.jump);
		final TabGroupActivity parentActivity1 = (TabGroupActivity) getParent();

		jump.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parentActivity1.startChildActivity("Activity01_C", new Intent(
						Activity01_B.this, Activity01_C.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parentActivity1.goBack();

			}
		});
	}
}
