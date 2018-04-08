package com.lqf.riji;

import com.lqf.gerenriji.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class JiNianRi extends Activity {
	private TextView textneirong;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jinianri);
		textneirong = (TextView) findViewById(R.id.textneirong);
		textneirong.setText("¼Í\nÄî\nÄÚ\nÈÝ");
	}

}
