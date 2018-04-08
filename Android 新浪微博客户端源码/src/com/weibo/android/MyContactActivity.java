package com.weibo.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weibo.android.logic.AsyncBitmapLoader;
import com.weibo.android.logic.MainService;
import com.weibo.android.logic.Task;

public class MyContactActivity extends Activity implements IWeiboActivity {

	private ImageView mycontact_iamgeIcon;
	private TextView mycontact_textName;
	private Button mycontact_editBtn;
	private TextView mycontact_address;
	private TextView mycontact_loginName;
	private TextView mycontact_item_attention;
	private TextView mycontact_item_weibo;
	private TextView mycontact_item_interest;
	private TextView mycontact_item_topic;
	private TextView head_userName;
	private ProgressDialog p;
	private Button head_new;
	private Button head_refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycontact);

		head_new = (Button) this.findViewById(R.id.head_new);
		head_new.setVisibility(View.GONE);

		head_refresh = (Button) this.findViewById(R.id.head_refresh);
		head_refresh.setVisibility(View.GONE);

		head_userName = (TextView) this.findViewById(R.id.head_userName);
		head_userName.setText("个人资料");

		MainService.addActivity(this);
		MainService.newTask(new Task(Task.MYCONTACT, null, this));
		p = ProgressDialog.show(this, "", "正在加载，请稍候  . . .  ");

		mycontact_textName = (TextView) findViewById(R.id.mycontact_textName);
		mycontact_iamgeIcon = (ImageView) this
				.findViewById(R.id.mycontact_iamgeIcon);
		mycontact_editBtn = (Button) this.findViewById(R.id.mycontact_editBtn);
		mycontact_address = (TextView) this
				.findViewById(R.id.mycontact_address);
		mycontact_loginName = (TextView) this
				.findViewById(R.id.mycontact_loginName);
		mycontact_item_attention = (TextView) this
				.findViewById(R.id.mycontact_item_attention);
		mycontact_item_weibo = (TextView) this
				.findViewById(R.id.mycontact_item_weibo);
		mycontact_item_interest = (TextView) this
				.findViewById(R.id.mycontact_item_interest);
		mycontact_item_topic = (TextView) this
				.findViewById(R.id.mycontact_item_topic);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MainService.removeActivity(this);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("你确定退出吗？").setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									MyContactActivity.this.finish();
									// 停止服务
									Intent it = new Intent(
											MyContactActivity.this,
											MainService.class);
									MyContactActivity.this.stopService(it);
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									android.os.Process
											.killProcess(android.os.Process
													.myTid());
									android.os.Process
											.killProcess(android.os.Process
													.myUid());
								}
							}).setNegativeButton("返回",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		try {
			String userJson = (String) params[0];
			JSONObject user = new JSONObject(userJson);

			mycontact_textName.setText("微博昵称:" + user.getString("screen_name"));
			// 异步加载图片
			new AsyncBitmapLoader().execute(mycontact_iamgeIcon, user
					.getString("profile_image_url"));
			// mycontact_iamgeIcon.setBackgroundDrawable(NetUtil.getNetImage(user.getProfileImageURL()));
			mycontact_address.setText(user.getString("location"));
			mycontact_loginName.setText(user.getString("description"));
			mycontact_item_attention.setText(String.valueOf(user
					.getInt("friends_count")));
			mycontact_item_weibo.setText(String.valueOf(user
					.getInt("statuses_count")));
			mycontact_item_interest.setText(String.valueOf(user
					.getInt("followers_count")));
			mycontact_item_topic.setText(String.valueOf(user
					.getInt("favourites_count")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		p.dismiss();
	}

}
