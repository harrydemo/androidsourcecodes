package com.android.caigang.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.AsyncImageLoader;
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.android.caigang.util.TimeUtil;
import com.android.caigang.view.HomeTimeLineActivity.HomeViewHolder;
import com.mime.qweibo.examples.MyWeiboSync;

public class UserInfoActivity extends Activity implements OnItemClickListener{
	private String currentNick;
	private String name;
	private String origtext;
	private String timestamp;
	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private Handler handler;
	private AsyncImageLoader asyncImageLoader; 
	private UserInfoThread thread;
	private String weiboid;
	private String returnJsonStr;
	private JSONObject dataObj ;
	private ImageView user_headicon;
	private TextView user_nick;
	private TextView user_name;
	private TextView user_origtext;
	private TextView user_time;
	private TextView user_sex;
	private TextView user_age;
	private TextView user_location;
	private TextView user_verifyinfo;
	private Button user_back_btn;
	private Button user_dialog_btn;
	private Button user_message_btn;
	private GridView gridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		setUpViews();//设置view
		setUpListeners();//设置listenter
		asyncImageLoader = new AsyncImageLoader();
		dataHelper = new DataHelper(UserInfoActivity.this);
		weibo = new MyWeiboSync();
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");
		
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
		}
		
		weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());
		
		//获取从上一界面传递过来的数据
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		currentNick = intent.getStringExtra("nick");//昵称
		origtext = intent.getStringExtra("origtext");
		timestamp = intent.getStringExtra("timestamp");
		user_name.setText("@"+name);
		user_origtext.setText(origtext);
		user_time.setText(timestamp);
		
		handler = new UserInfoHandler();
		thread = new UserInfoThread();
		thread.start();//开启一个线程获取数据
	}
	
	private void setUpViews(){
		user_headicon = (ImageView) findViewById(R.id.user_headicon);
		user_nick = (TextView) findViewById(R.id.user_nick);
		user_name = (TextView) findViewById(R.id.user_name);
		user_origtext = (TextView) findViewById(R.id.user_origtext);
		user_time = (TextView) findViewById(R.id.user_time);
		user_sex = (TextView) findViewById(R.id.user_sex);
		user_age = (TextView) findViewById(R.id.user_age);
		user_location = (TextView) findViewById(R.id.user_location);
		user_verifyinfo = (TextView) findViewById(R.id.user_verifyinfo);
		user_back_btn = (Button) findViewById(R.id.user_back_btn);
		user_dialog_btn = (Button) findViewById(R.id.user_dialog_btn);
		user_message_btn = (Button) findViewById(R.id.user_message_btn);
		gridView = (GridView)findViewById(R.id.user_grid);
	}
	
	private void setUpListeners(){
		gridView.setOnItemClickListener(this);
	}
	
	class UserInfoThread extends Thread {
		@Override
		public void run() {
			returnJsonStr = weibo.getUserInfoByName(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), name);
			//通知handler处理数据
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	class UserInfoHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			Drawable cachedImage;
			try {
				dataObj = new JSONObject(returnJsonStr).getJSONObject("data");
				cachedImage = asyncImageLoader.loadDrawable(dataObj.getString("head")+"/100",user_headicon, new ImageCallback(){
	                @Override
	                public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                    imageView.setImageDrawable(imageDrawable);
	                }
	            });
				if (cachedImage == null) {
					user_headicon.setImageResource(R.drawable.icon);
				} else {
					user_headicon.setImageDrawable(cachedImage);
				}
				user_nick.setText(dataObj.getString("nick"));
				user_sex.setText(dataObj.getInt("sex")==1?"男":"女");
				if(dataObj.getInt("birth_year")!=0){
					user_age.setText((Calendar.getInstance().get(Calendar.YEAR)-dataObj.getInt("birth_year"))+"岁");
				}
				user_location.setText(dataObj.getString("location"));
				String verifyinfo = dataObj.getString("verifyinfo");
				if(verifyinfo==null||"".equals(verifyinfo)){
					user_verifyinfo.setText("这家伙很懒,没留什么");
				}else{
					user_verifyinfo.setText(verifyinfo);
				}
				final List<String> numsList = new ArrayList<String>();
				numsList.add(dataObj.getString("tweetnum"));
				numsList.add(dataObj.getString("fansnum"));
				numsList.add(dataObj.getString("idolnum"));
				gridView.setAdapter(new GridAdapter(UserInfoActivity.this, numsList));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	class GridAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		List<String> numList;
		
		public GridAdapter(Context context, List<String> numList) {
			super();
			this.context = context;
			this.numList = numList;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return numList.size();
		}

		@Override
		public Object getItem(int position) {
			return numList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent){
			convertView = inflater.inflate(R.layout.userinfo_grid_item, null);
			TextView num = (TextView)convertView.findViewById(R.id.userinfo_grid_num);
			TextView title = (TextView)convertView.findViewById(R.id.userinfo_grid_title);
			ImageView image = (ImageView)convertView.findViewById(R.id.userinfo_grid_image);
			switch (position) {
			case 0:
				num.setText(numList.get(0));
				title.setText("广播");
				image.setVisibility(View.VISIBLE);
				break;
			case 1:
				num.setText(numList.get(1));
				title.setText("听众");
				image.setVisibility(View.VISIBLE);
				break;
			case 2:
				num.setText(numList.get(2));
				title.setText("收听");
				break;

			default:
				break;
			}
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
		switch (position) {
		case 0:
			Intent intent = new Intent(UserInfoActivity.this,TweetsActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("nick",currentNick);
			startActivity(intent);
			break;
		case 1:
			Intent intent2 = new Intent(UserInfoActivity.this,FansActivity.class);
			intent2.putExtra("name", name);
			intent2.putExtra("nick",currentNick);
			startActivity(intent2);
			break;
		case 2:
			Intent intent3 = new Intent(UserInfoActivity.this,IdolActivity.class);
			intent3.putExtra("name", name);
			intent3.putExtra("nick",currentNick);
			startActivity(intent3);
			break;
		default:
			break;
		}
		
	}
}
