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
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.android.caigang.util.TimeUtil;
import com.android.caigang.view.HomeTimeLineActivity.HomeAdapter;
import com.mime.qweibo.examples.MyWeiboSync;
import com.mime.qweibo.examples.QWeiboType.PageFlag;

public class TweetsActivity extends ListActivity implements OnItemClickListener{
	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private Handler handler;
	private AsyncImageLoader asyncImageLoader; 
	private TweetsThread thread;
	private ListView listView;
	private ProgressDialog progressDialog;
	private JSONArray array;
	private TweetsAdapter adapter;
	private String name;
	private String currentNick;//当前界面的昵称
	private View top_panel;
	private Button top_btn_left;
	private Button top_btn_right;
	private TextView top_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweets);
		setUpViews();//设置view
		setUpListeners();//设置listenter
		
		asyncImageLoader = new AsyncImageLoader();
		dataHelper = new DataHelper(TweetsActivity.this);
		weibo = new MyWeiboSync();
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
		}
		
		weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");//获取从前面页面传递过来的数据
		currentNick = intent.getStringExtra("nick");
		top_title.setText(currentNick+"的广播");
		
		progressDialog = new ProgressDialog(TweetsActivity.this);// 生成一个进度条
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");
		
		handler = new TweetsHandler();
		thread = new TweetsThread();
		thread.start();//开启一个线程获取数据
		progressDialog.show();
		
	}
	
	private void setUpViews(){
		listView = getListView();
		top_panel = (View)findViewById(R.id.tweets_top);
		top_btn_left = (Button)top_panel.findViewById(R.id.top_btn_left);
		top_btn_right = (Button)top_panel.findViewById(R.id.top_btn_right);
		top_title = (TextView)top_panel.findViewById(R.id.top_title);
	}
	
	private void setUpListeners(){
		listView.setOnItemClickListener(this);
	}
	
	class TweetsThread extends Thread {
		@Override
		public void run() {
			String jsonStr = weibo.getTweets(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(),PageFlag.PageFlag_First, 0, 20, 0,name);
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
	
	class TweetsHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			adapter = new TweetsAdapter(TweetsActivity.this, array);
			listView.setAdapter(adapter);
			progressDialog.dismiss();// 关闭进度条
		}
	}
	
	class TweetsAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private JSONArray array;
		
		public TweetsAdapter(Context context, JSONArray array) {
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
			TweetsViewHolder viewHolder = new TweetsViewHolder();
			JSONObject data = (JSONObject)array.opt(position); 
			JSONObject source = null;
			convertView = inflater.inflate(R.layout.tweets_list_item, null);
			try {
				source = data.getJSONObject("source");
			} catch (JSONException e1) {
				e1.printStackTrace(); 
			}
			viewHolder.tweets_headicon = (ImageView) convertView.findViewById(R.id.tweets_headicon);
			viewHolder.tweets_nick = (TextView) convertView.findViewById(R.id.tweets_nick);
			viewHolder.tweets_hasimage = (ImageView) convertView.findViewById(R.id.tweets_hasimage);
			viewHolder.tweets_timestamp = (TextView) convertView.findViewById(R.id.tweets_timestamp);
			viewHolder.tweets_origtext = (TextView) convertView.findViewById(R.id.tweets_origtext);
			viewHolder.tweets_source = (TextView) convertView.findViewById(R.id.tweets_source);
			
			if(data!=null){
				try {
					convertView.setTag(data.get("id"));
					viewHolder.tweets_nick.setText(data.getString("nick"));
					viewHolder.tweets_timestamp.setText(TimeUtil.converTime(Long.parseLong(data.getString("timestamp"))));
					viewHolder.tweets_origtext.setText(data.getString("origtext"), TextView.BufferType.SPANNABLE);
					
					if(source!=null){
						viewHolder.tweets_source.setText(source.getString("origtext"));
						viewHolder.tweets_source.setBackgroundResource(R.drawable.source_bg);
					}
					//异步加载图片
					Drawable cachedImage = asyncImageLoader.loadDrawable(data.getString("head")+"/100",viewHolder.tweets_headicon, new ImageCallback(){
	                    @Override
	                    public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                        imageView.setImageDrawable(imageDrawable);
	                    }
	                });
					if (cachedImage == null) {
						viewHolder.tweets_headicon.setImageResource(R.drawable.icon);
					} else {
						viewHolder.tweets_headicon.setImageDrawable(cachedImage);
					}
					if(data.getJSONArray("image")!=null){
						viewHolder.tweets_hasimage.setImageResource(R.drawable.hasimage);
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
	
	static class TweetsViewHolder {
		private ImageView tweets_headicon;
		private TextView tweets_nick;
		private TextView tweets_timestamp;
		private TextView tweets_origtext;
		private TextView tweets_source;
		private ImageView tweets_hasimage;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		JSONObject weiboInfo = (JSONObject)array.opt(position);
		Intent intent = new Intent(TweetsActivity.this, WeiboDetailActivity.class);
		try {
			intent.putExtra("weiboid", weiboInfo.getString("id"));
			startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
