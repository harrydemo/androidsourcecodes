package com.android.caigang.view;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.AsyncImageLoader;
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.android.caigang.util.TimeUtil;
import com.mime.qweibo.examples.MyWeiboSync;

public class WeiboDetailActivity extends Activity {
	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private Handler handler;
	private AsyncImageLoader asyncImageLoader; 
	private GetDetailThread thread;
	private String weiboid;
	private String returnJsonStr;
	private JSONObject dataObj ;
	private ImageView show_headicon;
	private ImageView show_image;
	private TextView show_count_mcount;
	private ImageView show_delete;
	private TextView show_nick;
	private TextView show_email;
	private TextView show_origtext;
	private TextView show_time;
	private TextView show_from;
	private Button to_userinfo_btn;
	private Button show_star_btn;
	private Button show_back_btn;
	private TextView show_rebroad_btn;
	private TextView show_dialog_btn;
	private TextView show_remark_btn;
	private Button show_tohome_btn;
	private RelativeLayout show_top;
	private View weibodetail_bottom3_bar;
	private JSONObject source = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_detail);
		setUpViews();//设置view
		setUpListeners();//设置listenter
		asyncImageLoader = new AsyncImageLoader();
		
		Intent intent = getIntent();
		weiboid = intent.getStringExtra("weiboid");
		dataHelper = new DataHelper(WeiboDetailActivity.this);
		weibo = new MyWeiboSync();
		
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");
		
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
		}
		
		weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());
		
		handler = new DealHandler();
		thread = new GetDetailThread();
		thread.start();//开启一个线程获取数据
	}
	
	private void setUpViews(){
		show_headicon = (ImageView) findViewById(R.id.show_headicon);
		show_delete = (ImageView) findViewById(R.id.show_delete);
		show_nick = (TextView) findViewById(R.id.show_nick);
		show_email = (TextView) findViewById(R.id.show_email);
		show_origtext = (TextView) findViewById(R.id.show_origtext);
		show_image = (ImageView) findViewById(R.id.show_image);
		show_count_mcount = (TextView)findViewById(R.id.show_count_mcount);
		show_time = (TextView) findViewById(R.id.show_time);
		show_from = (TextView) findViewById(R.id.show_from);
		to_userinfo_btn = (Button) findViewById(R.id.to_userinfo_btn);
		show_star_btn = (Button) findViewById(R.id.show_star_btn);
		show_back_btn = (Button) findViewById(R.id.show_back_btn);
		weibodetail_bottom3_bar = (View)findViewById(R.id.weibo_detail_bottom_bar);
		show_rebroad_btn = (TextView)weibodetail_bottom3_bar.findViewById(R.id.show_rebroad_btn);
		show_dialog_btn = (TextView)weibodetail_bottom3_bar.findViewById(R.id.show_dialog_btn);
		show_remark_btn = (TextView)weibodetail_bottom3_bar.findViewById(R.id.show_remark_btn);
		show_tohome_btn = (Button) findViewById(R.id.show_tohome_btn);
		show_top = (RelativeLayout)findViewById(R.id.show_top);
	}
	
	private void setUpListeners(){
		show_top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeiboDetailActivity.this,UserInfoActivity.class);
				try {
					intent.putExtra("name", dataObj.getString("name"));
					intent.putExtra("nick", dataObj.getString("nick"));
					intent.putExtra("origtext", dataObj.getString("origtext"));
					intent.putExtra("timestamp", TimeUtil.getStandardTime(dataObj.getLong("timestamp")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);//跳转到用户信息界面
				
			}
		});
		to_userinfo_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeiboDetailActivity.this,UserInfoActivity.class);
				try {
					intent.putExtra("name", dataObj.getString("name"));
					intent.putExtra("nick", dataObj.getString("nick"));
					intent.putExtra("origtext", dataObj.getString("origtext"));
					intent.putExtra("timestamp", TimeUtil.getStandardTime(dataObj.getLong("timestamp")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);//跳转到用户信息界面
			}
		});
		show_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//跳到大图浏览界面.
			}
		});
		
		show_count_mcount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//此微博的转播和点评
				Toast.makeText(WeiboDetailActivity.this, "将显示此微博的转播和点评列表", Toast.LENGTH_SHORT).show();
			}
		});
		
		show_rebroad_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {//转播此条微博
				Intent intent = new Intent(WeiboDetailActivity.this,AddWeiboActivity.class);
				try {
					if(source!=null){
						intent.putExtra("tip", "转播 "+source.getString("nick"));
					}else{
						intent.putExtra("tip", "转播 "+dataObj.getString("nick"));
					}
					if(dataObj.getString("origtext")!=null&&!"".equals(dataObj.getString("origtext"))){
						intent.putExtra("content", "|| @"+dataObj.getString("nick")+": "+dataObj.getString("origtext"));
						intent.putExtra("reid", dataObj.getString("id"));
					}else{
						intent.putExtra("content", "|| @"+source.getString("nick")+": ");
						intent.putExtra("reid", source.getString("id"));
					}
					intent.putExtra("from_flag", "rebroad");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
		
		show_dialog_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {//对话此条微博所有者
				Intent intent = new Intent(WeiboDetailActivity.this,AddWeiboActivity.class);
				try {
					intent.putExtra("tip", "对话 "+dataObj.getString("nick"));
					intent.putExtra("to",dataObj.getString("name"));//对话人的name
					intent.putExtra("from_flag", "private");
					intent.putExtra("content", "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
		
		show_remark_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {//点评此条微博
				Intent intent = new Intent(WeiboDetailActivity.this,AddWeiboActivity.class);
				try {
					if(source!=null){
						intent.putExtra("tip", "点评 "+source.getString("nick"));
					}else{
						intent.putExtra("tip", "点评 "+dataObj.getString("nick"));
					}
					if(dataObj.getString("origtext")!=null&&!"".equals(dataObj.getString("origtext"))){
						intent.putExtra("content", "|| @"+dataObj.getString("nick")+": "+dataObj.getString("origtext"));
						intent.putExtra("reid", dataObj.getString("id"));
					}else{
						intent.putExtra("content", "|| @"+source.getString("nick")+": ");
						intent.putExtra("reid", source.getString("id"));
					}
					intent.putExtra("from_flag", "comment");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
		
	}
	
	class GetDetailThread extends Thread {
		@Override
		public void run() {
			returnJsonStr = weibo.getWeiboDetail(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), weiboid);
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	class DealHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			Drawable cachedImage;
			try {
				dataObj = new JSONObject(returnJsonStr).getJSONObject("data");
				
				cachedImage = asyncImageLoader.loadDrawable(dataObj.getString("head")+"/100",show_headicon, new ImageCallback(){
	                @Override
	                public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                    imageView.setImageDrawable(imageDrawable);
	                }
	            });
				if (cachedImage == null) {
					show_headicon.setImageResource(R.drawable.icon);
				} else {
					show_headicon.setImageDrawable(cachedImage);
				}
				
				String count_mcount_text = "转播和点评("+(dataObj.getInt("count")+dataObj.getInt("mcount"))+")";//加下划线
				SpannableStringBuilder underlineSpannable=new SpannableStringBuilder(count_mcount_text);
				CharacterStyle span=new UnderlineSpan();  
				underlineSpannable.setSpan(span, 0, count_mcount_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				show_count_mcount.setText(underlineSpannable);
				
				
				show_nick.setText(dataObj.getString("nick"));
				show_email.setText("@"+dataObj.getString("name"));
				show_origtext.setText(dataObj.getString("origtext"));
				show_time.setText(TimeUtil.getStandardTime(dataObj.getLong("timestamp")));
				show_from.setText("来自"+dataObj.getString("from"));
				if(dataObj.getString("nick").equals(user.getUserName())){
					show_delete.setVisibility(View.VISIBLE);
				}
				
				JSONArray imageArray = dataObj.optJSONArray("image");//如果此微博有图片内容，就显示出来
				if(imageArray!=null&&imageArray.length()>0){
					String imageUrl = imageArray.optString(0)+"/460";//为什么加/460，腾讯规定的，支持160，2000，460还有一些，记不住了
					Drawable drawable = asyncImageLoader.loadDrawable(imageUrl,show_image, new ImageCallback(){
		                @Override
		                public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
		                    imageView.setImageDrawable(imageDrawable);
		                }
		            });
					show_image.setVisibility(View.VISIBLE);
				}
				
				if(!"null".equals(dataObj.getString("source"))){
					source = dataObj.getJSONObject("source");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
