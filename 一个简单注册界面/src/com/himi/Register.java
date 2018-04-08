/**
 * 
 */
package com.himi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Himi
 * 
 */
public class Register extends Activity {
	private Button button_ok;
	private EditText et;
	private TextView tv;
	private LinearLayout ly;
	private Register rs;
	private byte count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rs = this;
		ly = new LinearLayout(this);
		button_ok = new Button(this);
		button_ok.setWidth(100);
		button_ok.setText("确定");
		button_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (count == 1) {
					MySurfaceView.str_zh = et.getText().toString();
				} else if (count == 2) {
					MySurfaceView.str_pass = et.getText().toString();
				}

				rs.finish();
			}
		});
		Intent intent = this.getIntent();
		count = (byte) intent.getIntExtra("count", 0);
		String temp_str = "";
		String temp_str2 = "";
		et = new EditText(this);
		tv = new TextView(this);
		if (count != 3) {
			temp_str = intent.getStringExtra("himi");
			if (count == 1) {
				rs.setTitle("请输入帐号!");
			} else {
				rs.setTitle("请输入密码!");
			}
			ly.addView(tv);
			ly.addView(et);
			ly.addView(button_ok);
			if (temp_str != null) {
				et.setText(temp_str);
			}
		} else {
			temp_str = intent.getStringExtra("himi_zh");
			temp_str2 = intent.getStringExtra("himi_pass");
			rs.setTitle("您输入的信息：");
			tv.setText("帐号:" + temp_str + "\n" + "密码" + temp_str2);
			ly.addView(tv);
			ly.addView(button_ok);
			if (temp_str != null) {
				et.setText(temp_str);
			}
		}

		setContentView(ly);
	}
}
