package com.android.caigang.view;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.AsyncImageLoader;
import com.android.caigang.util.TimeUtil;
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.mime.qweibo.examples.MyWeiboSync;

public class FansActivity extends ListActivity implements OnItemClickListener{

	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private Handler handler;
	private AsyncImageLoader asyncImageLoader; 
	private FansThread thread;
	private ListView listView;
	private ProgressDialog progressDialog;
	private JSONArray array;
	private FansAdapter adapter;
	private String name;
	private String currentNick;//当前界面的昵称
	private View top_panel;
	private Button top_btn_left;
	private Button top_btn_right;
	private TextView top_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fans);
		setUpViews();//设置view
		setUpListeners();//设置listenter
		asyncImageLoader = new AsyncImageLoader();
		dataHelper = new DataHelper(FansActivity.this);
		weibo = new MyWeiboSync();
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");//本地文件保存的你的登录昵称
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
		}
		
		weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");//获取从前面页面传递过来的数据
		currentNick = intent.getStringExtra("nick");
		top_title.setText(currentNick+"的粉丝");
		
		progressDialog = new ProgressDialog(FansActivity.this);// 生成一个进度条
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");
		
		handler = new FansHandler();
		thread = new FansThread();
		thread.start();//开启一个线程获取数据
		progressDialog.show();
	}
	
	private void setUpViews(){
		listView = getListView();
		top_panel = (View)findViewById(R.id.fans_top);
		top_btn_left = (Button)top_panel.findViewById(R.id.top_btn_left);
		top_btn_right = (Button)top_panel.findViewById(R.id.top_btn_right);
		top_title = (TextView)top_panel.findViewById(R.id.top_title);
	}
	
	private void setUpListeners(){
		listView.setOnItemClickListener(this);
	}
	
	class FansThread extends Thread {
		@Override
		public void run() {
			String jsonStr = weibo.getFans(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), 20, 0, name);
			try {
				JSONObject dataObj = new JSONObject(jsonStr).getJSONObject("data");
				array = dataObj.getJSONArray("info");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//通知handler处理数据
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	class FansHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			adapter = new FansAdapter(FansActivity.this, array);
			listView.setAdapter(adapter);
			progressDialog.dismiss();// 关闭进度条
		}
	}
	
	class FansAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private JSONArray array;
		
		public FansAdapter(Context context, JSONArray array) {
			super();
			this.context = context;
			this.array = array;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return array.length();
		}

		@Override
		public Object getItem(int position) {
			return array.opt(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			asyncImageLoader = new AsyncImageLoader();
			FansViewHolder viewHolder = new FansViewHolder();
			JSONObject data = (JSONObject)array.opt(position);
			convertView = inflater.inflate(R.layout.fans_list_item, null);
			
			viewHolder.fans_headicon = (ImageView) convertView.findViewById(R.id.fans_headicon);
			viewHolder.fans_nick = (TextView) convertView.findViewById(R.id.fans_nick);
			viewHolder.fans_name = (TextView) convertView.findViewById(R.id.fans_name);
			
			Drawable cachedImage = null;
			if(data!=null){
				try {
					convertView.setTag(data.get("name"));
					viewHolder.fans_nick.setText(data.getString("nick"));
					viewHolder.fans_name.setText("@"+data.getString("name"));
					//异步加载图片
					cachedImage = asyncImageLoader.loadDrawable(data.getString("head")+"/100",viewHolder.fans_headicon, new ImageCallback(){
	                    @Override
	                    public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                        imageView.setImageDrawable(imageDrawable);
	                    }
	                });
					if (cachedImage == null) {
						viewHolder.fans_headicon.setImageResource(R.drawable.icon);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return convertView;
		}
	}
	
	static class FansViewHolder {
		private ImageView fans_headicon;
		private TextView fans_nick;
		private TextView fans_name;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
		Intent intent = new Intent(FansActivity.this,UserInfoActivity.class);
		try {
			JSONObject fansInfo = (JSONObject)array.opt(position);
			JSONArray tweets = fansInfo.getJSONArray("tweet");
			JSONObject tweet = null;
			if(tweets!=null&&tweets.length()>0){
				tweet = (JSONObject)tweets.opt(0);
				intent.putExtra("origtext", tweet.getString("text"));
				intent.putExtra("timestamp", TimeUtil.getStandardTime(tweet.getLong("timestamp")));
			}
			intent.putExtra("name", fansInfo.getString("name"));
			intent.putExtra("nick", fansInfo.getString("nick"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		startActivity(intent);//跳转到用户信息界面
	}
}
