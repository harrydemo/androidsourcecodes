package com.tiger.activitygroupdome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * ģ��ĵ�1������
 * @author HuYang
 *
 */
public class Activity01_A extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a01_a);
		Button button = (Button) findViewById(R.id.jump);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �õ��������,���һ������.!
				TabGroupActivity parentActivity1 = (TabGroupActivity) getParent();
				parentActivity1.startChildActivity("Activity01_B", new Intent(
						Activity01_A.this, Activity01_B.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

}
