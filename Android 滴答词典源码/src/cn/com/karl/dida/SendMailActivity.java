package cn.com.karl.dida;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMailActivity extends Activity {

	private EditText emailTitle;
	private EditText emailContent;
	private Button emailBack;
	private Button emailSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		
		emailTitle=(EditText) this.findViewById(R.id.emailTitle);
		emailContent=(EditText) this.findViewById(R.id.emailContent);
		emailBack=(Button) this.findViewById(R.id.emailBack);
		emailSend=(Button) this.findViewById(R.id.emailSend);
		emailBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		emailSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			   if("".equals(emailTitle.getText().toString().trim())){
				   Toast.makeText(getApplicationContext(), "标题不能为空", 1).show();
			   }else if("".equals(emailContent.getText().toString().trim())){
				   Toast.makeText(getApplicationContext(), "内容不能为空", 1).show();
			   }else{
				//发送邮件
				Intent mailIntent=new Intent(android.content.Intent.ACTION_SEND);

				mailIntent.setType("plain/test");
				String[] strEmailReciver=new String[]{ "wangkuifeng0118@gmail.com" };

				String strEmailSubject=emailTitle.getText().toString();

				String strEmailBody=emailContent.getText().toString();
				mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, strEmailReciver);


				mailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);

				mailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strEmailBody);

				startActivity(Intent.createChooser(mailIntent, "发送"));
				Toast.makeText(getApplicationContext(), "邮件发送成功", 1).show();
				finish();
			   }
			}
		});
	}
}
