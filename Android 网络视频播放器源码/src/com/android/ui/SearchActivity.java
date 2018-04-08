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
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.animation.TranslateAnimation;
import android.widget.*;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.android.datastruct.ListItemBean;
import com.android.datastruct.ListPagesBean;
import com.android.tools.AndroidHttp;
import com.android.tools.HttpResponseCallback;
import com.android.tools.MyDialog;
import com.android.tools.RegexCallback;
import com.android.tools.RegexTool;
import com.sph.player.R;

//查找的页面
public class SearchActivity extends Activity
	implements android.widget.AdapterView.OnItemClickListener, 
		HttpResponseCallback, RegexCallback{

	public static final String TAG = "SPH/TypeActivity";
	private String DefaultIndex;
	private int FromePage;
	String GotoUrl;
	private ListViewDetailAdapter LVDAdapter;
	private ListViewSearchAdapter LVSAdapter;
	private int PushTabBtnIndex;
	private RegexTool RegexAnalysis;
	private ImageView SearchButton;
	private String SearchWebsite;
	private int TypeOfListView;
	SearchActivity aTA;
	SearchActivity callBack;
	ImageView category_navigation;
	TextView category_navigation_text;
	Context ct;
	ArrayList dataStruct;
	ArrayList dataStruct2;
	MyDialog dialog;
	private int focusColor;
	private TranslateAnimation hideAction;
	ListPagesBean listPageBean;
	protected Handler mHandler;
	ImageView new_video;
	TextView new_video_text;
	ListView searchListView;
	private EditText search_box;
	private TranslateAnimation showAction;
	private ImageView title_tab_jianfang6;
	private TextView title_tab_jianfang6_1;
	private ImageView title_tab_ku6;
	private TextView title_tab_ku6_1;
	private ImageView title_tab_sina;
	private TextView title_tab_sina_1;
	private ImageView title_tab_tudou;
	private TextView title_tab_tudou_1;
	private ImageView title_tab_youku;
	private TextView title_tab_youku_1;
	int topTabbarIndex;
	private int unfocusColor;
	ImageView video_collect;
	TextView video_collect_text;
	ImageView video_more;
	TextView video_more_text;
	ImageView video_search;
	TextView video_search_text;

	//构造函数
	public SearchActivity(){
		title_tab_youku = null;
		title_tab_tudou = null;
		title_tab_ku6 = null;
		title_tab_jianfang6 = null;
		title_tab_sina = null;
		title_tab_youku_1 = null;
		title_tab_tudou_1 = null;
		title_tab_ku6_1 = null;
		title_tab_jianfang6_1 = null;
		title_tab_sina_1 = null;
		search_box = null;
		SearchButton = null;
		int i = Color.rgb(170, 188, 202);
		focusColor = i;
		int j = Color.rgb(90, 104, 115);
		unfocusColor = j;
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
		topTabbarIndex = 1;
		showAction = null;
		hideAction = null;
		PushTabBtnIndex = 3;
		FromePage = 0;
		SearchWebsite = "youku";
		DefaultIndex = "1";
		TypeOfListView = 1;
		LVSAdapter = null;
		ArrayList arraylist = new ArrayList();
		dataStruct = arraylist;
		GotoUrl = "";
		MyDialog mydialog = new MyDialog(this);
		dialog = mydialog;
		LVDAdapter = null;
		ArrayList arraylist1 = new ArrayList();
		dataStruct2 = arraylist1;
		listPageBean = null;
		ct = null;
		callBack = null;
		mHandler = new Handler();
	}

	private void ChangeTopTabbarImage(int i){
		switch(i){
		case 0:
			return;
			
		case 1:
			SearchWebsite = "youku";
			
			title_tab_youku.setImageResource(R.drawable.tab_1_focus);
			title_tab_youku_1.setTextColor(focusColor);

			title_tab_tudou.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_tudou_1.setTextColor(unfocusColor);

			title_tab_ku6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_ku6_1.setTextColor(unfocusColor);

			title_tab_jianfang6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_jianfang6_1.setTextColor(unfocusColor);

			title_tab_sina.setImageResource(R.drawable.tab_3_unfocus);
			title_tab_sina_1.setTextColor(unfocusColor);
			break;
			
		case 2:
			SearchWebsite = "tudou";
			title_tab_youku.setImageResource(R.drawable.tab_1_unfocus);
			title_tab_tudou.setImageResource(R.drawable.tab_2_focus);
			title_tab_ku6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_jianfang6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_sina.setImageResource(R.drawable.tab_3_unfocus);
			
			title_tab_youku_1.setTextColor(unfocusColor);
			title_tab_tudou_1.setTextColor(focusColor);
			title_tab_ku6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			break;
			
		case 3:
			SearchWebsite = "ku6";
			title_tab_youku.setImageResource(R.drawable.tab_1_unfocus);
			title_tab_tudou.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_ku6.setImageResource(R.drawable.tab_2_focus);
			title_tab_jianfang6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_sina.setImageResource(R.drawable.tab_3_unfocus);
			
			title_tab_youku_1.setTextColor(unfocusColor);
			title_tab_tudou_1.setTextColor(focusColor);
			title_tab_ku6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			break;
			
		case 4:
			SearchWebsite = "6";
			title_tab_youku.setImageResource(R.drawable.tab_1_unfocus);
			title_tab_tudou.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_ku6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_jianfang6.setImageResource(R.drawable.tab_2_focus);
			title_tab_sina.setImageResource(R.drawable.tab_3_unfocus);
			
			title_tab_youku_1.setTextColor(unfocusColor);
			title_tab_tudou_1.setTextColor(focusColor);
			title_tab_ku6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			break;
			
		case 5:
			SearchWebsite = "sina";
			title_tab_youku.setImageResource(R.drawable.tab_1_unfocus);
			title_tab_tudou.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_ku6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_jianfang6.setImageResource(R.drawable.tab_2_unfocus);
			title_tab_sina.setImageResource(R.drawable.tab_3_focus);
			
			title_tab_youku_1.setTextColor(unfocusColor);
			title_tab_tudou_1.setTextColor(focusColor);
			title_tab_ku6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			title_tab_jianfang6_1.setTextColor(unfocusColor);
			break;
		}
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
	//Http初始化函数
	private void HttpInit(){
		AndroidHttp androidhttp = new AndroidHttp(this, this);
		TypeOfListView = 1;
		Object aobj[] = new Object[1];
		aobj[0] = GotoUrl;

		android.os.AsyncTask asynctask = androidhttp.execute(aobj);
	}

	//设置屏幕标识，设置保持屏幕一直活动，不进入休眠状态
	private void SetScreenOnFlag(){
		//获取window组件
		Window window = getWindow();
		//设置window的属性，保存屏幕一直活动
		if ((window.getAttributes().flags & 0x80) == 0)
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	//获取数据适配器
	private ListViewSearchAdapter getAdapter(){
		if (LVSAdapter == null){
			ListViewSearchAdapter listviewsearchadapter = new ListViewSearchAdapter(this, dataStruct);
			LVSAdapter = listviewsearchadapter;
		} else{
			ListViewSearchAdapter listviewsearchadapter1 = new ListViewSearchAdapter(this, dataStruct);
			LVSAdapter = listviewsearchadapter1;
		}
		return LVSAdapter;
	}

	//获取数据适配器
	private ListViewDetailAdapter getAdapter2(){
		if (LVDAdapter == null){
			ListViewDetailAdapter listviewdetailadapter = new ListViewDetailAdapter(this, dataStruct2);
			LVDAdapter = listviewdetailadapter;
		} else{
			ListViewDetailAdapter listviewdetailadapter1 = new ListViewDetailAdapter(this, dataStruct2);
			LVDAdapter = listviewdetailadapter1;
		}
		return LVDAdapter;
	}

	private int getDisplayMetrics(Context context){
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

	//进入分类界面
	public void GotoCategoriesView(){
		Intent intent = new Intent();
		intent.setClass(this, TypeActivity.class);
		
		Bundle bundle = new Bundle();
		String s = String.valueOf(PushTabBtnIndex);
		String s1 = (new StringBuilder(s)).toString();
		bundle.putString("FROME_PAGE", s1);
		
		intent.putExtras(bundle);
		startActivity(intent);
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

	public void InitSlider(){
		int i = 1;
		float f = 0F;
		int j = 1;
		int k = 1;
		float f1 = 0F;
		TranslateAnimation translateanimation = new TranslateAnimation(1, 0F, i, f, j, 1F, k, f1);
		showAction = translateanimation;
		showAction.setDuration(500L);
	}

	//初始化顶部tab
	public void InitTabUp(){
		listPageBean = new ListPagesBean();

		//查找的listview
		searchListView = (ListView)findViewById(R.id.list_search);
		//listvie的点击事件监听器
		searchListView.setOnItemClickListener(this);

		
		//优酷的标题以及图标
		title_tab_youku_1 = (TextView)findViewById(R.id.tab_btn_youku_1);
		title_tab_youku = (ImageView)findViewById(R.id.tab_btn_youku);
		title_tab_youku.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				ChangeTopTabbarImage(1);
				Search();
			}
		});
		
		//土豆标题以及图标
		title_tab_tudou_1 = (TextView)findViewById(R.id.tab_btn_tudou_1);
		 title_tab_tudou = (ImageView)findViewById(R.id.tab_btn_tudou);
		title_tab_tudou.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				ChangeTopTabbarImage(2);
				Search();
			}
		});
		
		//酷六标题以及图标
		title_tab_ku6_1 = (TextView)findViewById(R.id.tab_btn_ku6_1);
		title_tab_ku6 = (ImageView)findViewById(R.id.tab_btn_ku6);
		title_tab_ku6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				ChangeTopTabbarImage(3);
				Search();
			}
		});
		
		//6间房标题以及图标
		title_tab_jianfang6_1 = (TextView)findViewById(R.id.tab_btn_jianfang6_1);
		title_tab_jianfang6 = (ImageView)findViewById(R.id.tab_btn_jianfang6);
		title_tab_jianfang6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				ChangeTopTabbarImage(4);
				Search();
			}
		});
		
		//新浪标题以及图标
		title_tab_sina_1 = (TextView)findViewById(R.id.tab_btn_sina_1);
		title_tab_sina = (ImageView)findViewById(R.id.tab_btn_sina);
		title_tab_sina.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				ChangeTopTabbarImage(5);
				Search();
			}
		});
		
		//查找的输入框
		search_box = (EditText)findViewById(R.id.search_text);
		//查找按钮
		SearchButton = (ImageView)findViewById(R.id.search_btn);
		SearchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				Search();
			}

		});
	}

	//初始化底部视图
	public void IntiTabView(){
		
		//分类导航图片控件
		category_navigation = (ImageView)findViewById(R.id.category_navigation);
		//分类导航图片控件点击事件监听器
		category_navigation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (FromePage == 1){
					finish();
				}
				else{
					GotoCategoriesView();
				}
			}

		});
		
		//今日最新图片控件
		new_video = (ImageView)findViewById(R.id.new_video);
		//今日最新图片控件点击事件监听器
		new_video.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (FromePage == 2){
					finish();
				}
				else{
					GotoNewEst();
				}
			}

		});
		
		//视频搜索图片控件
		video_search = (ImageView)findViewById(R.id.video_search);
		//视频搜索图片控件的点击事件监听器
		video_search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				HttpInit();
			}
		});
		
		//收藏图片控件
		video_collect = (ImageView)findViewById(R.id.video_collect);
		//收藏图片控件点击事件监听器
		video_collect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (FromePage == 4){
					finish();
				}
				else{
					GotoCollectView();
				}
			}
		});
		
		
		
	}


	//数据更新函数
	public void NotifyDataAnalysisFinished()
	{
		int type = TypeOfListView;
		if (1 != type) {
			if (2 == type){
				ListViewDetailAdapter listviewdetailadapter = getAdapter2();
				searchListView.setAdapter(listviewdetailadapter);
			}

		}else {
			Toast toast;
			if (dataStruct != null && searchListView != null){
				ListViewSearchAdapter listviewsearchadapter = getAdapter();
				searchListView.setAdapter(listviewsearchadapter);
			} else{
				toast = Toast.makeText(this, "ItemsListView empty", 0);
			}
		}
	}

	public void RegexRespone(String s, String s1)
	{
		int i = TypeOfListView;
		boolean flag;
		if (1 == i)
			flag = dataStruct.add(s);
	}

	public void RegexRespone(String s, String s1, String s2, String s3)
	{
		ListItemBean listitembean = new ListItemBean();
		listitembean.setVideoHref(s);
		listitembean.setVideoImgSrc(s3);
		listitembean.setVideoTitle(s1);
		listitembean.setVideoLength(s2);
		boolean flag = dataStruct2.add(listitembean);
	}

	//响应数据
	public void ResponseData(String s){
		if (!s.contains("socket_error")) {
			dataStruct.clear();
			dataStruct2.clear();
			int type = TypeOfListView;
			Toast toast;
			if (1 == type){
				if (RegexAnalysis != null){
					RegexAnalysis.RegexAnalysisSearchKeywords(s);
				}
				else{
					toast = Toast.makeText(this, "Regex empty", 0);
				}
			} else{
				Toast toast1;
				if (2 == type){
					if (RegexAnalysis != null){
						RegexAnalysis.RegexAnalysisListItme(s);
					}
					else{
						toast1 = Toast.makeText(this, "Regex empty", 0);
					}
				}
			}
		}else{
			CloseWaitDialog();
		}
		
		return;
	}

	//查找函数
	public void Search(){
		String search = "";
		String s1 = "";
		search = search_box.getText().toString();
		
		try {
			if (search == null) {
				Toast toast = Toast.makeText(this, "Keywords is empty", 0);
				return;
			}
			s1 = URLEncoder.encode(search, "UTF-8");
			StringBuilder stringbuilder = new StringBuilder(
					"http://m.vfun.tv:8080/webparser/so_");
			String s3 = SearchWebsite;
			String s4 = stringbuilder.append(s3).append(".php?title=")
					.append(s1).append("&index=").append("1").append("&o=1")
					.toString();
			TypeOfListView = 2;
			AndroidHttp androidhttp = new AndroidHttp(this, this);
			Object aobj[] = new Object[1];
			aobj[0] = s4;
			android.os.AsyncTask asynctask = androidhttp.execute(aobj);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
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
		//设置当前使用的布局文件
		setContentView(R.layout.search);
		ct = this;
		callBack = this;
		
		FromePage =  Integer.parseInt(getIntent().getExtras().getString("FROME_PAGE"));
		GotoUrl = "http://m.vfun.tv:8080/webparser/searchhot.php";
		
		//设置屏幕的显示模式
		SetScreenOnFlag();
		//初始化顶部的tab视图
		InitTabUp();
		//初始化底部的tab视图
		IntiTabView();
		//设置默认的焦点tab
		DefaultFocusedTab();
		
		RegexAnalysis = new RegexTool(this, this);
		
		//http初始化
		HttpInit();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	protected void onDestroy()
	{
		super.onDestroy();
		finish();
	}

	//listview的点击事件
	public void onItemClick(AdapterView adapterview, View view, int i, long l){
		int type = TypeOfListView;
		
		if (1 == type){
			String key = (String)searchListView.getItemAtPosition(i);
			String url = "";
			String s = "";
			
			try {
				StringBuilder stringbuilder;
				Object aobj[];
				android.os.AsyncTask asynctask;
				if (key != null) {
					search_box.setText(key);
					search_box.invalidate();
				} else {
					SearchActivity searchactivity2 = this;
					String s5 = "Keywords is empty";
					int l1 = 0;
					Toast toast = Toast.makeText(searchactivity2, s5, l1);
				}
				//搜索函数
				Search();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}else if(type == 2){
				ListItemBean listitembean = (ListItemBean)searchListView.getItemAtPosition(i);
				String video_href = listitembean.getVideoHref();
				String viedo_length = listitembean.getVideoLength();
				String video_imgsrc = listitembean.getVideoImgSrc();
				String video_titile = listitembean.getVideoTitle();
				
				
				Intent intent = new Intent();
				intent.setClass(this,  TypeActivityItemDetail.class);
				
				Bundle bundle = new Bundle();
				bundle.putString("REQUEST_URL", video_href);
				bundle.putString("TITLE", video_titile);
				bundle.putString("VIDEO_LEN", viedo_length);
				bundle.putString("IMAGE_SRC", video_imgsrc);
				String s10 = String.valueOf(PushTabBtnIndex);
				String FROME_PAGE = (new StringBuilder(s10)).toString();
				bundle.putString("FROME_PAGE", FROME_PAGE);
				
				intent.putExtras(bundle);

				this.startActivity(intent);
		}
	}

	public boolean onKeyDown(int i, KeyEvent keyevent)
	{
		if (i == 4 && keyevent.getRepeatCount() == 0)
		{
			Intent intent = new Intent();
			setResult(3, intent);
			finish();
		}
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem)
	{
		return super.onOptionsItemSelected(menuitem);
	}

	protected void onPause()
	{
		super.onPause();
	}

	public boolean onSearchRequested()
	{
		return false;
	}

	protected void onStop()
	{
		super.onStop();
	}

	public void searchClick(View view)
	{
		boolean flag = onSearchRequested();
	}
	

}
