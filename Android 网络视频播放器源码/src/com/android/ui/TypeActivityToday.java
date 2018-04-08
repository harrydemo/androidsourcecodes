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
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

import com.android.datastruct.ListItemBean;
import com.android.tools.AndroidHttp;
import com.android.tools.HttpResponseCallback;
import com.android.tools.MyDialog;
import com.android.tools.RegexCallback;
import com.android.tools.RegexTool;
import com.sph.player.R;

//今日最新的页面
public class TypeActivityToday extends Activity
	implements HttpResponseCallback, RegexCallback, 
		android.widget.AdapterView.OnItemClickListener{

	private int Fromepage;
	private ListView ItemsListView;
	private ListViewDetailAdapter LVDAdapter;
	private int PushTabBtnIndex;
	private RegexTool RegexAnalysis;
	private String RequestUrl;
	TypeActivityToday callBack;
	private ImageView category_navigation;
	private TextView category_navigation_text;
	Context ct;
	private ArrayList dataStruct;
	MyDialog dialog;
	private ImageView new_video;
	private TextView new_video_text;
	private ImageView video_collect;
	private TextView video_collect_text;
	private ImageView video_more;
	private TextView video_more_text;
	private ImageView video_search;
	private TextView video_search_text;

	//构造函数
	public TypeActivityToday(){
		RequestUrl = "http://m.vfun.tv:8080/webparser/newest.php";
		LVDAdapter = null;
		dataStruct = null;
		PushTabBtnIndex = 2;
		Fromepage = 0;
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
		MyDialog mydialog = new MyDialog(this);
		dialog = mydialog;
		ct = null;
		callBack = null;
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

	//http初始化函数
	private void HttpInit(){
		//android的http通信类
		AndroidHttp androidhttp = new AndroidHttp(this, this);
		Object aobj[] = new Object[1];
		aobj[0] = RequestUrl;

		//轻量级线程执行
		android.os.AsyncTask asynctask = androidhttp.execute(aobj);
	}

	//初始化函数
	private void Init(){
		RegexAnalysis = new RegexTool(this, this);

		//http初始化
		HttpInit();
	}

	//初始化list数据函数
	private void InitListData(){
		Fromepage = Integer.parseInt(getIntent().getExtras().getString("FROME_PAGE"));
		//创建数据源对象
		dataStruct = new ArrayList();
		//获取listview对象
		ItemsListView = (ListView)findViewById(R.id.list_detail_views);
		//设置listview的点击事件监听器
		ItemsListView.setOnItemClickListener(this);
	}

	private ListViewDetailAdapter getAdapter()
	{
		if (LVDAdapter == null)
		{
			ArrayList arraylist = dataStruct;
			ListViewDetailAdapter listviewdetailadapter = new ListViewDetailAdapter(this, arraylist);
			LVDAdapter = listviewdetailadapter;
		} else
		{
			ArrayList arraylist1 = dataStruct;
			ListViewDetailAdapter listviewdetailadapter1 = new ListViewDetailAdapter(this, arraylist1);
			LVDAdapter = listviewdetailadapter1;
		}
		return LVDAdapter;
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
	
	
	//初始化底部视频
	public void IntiTabView(){
		
		//分类导航图片控件
		category_navigation = (ImageView)findViewById(R.id.category_navigation);
		//分类导航图片控件点击事件监听器
		category_navigation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (Fromepage == 1){
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
				if (Fromepage == 2){
					finish();
				}
				else{
					HttpInit();
				}
			}

		});
		
		//视频搜索图片控件
		video_search = (ImageView)findViewById(R.id.video_search);
		//视频搜索图片控件的点击事件监听器
		video_search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (Fromepage == 3){
					finish();
				}
				else{
					GotoSearchView();
				}
			}
		});
		
		//收藏图片控件
		video_collect = (ImageView)findViewById(R.id.video_collect);
		//收藏图片控件点击事件监听器
		video_collect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (Fromepage == 4){
					finish();
				}
				else{
					GotoCollectView();
				}
			}
		});
		
	}

	//数据更新
	public void NotifyDataAnalysisFinished(){
		Toast toast;
		if (dataStruct != null && ItemsListView != null){
			ListViewDetailAdapter listviewdetailadapter = getAdapter();
			ItemsListView.setAdapter(listviewdetailadapter);
		} else{
			toast = Toast.makeText(this, "ItemsListView empty", 0);
		}
	}

	public void RegexRespone(String s, String s1){
		
	}

	//数据相应函数
	public void RegexRespone(String s, String s1, String s2, String s3){
		ListItemBean listitembean = new ListItemBean();
		listitembean.setVideoHref(s);
		listitembean.setVideoTitle(s1);
		listitembean.setVideoLength(s2);
		listitembean.setVideoImgSrc(s3);
		boolean flag = dataStruct.add(listitembean);
	}

	//数据响应函数
	public void ResponseData(String s){
		Toast toast;
		if (s.contains("socket_error")){
			CloseWaitDialog();
		}
		else{
			if (RegexAnalysis != null){
				dataStruct.clear();
				RegexAnalysis.RegexAnalysisListItme(s);
			} else{
				toast = Toast.makeText(this, "Regex empty", 0);
			}
		}
	}

	protected void onActivityResult(int i, int j, Intent intent)
	{
		super.onActivityResult(i, j, intent);
		if (j == 2)
			finish();
	}

	//activity的创建函数
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		setContentView(R.layout.type_activity_newest);
		ct = this;
		callBack = this;
		
		//初始化list数据
		InitListData();
		//初始化底部tab视图
		IntiTabView();

		//默认tab的焦点
		DefaultFocusedTab();
		
		//初始化
		Init();
	}

	//listview的点击事件接口函数
	public void onItemClick(AdapterView adapterview, View view, int i, long l){
		ListItemBean listitembean = (ListItemBean)ItemsListView.getItemAtPosition(i);
		Toast toast;
		if (listitembean != null){
			String video_href = listitembean.getVideoHref();
			String viedo_length = listitembean.getVideoLength();
			String video_imgsrc = listitembean.getVideoImgSrc();
			String video_titile = listitembean.getVideoTitle();
			
			Intent intent = new Intent();
			intent.setClass(this, TypeActivityItemDetail.class);
			
			Bundle bundle = new Bundle();
			bundle.putString("REQUEST_URL", video_href);
			bundle.putString("TITLE", video_titile);
			bundle.putString("VIDEO_LEN", viedo_length);
			bundle.putString("IMAGE_SRC", video_imgsrc);
			String s4 = String.valueOf(PushTabBtnIndex);
			String s5 = (new StringBuilder(s4)).toString();
			bundle.putString("FROME_PAGE", s5);
			
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		} else{
			toast = Toast.makeText(this, "ListItemBean is empty", 0);
		}
	}

}
