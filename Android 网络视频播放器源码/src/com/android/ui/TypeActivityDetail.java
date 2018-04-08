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
import java.util.ArrayList;

import com.android.datastruct.ListItemBean;
import com.android.datastruct.ListPagesBean;
import com.android.datastruct.PageBean;
import com.android.tools.AndroidHttp;
import com.android.tools.HttpResponseCallback;
import com.android.tools.MyDialog;
import com.android.tools.RegexCallback;
import com.android.tools.RegexTool;
import com.sph.player.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/*
 * 分类导航的详情页面
 * */
public class TypeActivityDetail extends Activity
	implements HttpResponseCallback, RegexCallback, 
		android.widget.AdapterView.OnItemClickListener{

	private String FirstLoadPageUrl;
	private String GotoUrl;
	private ListView ItemsListView;
	private ListViewDetailAdapter LVDAdapter;
	private int PushTabBtnIndex;
	private RegexTool RegexAnalysis;
	private String Title;
	private TextView TitleLiteral;
	private boolean addFirstUrlFlag;
	private TextView allPages;
	private ImageView backPageBtn;
	TypeActivityDetail callBack;
	private ImageView category_navigation;
	private TextView category_navigation_text;
	Context ct;
	private TextView currentPageIndex;
	private ArrayList dataStruct;
	MyDialog dialog;
	private ListPagesBean listPageBean;
	private ImageView new_video;
	private ImageView nextPageBtn;
	private boolean pushFlag;
	private ImageView return_btn;
	private ImageView video_collect;
	private ImageView video_more;
	private ImageView video_search;

	//构造函数--完成相关变量的初始化
	public TypeActivityDetail(){
		Title = "";
		GotoUrl = "";
		FirstLoadPageUrl = "";
		TitleLiteral = null;
		return_btn = null;
		PushTabBtnIndex = 1;
		category_navigation = null;
		new_video = null;
		video_search = null;
		video_collect = null;
		video_more = null;
		category_navigation_text = null;
		LVDAdapter = null;
		dataStruct = null;
		currentPageIndex = null;
		allPages = null;
		nextPageBtn = null;
		backPageBtn = null;
		listPageBean = null;
		pushFlag = false;
		addFirstUrlFlag = false;
		MyDialog mydialog = new MyDialog(this);
		dialog = mydialog;
		ct = null;
		callBack = null;
	}

	//更改当前页索引
	private void ChangePageIndex(){
		int i = listPageBean.GetIndex();
		int j = listPageBean.GetListLength();
		
		String s = String.valueOf(i + 1);
		String s1 = (new StringBuilder(s)).toString();
		currentPageIndex.setText(s1);
		
		String s2 = (new StringBuilder("/")).append(j).toString();
		allPages.setText(s2);
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

	//http初始化
	private void HttpInit(){
		//创建Android的Http对象
		AndroidHttp androidhttp = new AndroidHttp(this, this);
		Object aobj[] = new Object[1];
		aobj[0] = GotoUrl;

		//Asynctask--轻量级线程执行
		android.os.AsyncTask asynctask = androidhttp.execute(aobj);
	}

	//初始化函数
	private void Init(){
		//标题
		TitleLiteral = (TextView)findViewById(R.id.click_title);
		TitleLiteral.setText(Title);
		TitleLiteral.invalidate();
		
		//返回按钮的图片控件
		return_btn = (ImageView)findViewById(R.id.return_btn);
		//返回按钮的点击事件监听器
		return_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				finish();
			}
		});
		
		//当前页显示控件
		currentPageIndex = (TextView)findViewById(R.id.page_current);
		//总页数显示控件
		allPages = (TextView)findViewById(R.id.page_alls);
		
		//下一页图片显示控件
		nextPageBtn = (ImageView)findViewById(R.id.next_page_btn);
		nextPageBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view){
				if (pushFlag){
					PageBean pagebean = listPageBean.GetNextPageBean();
					//更改当前页
					ChangePageIndex();
					//数据对象清楚
					dataStruct.clear();
					
					StringBuilder stringbuilder = new StringBuilder("http://m.vfun.tv:8080/webparser/");
					String s = pagebean.GetUrl();
					String s1 = stringbuilder.append(s).toString();
					Context context = ct;
					TypeActivityDetail typeactivitydetail = callBack;
					AndroidHttp androidhttp = new AndroidHttp(context, typeactivitydetail);
					Object aobj[] = new Object[1];
					aobj[0] = s1;
					android.os.AsyncTask asynctask = androidhttp.execute(aobj);
				}
			}
		});
		
		//上一页图片显示控件
		backPageBtn = (ImageView)findViewById(R.id.back_page_btn);
		////上一页图片显示控件点击事件监听器
		backPageBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (pushFlag){
					PageBean pagebean = listPageBean.GetBackPageBean();
					ChangePageIndex();
					dataStruct.clear();
					StringBuilder stringbuilder = new StringBuilder("http://m.vfun.tv:8080/webparser/");
					String s = pagebean.GetUrl();
					String s1 = stringbuilder.append(s).toString();
					Context context = ct;
					TypeActivityDetail typeactivitydetail = callBack;
					AndroidHttp androidhttp = new AndroidHttp(context, typeactivitydetail);
					Object aobj[] = new Object[1];
					aobj[0] = s1;
					android.os.AsyncTask asynctask = androidhttp.execute(aobj);
				}
			}
		});
		
		
		//创建数据页对象
		listPageBean = new ListPagesBean();
		RegexAnalysis = new RegexTool(this, this);
		
		//初始化底部tab页面
		IntiTabView();
		//http初始化
		HttpInit();
	}

	//初始化list数据
	private void InitListData(){
		//创建数组list数据对象
		dataStruct = new ArrayList();

		//获取listview对象
		ItemsListView = (ListView)findViewById(R.id.list_detail_views);
		//listview点击事件监听器
		ItemsListView.setOnItemClickListener(this);
	}

	private void ResetData()
	{
		addFirstUrlFlag = false;
	}

	//获取listview的数据适配器
	private ListViewDetailAdapter getAdapter(){
		//判断数据适配器是否为空
		if (LVDAdapter == null){
			//创建数据适配器
			LVDAdapter = new ListViewDetailAdapter(this, dataStruct);
		} else{
			//创建数据适配器
			LVDAdapter = new ListViewDetailAdapter(this, dataStruct);
		}
		return LVDAdapter;
	}

	public void CloseWaitDialog()
	{
		dialog.ShowDialog();
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

	//初始化底部视图
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

	//数据解析好后，调用的函数
	public void NotifyDataAnalysisFinished(){
		Toast toast;
		
		if (dataStruct != null && ItemsListView != null){
			//获取数据适配器
			ListViewDetailAdapter listviewdetailadapter = getAdapter();
			//设置listview的数据适配器
			ItemsListView.setAdapter(listviewdetailadapter);
			//更改当前索引页
			ChangePageIndex();
			pushFlag = true;
		} else{
			toast = Toast.makeText(this, "ItemsListView empty", 0);
		}
	}

	
	//页面响应函数
	public void RegexRespone(String Url, String Page){
		if (!addFirstUrlFlag){
			addFirstUrlFlag = true;
			listPageBean.Add(FirstLoadPageUrl, "第1页");
		}
		
		if (listPageBean != null){
			listPageBean.Add(Url, Page);
		}
			
	}

	//页面响应函数
	public void RegexRespone(String s, String s1, String s2, String s3){
		ListItemBean listitembean = new ListItemBean();
		listitembean.setVideoHref(s);
		listitembean.setVideoTitle(s1);
		listitembean.setVideoLength(s2);
		listitembean.setVideoImgSrc(s3);
		boolean flag = dataStruct.add(listitembean);
	}

	//相应数据的方法
	public void ResponseData(String s){
		Toast toast;
		//关闭对话框
		if (s.contains("socket_error")){
			CloseWaitDialog();
		}
		else{
			if (RegexAnalysis != null){
				RegexAnalysis.RegexAnalysisListItme(s);
			}
			else{
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

	/*
	 * activity的创建函数
	 * */
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		//设置布局文件
		setContentView(R.layout.type_activity_detail);

		//获取通过intent传递的数据，url和标题参数
		Bundle bundle1 = getIntent().getExtras();
		GotoUrl = bundle1.getString("REQUEST_URL");
		Title = bundle1.getString("TITLE");
		

		//重组第一次加载页面的URL
		String as[] = GotoUrl.split("webparser/");
		int i = as.length - 1;
		FirstLoadPageUrl = as[i];
		String s3 = String.valueOf(FirstLoadPageUrl);
		FirstLoadPageUrl = (new StringBuilder(s3)).append("&o=1").toString();
		System.out.println("FirstLoadPageUrl = "+FirstLoadPageUrl);

		//相关变量赋值
		ct = this;
		callBack = this;
		
		//初始化list数据
		InitListData();
		
		//初始化
		Init();
	}

	protected void onDestroy()
	{
		super.onDestroy();
		ResetData();
	}

	//listview的点击接口函数--进入视频详情页面
	public void onItemClick(AdapterView adapterview, View view, int i, long l){
		//获取点击的数据项
		ListItemBean listitembean = (ListItemBean)ItemsListView.getItemAtPosition(i);
		String request_url = listitembean.getVideoHref();
		String video_len = listitembean.getVideoLength();
		String image_src = listitembean.getVideoImgSrc();
		String title = listitembean.getVideoTitle();
		
		Toast toast;
		if (listitembean == null){
			toast = Toast.makeText(this, "ListItemBean is empty", 0);
		}else{
			//通过Intent跳转进入视频详情页
			Intent intent = new Intent();
			Intent intent1 = intent.setClass(this, TypeActivityItemDetail.class);
			
			//创建数据对象
			Bundle bundle = new Bundle();
			bundle.putString("REQUEST_URL", request_url);
			bundle.putString("TITLE", title);
			bundle.putString("VIDEO_LEN", video_len);
			bundle.putString("IMAGE_SRC", image_src);
			
			String s4 = String.valueOf(PushTabBtnIndex);
			String s5 = (new StringBuilder(s4)).toString();
			bundle.putString("FROME_PAGE", s5);
			
			//intent设置携带的数据
			intent.putExtras(bundle);
			//进入视频详情页
			startActivityForResult(intent, 1);
		}
	
	}
}
