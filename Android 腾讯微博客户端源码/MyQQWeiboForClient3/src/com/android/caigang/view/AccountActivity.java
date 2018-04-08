package com.android.caigang.view;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.DataBaseContext;
import com.android.caigang.util.ImageUtil;
import com.android.caigang.util.WeiboContext;
import com.mime.qweibo.examples.MyWeiboSync;

public class AccountActivity extends ListActivity implements OnItemClickListener,OnItemLongClickListener,OnClickListener{
	
	private final static String TAG="AccountActivity";
	private DataHelper dataHelper;
	private MyWeiboSync weibo;
	private List<UserInfo> userList;
	private ListView listView;
	private ImageView user_default_headicon;
	private LinearLayout account_add_btn_bar;
	private UserInfo currentUser;
	private UserAdapater adapater;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		
		setUpViews();//设置view
		setUpListeners();//设置listenter
		
		registerReceiver(broadcastReceiver, new IntentFilter("com.weibo.caigang.getverifier"));//注册拿到验证码广播接收器.
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());//获取数据库连接类，用了单例，保证全局只有一个此对象。
		userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");//取得微博默认登录账号信息
		
		UserInfo user = null;
		
		if(userList!=null&&userList.size()>0){
			if (nick != "") {
				user = dataHelper.getUserByName(nick,userList);//取得微博默认登录账号信息
			}
			if(user == null) {
				user = userList.get(0);
			}
		}
		if(user!=null){
			user_default_headicon.setImageDrawable(user.getUserIcon());
		}
		
		if(userList!=null&&userList.size()>0){
			adapater = new UserAdapater();
			listView.setAdapter(adapater);
			listView.setOnItemClickListener(this);
		}
	}
	
	private void setUpViews(){
		listView = getListView();
		user_default_headicon = (ImageView)findViewById(R.id.user_default_headicon);
		account_add_btn_bar = (LinearLayout)findViewById(R.id.account_add_btn_bar);
	}
	
	private void setUpListeners(){
		user_default_headicon.setOnClickListener(this);
		account_add_btn_bar.setOnClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	
	
	
	public class UserAdapater extends BaseAdapter{
        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.account_list_item, null);
			ImageView user_headicon = (ImageView) convertView.findViewById(R.id.user_headicon);
			TextView user_nick = (TextView) convertView.findViewById(R.id.user_nick);
			TextView user_name = (TextView) convertView.findViewById(R.id.user_name);
			UserInfo user = userList.get(position);
			try {
				user_headicon.setImageDrawable(user.getUserIcon());
				user_nick.setText(user.getUserName());
				user_name.setText("@"+user.getUserId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		currentUser = userList.get(position);
		user_default_headicon.setImageDrawable(currentUser.getUserIcon());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_add_btn_bar: {
			weibo = WeiboContext.getInstance();//单例，保证整个应用只有一个weibo对象
			weibo.getRequestToken();
			Intent intent = new Intent(AccountActivity.this,AuthorizeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", weibo.getAuthorizeUrl());
			intent.putExtras(bundle);
			startActivity(intent);//跳转到腾讯的微博授权页面,使用webview来显示
		}
			break;
		case R.id.user_default_headicon: {
			SharedPreferences preferences = getSharedPreferences("default_user", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("user_default_nick", currentUser.getUserName());
			editor.putString("user_default_name", currentUser.getUserId());
			editor.commit();
			Intent intent = new Intent(AccountActivity.this, MainActivity.class);
			startActivity(intent);
		}
			break;

		default:
			break;
		}
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("com.weibo.caigang.getverifier")){
				weibo = WeiboContext.getInstance();
				Bundle bundle = intent.getExtras();
				String veryfier = bundle.getString("veryfier");//获取从授权页面返回的veryfier
				if(veryfier!=null){
					//unregisterReceiver(broadcastReceiver);
					weibo.getAccessToken(weibo.getTokenKey(), weibo.getTokenSecrect(), veryfier);//取得key和secret,这个key和secret非常重要，调腾讯的API全靠它了，神马新浪的，人人网的都一样的，不过还是有点区别，腾讯的OAuth认证是基于1.0的
					String userInfo = weibo.getUserInfo(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect());
					try {
						JSONObject data = new JSONObject(userInfo).getJSONObject("data");
						String headUrl = null;
						if(data.getString("head")!=null&&!"".equals(data.getString("head"))){
							headUrl = data.getString("head")+"/100";
						}
						String userId = data.getString("name");
						String userName = data.getString("nick");
						
						UserInfo user = new UserInfo();//生成一个user对象保存到数据库
						user.setUserId(userId);
						user.setUserName(userName);
						user.setToken(weibo.getAccessTokenKey());
						user.setTokenSecret(weibo.getAccessTokenSecrect());
						
						Long insertId = 0L;
						
						if (dataHelper.HaveUserInfo(userId)){//数据库已经存在了次用户
							dataHelper.UpdateUserInfo(userName, ImageUtil.getRoundBitmapFromUrl(headUrl, 15), userId);
							//Toast.makeText(AccountActivity.this, "此用户已存在,如果你用户名或者头像已经改变，那么此操作将同步更新!", Toast.LENGTH_LONG).show();
						}else{
							if(headUrl!=null){
								insertId = dataHelper.SaveUserInfo(user,ImageUtil.getBytesFromUrl(headUrl));
							}else{
								insertId = dataHelper.SaveUserInfo(user,null);
							}
						}
						if(insertId>0L){
							//Toast.makeText(AccountActivity.this, "已经授权成功,将跳转到选择默认的登录用户界面!", Toast.LENGTH_LONG).show();
						}
						Log.d(TAG+"插入数据库的id是", insertId.toString());
						
						userList = dataHelper.GetUserList(false);
						adapater = new UserAdapater();
						adapater.notifyDataSetChanged();//刷新listview
						listView.setAdapter(adapater);
						listView.setOnItemClickListener(AccountActivity.this);
						
						SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
						String nick = preferences.getString("user_default_nick", "");//取得微博默认登录账号信息
						
						UserInfo defauUserInfo = null;
						
						if(userList!=null&&userList.size()>0){
							if (nick != "") {
								defauUserInfo = dataHelper.getUserByName(nick,userList);//取得微博默认登录账号信息
							}
							if(defauUserInfo == null) {
								defauUserInfo = userList.get(0);
							}
						}
						if(defauUserInfo!=null){
							currentUser = defauUserInfo;
							user_default_headicon.setImageDrawable(defauUserInfo.getUserIcon());
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.e(TAG, userInfo);
				}
				Log.e(TAG, veryfier);
			}
		}
	};


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position,
			long arg3) {
		CharSequence [] items = null;
		items= new CharSequence[]{"删除账号","取消"};
		new AlertDialog.Builder(AccountActivity.this).setTitle("选项").setItems(items,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: {
							String userId = userList.get(position).getUserId();
							dataHelper.DelUserInfo(userId);//删除数据库记录
							SharedPreferences preferences = getSharedPreferences("default_user", Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							if(preferences.getString("user_default_name", "").equals(userId)){
								editor.putString("user_default_nick", "");
								editor.putString("user_default_name", "");
								editor.commit();//清除里面保存的记录SharedPreferences
							}
							userList = dataHelper.GetUserList(false);
							adapater = new UserAdapater();
							adapater.notifyDataSetChanged();//刷新listview
							listView.setAdapter(adapater);
						}
							break;
						case 1: {
						}
							break;
						default:
							break;
						}
			}
		}).show();
		return false;
	}
}
