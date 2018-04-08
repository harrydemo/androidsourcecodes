
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tools.CDataStruct;
import com.android.tools.Configer;
import com.android.tools.RelativeCallback;
import com.sph.player.R;
import com.sph.player.player.util.ScrollLayout;


//主界面
public class TypeActivity extends Activity
	implements  RelativeCallback{

	public static final String TAG = "SPH/TypeActivity";
	private static String desc[];
	private static String jianfang6Menu[];
	private static String ku6Menu[];
	private static String sinaMenu[];
	private static String tudouMenu[];
	private static String youkuMenu[];
	private String DefaultIndex;
	private String DefaultWebsite;
	private int FromePage;
	private int PushTabBtnIndex;
	private Activity TypeActivityContext;
	RelativeCallback aTA;
	ListViewCustomAdapter adapter;
	String bandwidth;
	private CDataStruct categorizeList;
	ImageView category_navigation;
	TextView category_navigation_text;
	private TranslateAnimation hideAction;
	private ImageView jianfang6;
	private ImageView ku6;
	protected Handler mHandler;
	ImageView new_video;
	TextView new_video_text;
	ListView rightListView;
	private TranslateAnimation showAction;
	private ImageView sina;
	TypeActivity thisPointer;
	private TextView title;
	private ImageView tudou;
	ImageView video_collect;
	TextView video_collect_text;
	ImageView video_more;
	TextView video_more_text;
	ImageView video_search;
	TextView video_search_text;
	private ImageView youku;
	
	private ScrollLayout mScrollLayout;
	private ListViewCustomAdapter mListViewCustomAdapter;
	private ImageView mCategoryImage;
	public static TypeActivity instance = null;
	
	//构造函数
	public TypeActivity(){
		youku = null;
		tudou = null;
		ku6 = null;
		jianfang6 = null;
		sina = null;
		TypeActivityContext = null;
		title = null;
		showAction = null;
		hideAction = null;
		categorizeList = null;
		PushTabBtnIndex = 1;
		FromePage = 1;
		thisPointer = null;
		DefaultWebsite = "youku";
		DefaultIndex = "1";
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
		bandwidth = "";
		mHandler = new Handler();
	}

	public void HandlerItemCLick(int i){
		String des = "";
		String vTitle = "";
		
		if (categorizeList != null){
			if(DefaultWebsite.equals("youku")){
				//Map数据类型hashList对象
				if (categorizeList != null){
					vTitle = youkuMenu[i];
					//获取对应标题的数据
					 des = categorizeList.GetPosition(vTitle);
				} 
				
			}else if(DefaultWebsite.equals("tudou")){
				//Map数据类型hashList对象
				if (categorizeList != null){
					 vTitle = tudouMenu[i];
					
					//获取对应标题的数据
					 des = categorizeList.GetPosition(vTitle);
				} 
				
			}else if(DefaultWebsite.equals("ku6")){
				//Map数据类型hashList对象
				if (categorizeList != null){
					 vTitle = ku6Menu[i];
					
					//获取对应标题的数据
					 des = categorizeList.GetPosition(vTitle);
				} 
				
			}else if(DefaultWebsite.equals("6")){
				//Map数据类型hashList对象
				if (categorizeList != null){
					 vTitle = jianfang6Menu[i];
					
					//获取对应标题的数据
					 des = categorizeList.GetPosition(vTitle);
				} 
				
			}else if(DefaultWebsite.equals("sina")){
				//Map数据类型hashList对象
				if (categorizeList != null){
					 vTitle = sinaMenu[i];
					
					//获取对应标题的数据
					 des = categorizeList.GetPosition(vTitle);
				} 
				
			}
			
			if (des == null){
				Toast.makeText(this, "数据查询出错", 0).show();
			}else{
				//页面索引PageIndex
				if (DefaultIndex != null && DefaultIndex.length() > 0){
					StringBuilder stringbuilder = new StringBuilder("http://m.vfun.tv:8080/webparser/");
					StringBuilder stringbuilder1 = stringbuilder.append(DefaultWebsite).append("_category.php?c=").append(des).append("&index=");
					String Url = stringbuilder1.append(DefaultIndex).toString();
					if (vTitle == null){
						Toast.makeText(this, "数组内容查询失败", 0).show();
					}
					else{
						//进入到详情view
						aTA.GotoDetailView(Url, vTitle);
					}
				} else{
					String Url = (new StringBuilder("http://m.vfun.tv:8080/webparser/get_live.php?vid=")).append(des).toString();
					//进入到详情view
					aTA.GotoDetailView(Url, vTitle);
				}
			}
		}else{
			Toast.makeText(this, "数据HashMap为null", 0).show();
		}
	}
	
	//更新分类
	public void UpdateCategory(int curScreen, String flag){
		System.out.println("curScreen = "+curScreen +" flag = "+flag);
		
		if(flag.equals("left")){
			//6间房
			if(curScreen == 2){
				mCategoryImage.setImageResource(R.drawable.jianfang6);
			}
			//ku6
			else if(curScreen == 1){
				mCategoryImage.setImageResource(R.drawable.ku6);
			}
			//tudou
			else if(curScreen == 0){
				mCategoryImage.setImageResource(R.drawable.tudou);
			}
			//youku
			else if(curScreen == -1){
				mCategoryImage.setImageResource(R.drawable.youku);
			} 
		}else{
			//tudou
			if(curScreen == 2){
				mCategoryImage.setImageResource(R.drawable.tudou);
			}
			//ku6
			else if(curScreen == 3){
				mCategoryImage.setImageResource(R.drawable.ku6);
			}
			//6房间
			else if(curScreen == 4){
				mCategoryImage.setImageResource(R.drawable.jianfang6);
			}
			//新浪视频
			else if(curScreen == 5){
				mCategoryImage.setImageResource(R.drawable.sina);
			}
		}
	}
	
	//初始化函数
	private void Init(){
		//当前分类
		instance = this;
		mCategoryImage = (ImageView)findViewById(R.id.category_image);
		mCategoryImage.setImageResource(R.drawable.youku);
		
		//键值数据数组
		String as[] = Configer.KeyArray;
		String as1[] = Configer.ValueArray;
		
		//CD数据结构，分类列表数据
		categorizeList = new CDataStruct(as, as1);
		thisPointer = this;
		TypeActivityContext = this;
		aTA = this;
		

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // the total pages
        final int PageCount = 5;
        for (int i=0; i<PageCount; i++) {
        	final GridView webPage = new GridView(this);
        	if(i == 0){
        		mListViewCustomAdapter = new 
        			ListViewCustomAdapter(TypeActivity.this,  TypeActivity.youkuMenu,
        					TypeActivity.desc);
        		
        		mListViewCustomAdapter.setHashList(categorizeList, DefaultWebsite, 
        				DefaultIndex, aTA);
        		webPage.setAdapter(mListViewCustomAdapter);
        		
        		webPage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	        		//listview中点击事件接口函数
	            	public void onItemClick(AdapterView adapterview, View view, int i, long l){
            				DefaultWebsite = "youku";
            				HandlerItemCLick(i);

	            	}
        		} );
        		
        	}else if(i == 1){
        		mListViewCustomAdapter = new 
    			ListViewCustomAdapter(TypeActivity.this,  TypeActivity.tudouMenu, TypeActivity.desc);
    		
        		mListViewCustomAdapter.setHashList(categorizeList, DefaultWebsite, 
    				DefaultIndex, aTA);
        		webPage.setAdapter(mListViewCustomAdapter);
        		webPage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	        		//listview中点击事件接口函数
	            	public void onItemClick(AdapterView adapterview, View view, int i, long l){
	            			DefaultWebsite = "tudou";
	            			HandlerItemCLick(i);
	            	}
        		} );
        		
        	}else if(i == 2){
        		mListViewCustomAdapter = new 
    			ListViewCustomAdapter(TypeActivity.this,  TypeActivity.ku6Menu, TypeActivity.desc);
    		
        		mListViewCustomAdapter.setHashList(categorizeList, DefaultWebsite, 
    				DefaultIndex, aTA);
        		webPage.setAdapter(mListViewCustomAdapter);
        		webPage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	        		//listview中点击事件接口函数
	            	public void onItemClick(AdapterView adapterview, View view, int i, long l){
	            			DefaultWebsite = "ku6";
	            			HandlerItemCLick(i);
	            	}
        		} );
        	}else if(i == 3){
        	    mListViewCustomAdapter = new 
    			ListViewCustomAdapter(TypeActivity.this, TypeActivity.jianfang6Menu, TypeActivity.desc);
    		
        		mListViewCustomAdapter.setHashList(categorizeList, DefaultWebsite, 
    				DefaultIndex, aTA);
        		webPage.setAdapter(mListViewCustomAdapter);
        		
        		webPage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	        		//listview中点击事件接口函数
	            	public void onItemClick(AdapterView adapterview, View view, int i, long l){
	            			DefaultWebsite = "6";
	            			HandlerItemCLick(i);
	            	}
        		} );
        	}else if(i == 4){
        		mListViewCustomAdapter = new 
    			ListViewCustomAdapter(TypeActivity.this,  TypeActivity.sinaMenu, TypeActivity.desc);
    		
        		mListViewCustomAdapter.setHashList(categorizeList, DefaultWebsite, 
    				DefaultIndex, aTA);
        		webPage.setAdapter(mListViewCustomAdapter);
        		webPage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	        		//listview中点击事件接口函数
	            	public void onItemClick(AdapterView adapterview, View view, int i, long l){
	            			DefaultWebsite = "sina";
	            			HandlerItemCLick(i);
	            	}
        		} );
        	}
        	webPage.setNumColumns(3);
        	mScrollLayout.addView(webPage);
        	
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

	//初始滑动效果
	public void InitSlider(){
		showAction = new TranslateAnimation(1, 0F, 1, 0F, 1, 1F, 1, 0F);
		//设置动画持续时间
		showAction.setDuration(500L);
	}

	//底部tabview控件的初始化
	public void IntiTabView(){

		//分类导航图片控件
		category_navigation = (ImageView)findViewById(R.id.category_navigation);
		//分类导航图片控件点击事件监听器
		category_navigation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				TextView textview = category_navigation_text;
				int i = Color.rgb(58, 161, 248);
				textview.setTextColor(i);
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
		
		//分类导航
		category_navigation_text = (TextView)findViewById(R.id.category_navigation_text);

		//今日最新
		new_video_text = (TextView)findViewById(R.id.new_video_text);

		//视频搜索
		video_search_text = (TextView)findViewById(R.id.video_search_text);
		
		//收藏
		video_collect_text = (TextView)findViewById(R.id.video_collect_text);

		//更多
		video_more_text = (TextView)findViewById(R.id.video_more_text);

		//设置颜色
		int i = Color.rgb(58, 161, 248);
		category_navigation_text.setTextColor(i);
	}

	public void changeTabClick(View view){
		
	}

	protected void onActivityResult(int i, int j, Intent intent){
		super.onActivityResult(i, j, intent);
		if (j != 3){
			if (j == 5){
				PushTabBtnIndex = 1;
			}
		}else{
			PushTabBtnIndex = 1;
		}
	}

	public boolean onContextItemSelected(MenuItem menuitem)
	{
		return super.onContextItemSelected(menuitem);
	}

	//activity的创建函数
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
	/*	//设置布局文件
		setContentView(R.layout.type_activity);
		
		//初始化滑动效果
		InitSlider();
		//tabview组件初始化函数
		IntiTabView();*/
		
		setContentView(R.layout.type_page);
		
		mScrollLayout = (ScrollLayout)findViewById(R.id.ScrollLayout);
		
		//设置屏幕标识
		SetScreenOnFlag();
		
		//初始化函数
		Init();
		
		
	}

	public boolean onCreateOptionsMenu(Menu menu){
		return super.onCreateOptionsMenu(menu);
	}


	//按键的事件监听接口函数
	public boolean onKeyDown(int keyCode, KeyEvent keyevent){
		boolean flag = false;
		
		//返回键的事件处理，退出对话框
		if (keyCode == keyevent.KEYCODE_BACK){
			
			android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(this)).
				setTitle(R.string.vlc_exit_info_title).setMessage(R.string.vlc_exit_info_content);
			
			android.app.AlertDialog.Builder builder1 = builder.setNegativeButton(R.string.cancel_exit,
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});

			android.app.AlertDialog alertdialog = builder1.setPositiveButton(R.string.button_confirm, 
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i){
							finish();
						}
					}).show();
			flag = true;
		} else{
			flag = super.onKeyDown(keyCode, keyevent);
		}
		return flag;
	}

	//menu菜单选择事件的函数
	public boolean onOptionsItemSelected(MenuItem menuitem){
		return super.onOptionsItemSelected(menuitem);
	}

	//activity显示时调用的方法
	protected void onResume(){
		super.onResume();
		Intent intent = new Intent();
		setResult(-1);
	}

	public boolean onSearchRequested()
	{
		return false;
	}

	public void searchClick(View view)
	{
		boolean flag = onSearchRequested();
	}

	//数据数组初始化
	static {
		String as[] = new String[19];
		as[0] = "电影";
		as[1] = "电视";
		as[2] = "综艺";
		as[3] = "动漫";
		as[4] = "音乐";
		as[5] = "教育";
		as[6] = "资讯";
		as[7] = "娱乐";
		as[8] = "原创";
		as[9] = "体育";
		as[10] = "汽车";
		as[11] = "科技";
		as[12] = "游戏";
		as[13] = "生活";
		as[14] = "时尚";
		as[15] = "旅游";
		as[16] = "母婴";
		as[17] = "搞笑";
		as[18] = "广告";
		youkuMenu = as;
		
		String as1[] = new String[17];
		as1[0] = "原创";
		as1[1] = "电影";
		as1[2] = "动漫";
		as1[3] = "搞笑";
		as1[4] = "资讯";
		as1[5] = "财经";
		as1[6] = "娱乐";
		as1[7] = "体育";
		as1[8] = "游戏";
		as1[9] = "广告";
		as1[10] = "汽车";
		as1[11] = "生活";
		as1[12] = "教育";
		as1[13] = "房产";
		as1[14] = "综艺";
		as1[15] = "女性";
		as1[16] = "生活";
		ku6Menu = as1;
		
		String as2[] = new String[18];
		as2[0] = "资讯";
		as2[1] = "电视";
		as2[2] = "电影";
		as2[3] = "综艺";
		as2[4] = "原创";
		as2[5] = "娱乐";
		as2[6] = "搞笑";
		as2[7] = "动漫";
		as2[8] = "音乐";
		as2[9] = "体育";
		as2[10] = "汽车";
		as2[11] = "财经";
		as2[12] = "时尚";
		as2[13] = "女性";
		as2[14] = "游戏";
		as2[15] = "生活";
		as2[16] = "科技";
		as2[17] = "教育";
		tudouMenu = as2;
		String as3[] = new String[20];
		as3[0] = "电视";
		as3[1] = "音乐";
		as3[2] = "电影";
		as3[3] = "娱乐";
		as3[4] = "动漫";
		as3[5] = "游戏";
		as3[6] = "搞笑";
		as3[7] = "美食";
		as3[8] = "女性";
		as3[9] = "时尚";
		as3[10] = "体育";
		as3[11] = "科技";
		as3[12] = "数码";
		as3[13] = "教育";
		as3[14] = "生活";
		as3[15] = "家庭";
		as3[16] = "旅游";
		as3[17] = "汽车";
		as3[18] = "财经";
		as3[19] = "资讯";
		jianfang6Menu = as3;
		String as4[] = new String[20];
		as4[0] = "原创";
		as4[1] = "娱乐";
		as4[2] = "游戏";
		as4[3] = "广告";
		as4[4] = "搞笑";
		as4[5] = "体育";
		as4[6] = "社会";
		as4[7] = "音乐";
		as4[8] = "生活";
		as4[9] = "科技";
		as4[10] = "猎奇";
		as4[11] = "动漫";
		as4[12] = "教育";
		as4[13] = "汽车";
		as4[14] = "旅游";
		as4[15] = "宠物";
		as4[16] = "房产";
		as4[17] = "母婴";
		as4[18] = "拍客";
		as4[19] = "军事";
		sinaMenu = as4;
		String as5[] = new String[12];
		as5[0] = "Month - 1";
		as5[1] = "Month - 2";
		as5[2] = "Month - 3";
		as5[3] = "Month - 4";
		as5[4] = "Month - 5";
		as5[5] = "Month - 6";
		as5[6] = "Month - 7";
		as5[7] = "Month - 8";
		as5[8] = "Month - 9";
		as5[9] = "Month - 10";
		as5[10] = "Month - 11";
		as5[11] = "Month - 12";
		desc = as5;
	}


}
