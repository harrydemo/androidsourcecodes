package cn.com.karl.dida;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestActivity extends Activity {
	private EditText userName;
	private EditText password;
	private ImageButton registerBtn;
	private ImageButton loginBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);

		userName = (EditText) this.findViewById(R.id.userName);
		password = (EditText) this.findViewById(R.id.passWord);
		registerBtn = (ImageButton) this.findViewById(R.id.registerBtn01);
		loginBtn = (ImageButton) this.findViewById(R.id.loginBtn);
		
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName.setText("");
				password.setText("");
				Intent intent = new Intent(TestActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);

			}
		});
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (userName == null
						|| userName.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), "用户名不能为空", 1)
							.show();
				} else {
					if (password == null
							|| password.getText().toString().trim().equals("")) {
						Toast.makeText(getApplicationContext(), "密码不能为空", 1)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "对不起此版块暂未开通", 1)
								.show();
						userName.setText("");
						password.setText("");
						
					}
				}
			}
		});

		     
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					TestActivity.this);
			builder.setIcon(R.drawable.bee);
			builder.setTitle("你确定退出吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							TestActivity.this.finish();
							android.os.Process.killProcess(android.os.Process
									.myPid());
							android.os.Process.killProcess(android.os.Process
									.myTid());
							android.os.Process.killProcess(android.os.Process
									.myUid());
						}
					});
			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			builder.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
