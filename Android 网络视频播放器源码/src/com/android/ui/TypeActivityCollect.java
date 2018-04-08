
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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


import java.util.ArrayList;

import com.android.datastore.DatabaseManager;
import com.android.datastruct.ListItemBean;
import com.android.tools.RemoveCallback;
import com.sph.player.R;

//收藏页面
public class TypeActivityCollect extends Activity
	implements android.widget.AdapterView.OnItemClickListener, RemoveCallback{

	private boolean EditMode_ItemClick_UnResponsable;
	private int FromePage;
	private ListView ItemsListView;
	private ListViewCollectAdapter LVCAdapter;
	private int PushTabBtnIndex;
	private ImageView category_navigation;
	private TextView category_navigation_text;
	private DatabaseManager dbManager;
	private ImageView edit_btn;
	private ImageView new_video;
	private TextView new_video_text;
	private ImageView video_collect;
	private TextView video_collect_text;
	private ImageView video_more;
	private TextView video_more_text;
	private ImageView video_search;
	private TextView video_search_text;

	//构造函数
	public TypeActivityCollect(){
		PushTabBtnIndex = 4;
		FromePage = 0;
		LVCAdapter = null;
		EditMode_ItemClick_UnResponsable = true;
		category_navigation = null;
		new_video = null;
		video_search = null;
		video_collect = null;
		video_more = null;
		edit_btn = null;
		category_navigation_text = null;
		new_video_text = null;
		video_search_text = null;
		video_collect_text = null;
		video_more_text = null;
		dbManager = null;
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

	//初始化函数
	private void Init(){
		if (dbManager == null){
			dbManager = DatabaseManager.getInstance(this);
		}
		
		ArrayList arraylist = dbManager.SelectAll();
		Toast toast;
		if (arraylist != null){
			if (arraylist != null && ItemsListView != null){
				ListViewCollectAdapter listviewcollectadapter = getAdapter(arraylist);
				ItemsListView.setAdapter(listviewcollectadapter);
			} 
		}else{
			toast = Toast.makeText(this, "Collect videos empty", 0);
		}
	}

	//初始化list数据
	private void InitListData(){
		ItemsListView = (ListView)findViewById(R.id.list_detail_views);
		ItemsListView.setOnItemClickListener(this);
	}

	
	//获取listview的数据适配器
	private ListViewCollectAdapter getAdapter(ArrayList arraylist){
		if (LVCAdapter == null){
			LVCAdapter = new ListViewCollectAdapter(this, arraylist, this);
			LVCAdapter.setNotifyOnChange(true);
		} else{
			LVCAdapter = new ListViewCollectAdapter(this, arraylist, this);
		}
		return LVCAdapter;
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
	
	//初始化tabview
	public void IntiTabView(){
		
		edit_btn = (ImageView)findViewById(R.id.my_edit_button);
		edit_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (LVCAdapter == null) {
					LVCAdapter.notifyDataSetInvalidated();
				} else {
					if (LVCAdapter.getEditModle()){
						if (LVCAdapter.getEditModle()){
							EditMode_ItemClick_UnResponsable = true;
							LVCAdapter.setEditModle(false);
						}
					}else{
						EditMode_ItemClick_UnResponsable = false;
						LVCAdapter.setEditModle(true);
					}
					LVCAdapter.notifyDataSetInvalidated();
				}
			}
		});
		
		
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
				if (FromePage == 3){
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

			}
		});
		
		//初始化当前焦点
		DefaultFocusedTab();
		
	}

	public int RemoveCollectItemBean(ListItemBean listitembean)
	{
		String s = listitembean.getVideoHref();
		DatabaseManager.getInstance(this).removeMedia(s);
		LVCAdapter.remove(listitembean);
		LVCAdapter.notifyDataSetChanged();
		LVCAdapter.notifyDataSetInvalidated();
		return 0;
	}

	//activity的创建函数
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		//设置当前的布局文件
		setContentView(R.layout.type_activity_collect);
		
		FromePage = Integer.parseInt(getIntent().getExtras().getString("FROME_PAGE"));
		
		//初始化list数据
		InitListData();
		//初始化底部视图
		IntiTabView();
		//初始化函数
		Init();
	}

	//listview的点击事件接口函数
	public void onItemClick(AdapterView adapterview, View view, int i, long l){
		if (!EditMode_ItemClick_UnResponsable){
			LVCAdapter.setSelectItem(i);
			LVCAdapter.notifyDataSetInvalidated();
		} else{
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
				toast = Toast.makeText(this, "CollectItems is empty", 0);
			}
		}
	}

}
