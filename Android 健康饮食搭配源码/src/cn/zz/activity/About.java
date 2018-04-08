package cn.zz.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class About extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
	}
}
