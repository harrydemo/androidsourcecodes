package com.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProgressActivity extends Activity {

	private Button button1;
	private TextView textView1;
	public ProgressDialog dialog = null;

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		button1 = (Button) findViewById(R.id.myButton1);
		textView1 = (TextView) findViewById(R.id.myTextView1);
		button1.setOnClickListener(myShowProgressBar);
	}

	Button.OnClickListener myShowProgressBar = new Button.OnClickListener() {
		
		public void onClick(View v) {
			
			final CharSequence dialogTitle = getString(R.string.str_dialog_title);
			final CharSequence dialogBody = getString(R.string.str_dialog_body);

			// ��ʾProgress�Ի���
			dialog = ProgressDialog.show(ProgressActivity.this, dialogTitle,
					dialogBody, true);

			textView1.setText(dialogBody);

			new Thread() {
				public void run() {
					try {
						/* ������д��Ҫ�������еĳ���Ƭ�� */
						/* Ϊ�����Կ���Ч��������ͣ3����Ϊʾ�� */
						sleep(3000);
					} 
					catch (Exception e) {
						e.printStackTrace();
					} 
					finally {
						// ж����������dialog����
						dialog.dismiss();
					}
				}
			}.start();
		}
	};
}