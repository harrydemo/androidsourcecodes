package com.weibo.android;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.android.logic.MainService;
import com.weibo.android.logic.Task;

public class NewWeiboActivity extends Activity implements IWeiboActivity {

	private Button btnSend;
	private Button btnReset;
	private Button head_new;
	private Button head_refresh;
	private TextView head_userName;
	private EditText new_weibo_edit;
	private ProgressDialog p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.newweibo);

		new_weibo_edit = (EditText) this.findViewById(R.id.new_weibo_edit);
		btnSend = (Button) this.findViewById(R.id.new_send);
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (new_weibo_edit.length() > 140) {
					Toast.makeText(NewWeiboActivity.this, "字数超过140，无法发送", 1)
							.show();
					return;
				}else if(new_weibo_edit.length()<=0){
					Toast.makeText(NewWeiboActivity.this, "发送内容不能为空，请重新发送", 1)
					.show();
			return;
				}
				HashMap hm = new HashMap();
				hm.put("msg", new_weibo_edit.getText().toString());
				MainService.addActivity(NewWeiboActivity.this);
				Task ts = new Task(Task.NEWWEIBO, hm,NewWeiboActivity.this);
				MainService.newTask(ts);// 添加发表微博的任务
				p = ProgressDialog.show(NewWeiboActivity.this, "", "正在发表微博，请稍候  . . .  ", true);
			}
		});
		btnReset = (Button) this.findViewById(R.id.new_reset);
		btnReset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new_weibo_edit.setText("");
			}
		});
		head_new = (Button) this.findViewById(R.id.head_new);
		// head_new.setBackgroundResource(R.drawable.title_back_normal);
		head_new.setBackgroundResource(R.drawable.new_weibo_btn_back);
		head_new.setText("返回");
		head_new.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		head_refresh = (Button) this.findViewById(R.id.head_refresh);
		head_refresh.setVisibility(View.GONE);

		head_userName = (TextView) this.findViewById(R.id.head_userName);
		head_userName.setText("发表新微薄");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		String success = (String) params[0];
		p.dismiss();
		Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
		finish();
	}

}
