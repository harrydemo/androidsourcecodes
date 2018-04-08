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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//��ȥ���⣨Ӧ�õ����ֱ���Ҫд��setContentView֮ǰ����������쳣��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main); //Ҫ����ʾȻ���ٶ������ȡ�����������
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		tv=(TextView)findViewById(R.id.textview);
		
	}
 
	public void onClick(View v) {
		if (v == button1) {
			MySurfaceView.button_str = "button 1������";
			tv.setText("button 1������");
		} else if (v == button2) {
			MySurfaceView.button_str = "button 2������";
			tv.setText("button 2������");
		}
	}
}