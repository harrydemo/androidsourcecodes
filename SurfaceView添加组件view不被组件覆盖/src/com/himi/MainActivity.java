package com.himi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private Button button1, button2;
	private TextView tv ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隐去标题（应用的名字必须要写在setContentView之前，否则会有异常）
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main); //要先显示然后再对其组件取出、处理操作
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		tv=(TextView)findViewById(R.id.textview);
		
	}
 
	public void onClick(View v) {
		if (v == button1) {
			MySurfaceView.button_str = "button 1被触发";
			tv.setText("button 1被触发");
		} else if (v == button2) {
			MySurfaceView.button_str = "button 2被触发";
			tv.setText("button 2被触发");
		}
	}
}