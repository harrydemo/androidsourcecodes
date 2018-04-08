
package com.android.ui;

/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import android.app.Activity;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.datastore.DatabaseManager;
import com.android.datastruct.ListItemBean;
import com.android.datastruct.TwintterBean;
import com.android.tools.AndroidHttp;
import com.android.tools.AsyncImgLoader;
import com.android.tools.HttpResponseCallback;
import com.android.tools.MyDialog;
import com.android.tools.RegexCallback;
import com.android.tools.RegexTool;
import com.android.weibo.OAuthConstant;
import com.android.weibo.WeiboActivity;
import com.android.weibo.WeiboActivitySend;
import com.sph.player.R;
import com.sph.player.activity.PlayerActivity;

import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.RequestToken;

/*
 * 视频详情页
 * */
public class TypeActivityItemDetail extends Activity
	implements android.widget.AdapterView.OnItemClickListener, HttpResponseCallback, RegexCallback{

	public static final String TAG = "SPH/TypeActivityItemDetail";
	ListView AboutViewListView;
	private String DefaultWebsite;
	private String GotoUrl;
	private String GotoUrlCollect;
	private String ImageSrc;
	ListViewDetailAdapter LVDAdapter;
	private String OpenVideoUrl;
	private int PushTabBtnIndex;
	private RegexTool RegexAnalysis;
	private TextView ShowLength;
	private TextView ShowTitle;
	private Activity TypeActivityContext;
	private String VidewLen;
	private String VidewTitle;
	TypeActivityItemDetail callBack;
	ImageView category_navigation;
	TextView category_navigation_text;
	private ImageView collectBtn;
	Context ct;
	private ArrayList dataStruct;
	private DatabaseManager dbManager;
	MyDialog dialog;
	private String finalUrl;
	private AsyncImgLoader imageLoader;
	protected Handler mHandler;
	ImageView new_video;
	TextView new_video_text;
	private ImageView novideo_btn;
	private ImageView playButton;
	private ImageView preview_images;
	private String publishWeibo;
	private ImageView returnBackBtn;
	private ImageView sinaTritter;
	ImageView video_collect;
	TextView video_collect_text;
	ImageView video_more;
	TextView video_more_text;
	ImageView video_search;
	TextView video_search_text;

	//构造函数
	public TypeActivityItemDetail(){
		returnBackBtn = null;
		playButton = null;
		novideo_btn = null;
		collectBtn = null;
		sinaTritter = null;
		GotoUrlCollect = "";
		GotoUrl = "";
		VidewTitle = "";
		VidewLen = "";
		ImageSrc = "";
		ShowTitle = null;
		ShowLength = null;
		imageLoader = new AsyncImgLoader();
		preview_images = null;
		TypeActivityContext = null;
		PushTabBtnIndex = 1;
		DefaultWebsite = "youku";
		RegexAnalysis = null;
		dataStruct = null;
		category_navigation = null;
		new_video = null;
		video_search = null;
		video_collect = null;
		video_more = null;
		category_navigation_text = null;
		new_video_text = null;
		video_search_text = null;
		video_collect_text = null;
		video_more_text = null;
		OpenVideoUrl = "";
		finalUrl = "";
		dbManager = null;
		publishWeibo = "";
		MyDialog mydialog = new MyDialog(this);
		dialog = mydialog;
		ct = null;
		callBack = null;
		
		mHandler = new Handler();
	}
	//设置默认焦点
	private void DefaultFocusedTab(){
		//根据当前所在的焦点处进行相应设置
		switch(PushTabBtnIndex){
			//分类导航
			case 1:
				category_navigation.setImageResource(R.drawable.home_select);
				new_video.setImageResource(R.drawable.today_news_normal);
				video_search.setImageResource(R.drawable.search_normal);
				video_collect.setImageResource(R.drawable.favorite_normal);
				break;
			
				//今日最新
				case 2:
					category_navigation.setImageResource(R.drawable.home_normal);
					new_video.setImageResource(R.drawable.today_news_select);
					video_search.setImageResource(R.drawable.search_normal);
					video_collect.setImageResource(R.drawable.favorite_normal);
				break;
				
				//搜索
				case 3:
					category_navigation.setImageResource(R.drawable.home_normal);
					new_video.setImageResource(R.drawable.today_news_normal);
					video_search.setImageResource(R.drawable.search_select);
					video_collect.setImageResource(R.drawable.favorite_normal);
					break;
				
			   //收藏
				case 4:
					category_navigation.setImageResource(R.drawable.home_normal);
					new_video.setImageResource(R.drawable.today_news_normal);
					video_search.setImageResource(R.drawable.search_normal);
					video_collect.setImageResource(R.drawable.favorite_select);
					break;
		}
	}

	private String Find_a_content(String s, String s1, String s2, String s3)
	{
		String s4 = "";
		Matcher matcher = Pattern.compile(s1).matcher(s);
		do
		{
			if (!matcher.find())
			{
				if (s4 != null)
				{
					if (s2 != null && s2.length() > 0)
						s4 = s4.replace(s2, "");
					if (s3 != null && s3.length() > 0)
						s4 = s4.replace(s3, "");
				}
				return s4;
			}
			s4 = matcher.group();
		} while (true);
	}

	//Http初始化函数
	private void HttpInit(){
		//创建Http通信类
		AndroidHttp androidhttp = new AndroidHttp(this, this);
		Object aobj[] = new Object[1];
		aobj[0] = GotoUrl;
		
		//开启线程执行http通信
		android.os.AsyncTask asynctask = androidhttp.execute(aobj);
	}

	//初始化函数
	private void Init(){
		//显示的标题
		ShowTitle = (TextView)findViewById(R.id.video_title);
		ShowTitle.setText(VidewTitle);

		//显示视频长度
		ShowLength = (TextView)findViewById(R.id.video_length);
		ShowLength.setText(VidewLen);

		TypeActivityContext = this;
		//获取相关视频的listview
		AboutViewListView = (ListView)findViewById(R.id.list_detail_views);
		//设置listview的点击事件监听器
		AboutViewListView.setOnItemClickListener(this);

		//获取显示的图片
		preview_images= (ImageView)findViewById(R.id.preview_images);
		//设置图片的点击事件监听器
		preview_images.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (OpenVideoUrl != null && OpenVideoUrl.length() > 0){
					AndroidHttp androidhttp = new AndroidHttp(ct, callBack);
					Object aobj[] = new Object[1];
					aobj[0] = OpenVideoUrl;
					android.os.AsyncTask asynctask = androidhttp.execute(aobj);
				}
			}
		});
		
		//获取返回按钮图片
		returnBackBtn = (ImageView)findViewById(R.id.return_btn);
		returnBackBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				finish();
			}
		});
		
		//获取播放按钮
		playButton = (ImageView)findViewById(R.id.play_btn);
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (OpenVideoUrl != null && OpenVideoUrl.length() > 0){
					AndroidHttp androidhttp = new AndroidHttp(ct, callBack);
					Object aobj[] = new Object[1];
					aobj[0] = OpenVideoUrl;
					android.os.AsyncTask asynctask = androidhttp.execute(aobj);
				}
			}
		});
		
		//获取值播放声音的按钮
		novideo_btn = (ImageView)findViewById(R.id.novideo_btn);
		novideo_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				Toast.makeText(ct, "暂时不支持", 0).show();
			}
		});
		
		//获取收藏按钮
		collectBtn = (ImageView)findViewById(R.id.collection_btn);
		collectBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				CollectVideo();
				Toast.makeText(TypeActivityContext, "收藏视频", 0).show();
			}
		});
		
		//转发到微博按钮
		sinaTritter = (ImageView)findViewById(R.id.sina_twitter);
		sinaTritter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				LoginWeiboVerdict();
			}
		});

		//创建数据对象
		dataStruct = new ArrayList();
		RegexAnalysis = new RegexTool(this, this);
		
		//Http初始化
		HttpInit();
		
		//加载图片
		if (ImageSrc != null && ImageSrc.length() > 0){
			LoadCallback mLoadCallback = new LoadCallback();
			Bitmap bitmap = imageLoader.loadDrawable(ImageSrc, mLoadCallback);
		}
	}

	//设置屏幕标识，设置保持屏幕一直活动，不进入休眠状态
	private void SetScreenOnFlag(){
		//获取window组件
		Window window = getWindow();
		//设置window的属性，保存屏幕一直活动
		if ((window.getAttributes().flags & 0x80) == 0)
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	//获取ListView的数据适配器
	private ListViewDetailAdapter getAdapter(){
		if (LVDAdapter == null){
			LVDAdapter = new ListViewDetailAdapter(this, dataStruct);
		} else{
			LVDAdapter = new ListViewDetailAdapter(this, dataStruct);
		}
		return LVDAdapter;
	}

	private int getDisplayMetrics(Context context)
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels / 5;
	}

	private void showAudioTab()
	{
	}

	private void showInfoDialog()
	{
	}

	private void showVideoTab()
	{
	}

	public void CloseWaitDialog()
	{
		dialog.ShowDialog();
	}

	//收藏事件函数
	public void CollectVideo(){
		
		dbManager = DatabaseManager.getInstance(this);
		ListItemBean listitembean = new ListItemBean();
		listitembean.setVideoHref(GotoUrlCollect);
		listitembean.setVideoImgSrc(ImageSrc);
		listitembean.setVideoLength(VidewLen);
		listitembean.setVideoTitle(VidewTitle);

		dbManager.addListTime(listitembean);
	}

	//创建微博URL
	public void CreateWeiboUrl(){
		Pattern pattern = Pattern.compile("vid=.*");
		Matcher matcher = pattern.matcher(GotoUrl);
		
		String s = "";
		String s1 = "";
		
		while(true){
			if (!matcher.find()){
				if (s.compareToIgnoreCase("youku") == 0){
					publishWeibo = (new StringBuilder("http://v.youku.com/v_show/id_")).append(s1).append(".html").toString();
				} 
				
				else if (s.compareToIgnoreCase("tudou") == 0){
					publishWeibo = (new StringBuilder("http://www.tudou.com/programs/view/")).append(s1).append("/").toString();
				} 
				
				else if (s.compareToIgnoreCase("sina") == 0){
					publishWeibo = (new StringBuilder("http://video.sina.com.cn/")).append(s1).append(".html").toString();
				} 
				
				else if (s.compareToIgnoreCase("ku6") == 0){
					publishWeibo = (new StringBuilder("http://v.ku6.com/show/")).append(s1).append(".html").toString();
				}
				
				else if (s.compareToIgnoreCase("6") == 0){
					publishWeibo = (new StringBuilder("http://6.cn/watch/")).append(s1).append(".html").toString();
				}
				
				break;
			}
			s = Find_a_content(matcher.group(), "&site=.*", "&site=", "");
			s1 = Find_a_content(matcher.group(), "vid=.*?&", "vid=", "&");	
		}
	}

	//获取视频带宽
	public String GetBw(){
		return getSharedPreferences("faplayer_prefer", 0).getString("band_width", "");
	}

	
	public void GotoPlayerView(String s, String s1)
	{
	}


	//进入收藏页面
	public void GotoCollectView(){
		Intent intent = new Intent();
		intent.setClass(this, TypeActivityCollect.class);
		
		Bundle bundle = new Bundle();
		String s = String.valueOf(PushTabBtnIndex);
		String s1 = (new StringBuilder(s)).toString();
		bundle.putString("FROME_PAGE", s1);
		
		Intent intent2 = intent.putExtras(bundle);
		startActivity(intent);
	}


	//进入今日最新页面
	public void GotoNewEst(){
		Intent intent = new Intent();
		intent.setClass(this, TypeActivityToday.class);
		Bundle bundle = new Bundle();
		String s = String.valueOf(PushTabBtnIndex);
		String s1 = (new StringBuilder(s)).toString();
		bundle.putString("FROME_PAGE", s1);
		
		intent.putExtras(bundle);
		startActivity(intent);
	}

	//进入查找界面
	public void GotoSearchView(){
		Intent intent = new Intent();
		intent.setClass(this, SearchActivity.class);
		
		Bundle bundle = new Bundle();
		String s = String.valueOf(PushTabBtnIndex);
		String FROME_PAGE = (new StringBuilder(s)).toString();
		bundle.putString("FROME_PAGE", FROME_PAGE);
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	//进入到详情view
	public void GotoDetailView(String Url, String title){
		//创建intent对象，进入到详情页面
		Intent intent = new Intent();
		//设置class，com.android.ui.TypeActivityDetail.class为详情页面
		intent.setClass(this, com.android.ui.TypeActivityDetail.class);
		
		//创建数据类
		Bundle bundle = new Bundle();
		bundle.putString("REQUEST_URL", Url);
		bundle.putString("TITLE", title);
		
		//设置intent携带的数据
		intent.putExtras(bundle);
		
		//开始跳转
		startActivity(intent);
	}


	//初始化底部视频
	public void IntiTabView(){
		//分类导航图片控件
		category_navigation = (ImageView)findViewById(R.id.category_navigation);
		//分类导航图片控件点击事件监听器
		category_navigation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				finish();
			}
		});
		
		//今日最新图片控件
		new_video = (ImageView)findViewById(R.id.new_video);
		//今日最新图片控件点击事件监听器
		new_video.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				GotoNewEst();
			}

		});
		
		//视频搜索图片控件
		video_search = (ImageView)findViewById(R.id.video_search);
		//视频搜索图片控件的点击事件监听器
		video_search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				GotoSearchView();
			}
		});
		
		//收藏图片控件
		video_collect = (ImageView)findViewById(R.id.video_collect);
		//收藏图片控件点击事件监听器
		video_collect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				GotoCollectView();
			}
		});
		
		
		//默认tab的焦点
		DefaultFocusedTab();
	}

	//登录微博
	public void LoginWeibo(){
		try {
			String s1 = System.setProperty("weibo4j.oauth.consumerKey",
					Weibo.CONSUMER_KEY);
			String s3 = System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			;
			String s4 = getResources().getString(R.string.app_name);
			RequestToken requesttoken = weibo.getOAuthRequestToken();
			OAuthConstant.getInstance().setRequestToken(requesttoken);
			String s5 = String.valueOf(requesttoken.getAuthenticationURL());
			String WEIBO = (new StringBuilder(s5)).append("&display=mobile")
					.toString();
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setClass(this, WeiboActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("WEIBO", WEIBO);
			bundle.putString("WEIBO_SHARE_URL", publishWeibo);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			String s8 = getResources().getString(R.string.err_message_2);
			Toast.makeText(this, s8, 0).show();
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	//转发到微博
	public void LoginWeiboVerdict(){
		TwintterBean twintterbean = DatabaseManager.getInstance(this).
			SelectTwintterToken();
		String s = twintterbean.GetToken();
		
		if (s != null && s.length() > 0){
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setClass(this, WeiboActivitySend.class);
			
			Bundle bundle = new Bundle();
			String WEIBO_TOKEN = twintterbean.GetToken();
			bundle.putString("WEIBO_TOKEN", WEIBO_TOKEN);
			
			String WEIBO_TOKENSECRET = twintterbean.GetTokenSecret();
			bundle.putString("WEIBO_TOKENSECRET", WEIBO_TOKENSECRET);
			
			String WEIBO_SHARE_URL = publishWeibo;
			bundle.putString("WEIBO_SHARE_URL", WEIBO_SHARE_URL);
			intent.putExtras(bundle);
			
			startActivity(intent);
		} else{
			LoginWeibo();
		}
	}

	//数据加载完毕，更新UI函数
	public void NotifyDataAnalysisFinished(){
		Toast toast;
		
		if (dataStruct != null && AboutViewListView != null){
			AboutViewListView.setAdapter(getAdapter());
		} else{
			toast = Toast.makeText(this, "ItemsListView empty", 0);
		}
	}

	//打开播放器的intetn
	public void OpenPlayerIntent(){
		//创建Intent对象，用于跳转到播放界面
		Intent intent = new Intent();
		//设置需要跳转的类
		intent.setClass(this, PlayerActivity.class);
		//设置intent的action
		intent.setAction("android.intent.action.VIEW");
		
		//创建数据对象
		Bundle bundle = new Bundle();
		bundle.putString("FINAL_URL", finalUrl);
		
		//设置intent携带的数据
		intent.putExtras(bundle);
		
		//开始跳转
		startActivity(intent);
	}

	//页面响应函数
	public void RegexRespone(String s, String Len){
		//设置显示视频长度
		if (Len != null && Len.length() > 0){
			VidewLen = Len;
			ShowLength.setText(VidewLen);
		}
		
		//获取打开视频的URL
		OpenVideoUrl = "";
		String s3 = String.valueOf(OpenVideoUrl);
		OpenVideoUrl = (new StringBuilder(s3)).append("http://m.vfun.tv:8080/webparser/").append(s).toString();
		String s5 = String.valueOf(OpenVideoUrl);
		OpenVideoUrl = (new StringBuilder(s5)).append("&bw=").toString();

		
		//获取当前手机的带宽
		String bandWidth = GetBw();
		if (bandWidth == ""){
			String s8 = String.valueOf(OpenVideoUrl);
			OpenVideoUrl = (new StringBuilder(s8)).append("10").toString();
		} else{
			String s10 = String.valueOf(OpenVideoUrl);
			OpenVideoUrl = (new StringBuilder(s10)).append(bandWidth).toString();
		}
	}

	//页面相应函数
	public void RegexRespone(String s, String s1, String s2, String s3){
		ListItemBean listitembean = new ListItemBean();
		listitembean.setVideoHref(s);
		listitembean.setVideoTitle(s1);
		listitembean.setVideoLength(s2);
		listitembean.setVideoImgSrc(s3);
		
		boolean flag = dataStruct.add(listitembean);
	}

	//响应数据函数
	public void ResponseData(String s){
		if (!s.contains("socket_error")){
			if (s.contains("a.flv?key=")){
				Matcher matcher = Pattern.compile("http://.*?\"").matcher(s);

				if(matcher.find()){
					finalUrl = matcher.group();
					if (finalUrl != null && finalUrl.length() > 0){
						finalUrl = finalUrl.replace("\"", "");
						OpenPlayerIntent();
					}
				}
			}
			
			Toast toast;
			if (RegexAnalysis != null){
				RegexAnalysis.RegexAnalysisItemDetail(s);
			}
			else{
				toast = Toast.makeText(this, "Regex empty", 0);
			}
		} else {
			//关闭等待对话框
			CloseWaitDialog();
		}

	}

	public void changeTabClick(View view)
	{
	}

	public boolean onContextItemSelected(MenuItem menuitem)
	{
		return super.onContextItemSelected(menuitem);
	}

	//activity的创建函数
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		//设置使用的布局文件
		setContentView(R.layout.type_activity_item_detail);
		ct = this;
		callBack = this;
		
		//获取intent携带的数据，初始化相关变量
		Bundle bundle1 = getIntent().getExtras();
		GotoUrlCollect = bundle1.getString("REQUEST_URL");
		StringBuilder stringbuilder = new StringBuilder("http://m.vfun.tv:8080/webparser/");
		GotoUrl = stringbuilder.append(GotoUrlCollect).toString();
		VidewTitle = bundle1.getString("TITLE");
		VidewLen = bundle1.getString("VIDEO_LEN");
		ImageSrc = bundle1.getString("IMAGE_SRC");
		PushTabBtnIndex = Integer.parseInt(bundle1.getString("FROME_PAGE"));

		//创建微博的URL
		CreateWeiboUrl();
		//设置屏幕显示模式
		SetScreenOnFlag();
		//初始化函数
		Init();

		//初始化底部视频
		IntiTabView();
	}

	//Menu函数
	public boolean onCreateOptionsMenu(Menu menu){
		return super.onCreateOptionsMenu(menu);
	}

	//listview的点击事件监听器接口函数
	public void onItemClick(AdapterView adapterview, View view, int i, long l){
		ListItemBean listitembean = (ListItemBean)AboutViewListView.getItemAtPosition(i);
		
		String video_href = listitembean.getVideoHref();
		String video_length = listitembean.getVideoLength();
		String video_imgsrc = listitembean.getVideoImgSrc();
		String video_title = listitembean.getVideoTitle();
		Intent intent = new Intent();
		intent.setClass(this, TypeActivityItemDetail.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("REQUEST_URL", video_href);
		bundle.putString("TITLE", video_title);
		bundle.putString("VIDEO_LEN", video_length);
		bundle.putString("IMAGE_SRC", video_imgsrc);
		String s4 = String.valueOf(PushTabBtnIndex);
		String s5 = (new StringBuilder(s4)).toString();
		bundle.putString("FROME_PAGE", s5);

		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	public boolean onOptionsItemSelected(MenuItem menuitem)
	{
		return super.onOptionsItemSelected(menuitem);
	}

	public boolean onSearchRequested()
	{
		return false;
	}

	public void searchClick(View view)
	{
		boolean flag = onSearchRequested();
	}

	
	//加载视频的回调函数
	private class LoadCallback
		implements com.android.tools.AsyncImgLoader.ImageCallback{
		final TypeActivityItemDetail this$0;

		public void imageLoaded(Bitmap bitmap, String s){
			if (bitmap != null){
				preview_images.setImageBitmap(bitmap);
			}
		}
	
		LoadCallback(){
			super();
			this$0 = TypeActivityItemDetail.this;
		}
	}

}
