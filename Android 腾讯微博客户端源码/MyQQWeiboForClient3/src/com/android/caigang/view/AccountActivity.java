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
		
		setUpViews();//����view
		setUpListeners();//����listenter
		
		registerReceiver(broadcastReceiver, new IntentFilter("com.weibo.caigang.getverifier"));//ע���õ���֤��㲥������.
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());//��ȡ���ݿ������࣬���˵�������֤ȫ��ֻ��һ���˶���
		userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");//ȡ��΢��Ĭ�ϵ�¼�˺���Ϣ
		
		UserInfo user = null;
		
		if(userList!=null&&userList.size()>0){
			if (nick != "") {
				user = dataHelper.getUserByName(nick,userList);//ȡ��΢��Ĭ�ϵ�¼�˺���Ϣ
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
			weibo = WeiboContext.getInstance();//��������֤����Ӧ��ֻ��һ��weibo����
			weibo.getRequestToken();
			Intent intent = new Intent(AccountActivity.this,AuthorizeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", weibo.getAuthorizeUrl());
			intent.putExtras(bundle);
			startActivity(intent);//��ת����Ѷ��΢����Ȩҳ��,ʹ��webview����ʾ
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
				String veryfier = bundle.getString("veryfier");//��ȡ����Ȩҳ�淵�ص�veryfier
				if(veryfier!=null){
					//unregisterReceiver(broadcastReceiver);
					weibo.getAccessToken(weibo.getTokenKey(), weibo.getTokenSecrect(), veryfier);//ȡ��key��secret,���key��secret�ǳ���Ҫ������Ѷ��APIȫ�����ˣ��������˵ģ��������Ķ�һ���ģ����������е�������Ѷ��OAuth��֤�ǻ���1.0��
					String userInfo = weibo.getUserInfo(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect());
					try {
						JSONObject data = new JSONObject(userInfo).getJSONObject("data");
						String headUrl = null;
						if(data.getString("head")!=null&&!"".equals(data.getString("head"))){
							headUrl = data.getString("head")+"/100";
						}
						String userId = data.getString("name");
						String userName = data.getString("nick");
						
						UserInfo user = new UserInfo();//����һ��user���󱣴浽���ݿ�
						user.setUserId(userId);
						user.setUserName(userName);
						user.setToken(weibo.getAccessTokenKey());
						user.setTokenSecret(weibo.getAccessTokenSecrect());
						
						Long insertId = 0L;
						
						if (dataHelper.HaveUserInfo(userId)){//���ݿ��Ѿ������˴��û�
							dataHelper.UpdateUserInfo(userName, ImageUtil.getRoundBitmapFromUrl(headUrl, 15), userId);
							//Toast.makeText(AccountActivity.this, "���û��Ѵ���,������û�������ͷ���Ѿ��ı䣬��ô�˲�����ͬ������!", Toast.LENGTH_LONG).show();
						}else{
							if(headUrl!=null){
								insertId = dataHelper.SaveUserInfo(user,ImageUtil.getBytesFromUrl(headUrl));
							}else{
								insertId = dataHelper.SaveUserInfo(user,null);
							}
						}
						if(insertId>0L){
							//Toast.makeText(AccountActivity.this, "�Ѿ���Ȩ�ɹ�,����ת��ѡ��Ĭ�ϵĵ�¼�û�����!", Toast.LENGTH_LONG).show();
						}
						Log.d(TAG+"�������ݿ��id��", insertId.toString());
						
						userList = dataHelper.GetUserList(false);
						adapater = new UserAdapater();
						adapater.notifyDataSetChanged();//ˢ��listview
						listView.setAdapter(adapater);
						listView.setOnItemClickListener(AccountActivity.this);
						
						SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
						String nick = preferences.getString("user_default_nick", "");//ȡ��΢��Ĭ�ϵ�¼�˺���Ϣ
						
						UserInfo defauUserInfo = null;
						
						if(userList!=null&&userList.size()>0){
							if (nick != "") {
								defauUserInfo = dataHelper.getUserByName(nick,userList);//ȡ��΢��Ĭ�ϵ�¼�˺���Ϣ
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
		items= new CharSequence[]{"ɾ���˺�","ȡ��"};
		new AlertDialog.Builder(AccountActivity.this).setTitle("ѡ��").setItems(items,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: {
							String userId = userList.get(position).getUserId();
							dataHelper.DelUserInfo(userId);//ɾ�����ݿ��¼
							SharedPreferences preferences = getSharedPreferences("default_user", Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							if(preferences.getString("user_default_name", "").equals(userId)){
								editor.putString("user_default_nick", "");
								editor.putString("user_default_name", "");
								editor.commit();//������汣��ļ�¼SharedPreferences
							}
							userList = dataHelper.GetUserList(false);
							adapater = new UserAdapater();
							adapater.notifyDataSetChanged();//ˢ��listview
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
