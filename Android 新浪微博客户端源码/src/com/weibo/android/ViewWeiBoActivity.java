package com.weibo.android;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weibo.android.logic.AsyncBitmapLoader;
import com.weibo.android.logic.MainService;
import com.weibo.android.logic.Task;
import com.weibo.net.Weibo;

public class ViewWeiBoActivity extends Activity implements IWeiboActivity {
	Weibo weibo = Weibo.getInstance();
	String returnStatus;
	ImageView bmiddleImageView;
	TextView usernameTextView;
	ImageView usericonImageView;
	TextView contentTextView;
	Button zfButton;
	Button plButton;
	private ProgressDialog p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewweibo);
		bmiddleImageView = (ImageView) findViewById(R.id.pic);
		usernameTextView = (TextView) findViewById(R.id.user_name);
		usericonImageView = (ImageView) findViewById(R.id.user_icon);
		contentTextView = (TextView) findViewById(R.id.text);
		zfButton = (Button) findViewById(R.id.btn_zf);
		plButton = (Button) findViewById(R.id.btn_pl);
		Intent i = this.getIntent();
		String key = i.getStringExtra("key");
		view(key);
	}

	private void view(String id) {
		try {
			Log.v("ViewWeiBoActivity",id);
			HashMap hm = new HashMap();
			hm.put("weiboid", id);
			MainService.addActivity(ViewWeiBoActivity.this);
			Task ts = new Task(Task.VIEWWEIBO, hm,ViewWeiBoActivity.this);
			MainService.newTask(ts);// 添加发表微博的任务
			p = new ProgressDialog(this);
			p.setMessage("正在加载中，请稍后");
			p.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	// 更新UI
	@Override
	public void refresh(Object... params) {
		try {
			System.out.println("刷新UI详细页");
			returnStatus = (String) params[0];
			JSONObject jsonobject = new JSONObject(returnStatus);
			String username = jsonobject.optJSONObject("user").getString(
					"screen_name");
			String usericon = jsonobject.optJSONObject("user").getString(
					"profile_image_url");
			String time = jsonobject.getString("created_at");
			String text = jsonobject.getString("text");
			int reposts_count = jsonobject.getInt("reposts_count");
			int comments_count = jsonobject.getInt("comments_count");
			if (jsonobject.has("bmiddle_pic")) {
				new AsyncBitmapLoader().execute(bmiddleImageView, jsonobject
						.getString("bmiddle_pic"));
			}
			usernameTextView.setText(username);
			new AsyncBitmapLoader().execute(usericonImageView, usericon);
			contentTextView.setText(text);
			zfButton.setText("转发(" + reposts_count + ")");
			plButton.setText("评论(" + comments_count + ")");
			MainService.removeActivity(ViewWeiBoActivity.this);
			p.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
