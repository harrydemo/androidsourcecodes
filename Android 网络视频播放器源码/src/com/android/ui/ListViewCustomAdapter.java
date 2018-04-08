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
import com.android.tools.CDataStruct;
import com.android.tools.RelativeCallback;
import com.sph.player.R;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.*;
import android.widget.*;

/*
 * ListView的数据适配器
 * */
public class ListViewCustomAdapter extends BaseAdapter{
	//每一行对象
	public class ViewHolder{
		//图片控件
		public ImageView imgViewLogo;
		//标题控件
		public TextView txtViewTitle;

		public ViewHolder(){

		}
	}

	//相关变量定义
	private String DefaultUrl;
	private String PageIndex;
	public Activity context;
	private CDataStruct hashList;
	public LayoutInflater inflater;
	View myConvertView;
	private int selectItem;
	public String title[];
	private RelativeCallback typeAc;

	//构造函数
	public ListViewCustomAdapter(Activity activity, String menu[], String dec[]){
		//相关变量初始化
		myConvertView = null;
		DefaultUrl = "";
		PageIndex = "";
		typeAc = null;
		selectItem = -1;
		
		//赋值初始化
		context = activity;
		title = menu;
		//获取加载布局文件对象
		inflater = (LayoutInflater)activity.getSystemService("layout_inflater");
	}

	//获取当前listview数据适配器中总数据量
	public int getCount(){
		return title.length;
	}

	//获取当前选择对象
	public Object getItem(int i){
		return null;
	}

	//获取当前选择对象id
	public long getItemId(int i){
		return (long)i;
	}

	//获取当前显示的view
	public View getView(int i, View view, ViewGroup viewgroup){
		//相关变量定义
		ViewHolder viewholder;
		TextView textview1;
		String s;
		int j;
		
		//获取创建当前显示的view
		if (view == null){
			//创建当前行显示对象
			viewholder = new ViewHolder();
			//设置当前行显示的布局文件
			view = inflater.inflate(R.layout.type_item, null);
			
			//获取图片控件
			viewholder.imgViewLogo = (ImageView)view.findViewById(R.id.ivAppIcon);
			
			//获取显示标题控件
			viewholder.txtViewTitle = (TextView)view.findViewById(R.id.tvAppName);
			viewholder.txtViewTitle.getPaint().setFakeBoldText(true);
			
			//设置当前view显示的控件对象
			view.setTag(viewholder);
		} else{
			//获取得当前view显示控件
			viewholder = (ViewHolder)view.getTag();
		}
		
		//设置图片控件显示的图片资源
		while(true){
			if(title[i].equals("原创")){
				viewholder.imgViewLogo.setImageResource(R.drawable.originality);
				break;
			} else if(title[i].equals("体育")){
				viewholder.imgViewLogo.setImageResource(R.drawable.sport);
				break;
			}else if(title[i].equals("音乐")){
				viewholder.imgViewLogo.setImageResource(R.drawable.music);
				break;
			}else if(title[i].equals("动漫")){
				viewholder.imgViewLogo.setImageResource(R.drawable.animation);
				break;
			}else if(title[i].equals("搞笑")){
				viewholder.imgViewLogo.setImageResource(R.drawable.funny);
				break;
			}else if(title[i].equals("电影")){
				viewholder.imgViewLogo.setImageResource(R.drawable.movie);
				break;
			}else if(title[i].equals("电视")){
				viewholder.imgViewLogo.setImageResource(R.drawable.tv);
				break;
			}else if(title[i].equals("汽车")){
				viewholder.imgViewLogo.setImageResource(R.drawable.bus);
				break;
			}else if(title[i].equals("教育")){
				viewholder.imgViewLogo.setImageResource(R.drawable.education);
				break;
			}else if(title[i].equals("资讯")){
				viewholder.imgViewLogo.setImageResource(R.drawable.news);
				break;
			}else if(title[i].equals("娱乐")){
				viewholder.imgViewLogo.setImageResource(R.drawable.entertainment);
				break;
			}else if(title[i].equals("科技")){
				viewholder.imgViewLogo.setImageResource(R.drawable.technology);
				break;
			}else if(title[i].equals("游戏")){
				viewholder.imgViewLogo.setImageResource(R.drawable.game);
				break;
			}else if(title[i].equals("生活")){
				viewholder.imgViewLogo.setImageResource(R.drawable.life);
				break;
			}else if(title[i].equals("时尚")){
				viewholder.imgViewLogo.setImageResource(R.drawable.fashion);
				break;
			}else if(title[i].equals("旅游")){
				viewholder.imgViewLogo.setImageResource(R.drawable.trip);
				break;
			}else if(title[i].equals("母婴")){
				viewholder.imgViewLogo.setImageResource(R.drawable.child);
				break;
			}else if(title[i].equals("广告")){
				viewholder.imgViewLogo.setImageResource(R.drawable.adver);
				break;
			}else if(title[i].equals("宠物")){
				viewholder.imgViewLogo.setImageResource(R.drawable.pet);
				break;
			}else if(title[i].equals("房产")){
				viewholder.imgViewLogo.setImageResource(R.drawable.house);
				break;
			}else if(title[i].equals("拍客")){
				viewholder.imgViewLogo.setImageResource(R.drawable.camera);
				break;
			}else if(title[i].equals("军事")){
				viewholder.imgViewLogo.setImageResource(R.drawable.military);
				break;
			}else if(title[i].equals("女性")){
				viewholder.imgViewLogo.setImageResource(R.drawable.woman);
				break;
			}else if(title[i].equals("记录")){
				viewholder.imgViewLogo.setImageResource(R.drawable.write);
				break;
			}else if(title[i].equals("美食")){
				viewholder.imgViewLogo.setImageResource(R.drawable.food);
				break;
			}else if(title[i].equals("财经")){
				viewholder.imgViewLogo.setImageResource(R.drawable.book);
				break;
			}else if(title[i].equals("猎奇")){
				viewholder.imgViewLogo.setImageResource(R.drawable.hunt);
				break;
			}else if(title[i].equals("数码")){
				viewholder.imgViewLogo.setImageResource(R.drawable.numeral);
				break;
			}else if(title[i].equals("家庭")){
				viewholder.imgViewLogo.setImageResource(R.drawable.family);
				break;
			}else if(title[i].equals("社会")){
				viewholder.imgViewLogo.setImageResource(R.drawable.society);
				break;
			}else if(title[i].equals("综艺")){
				viewholder.imgViewLogo.setImageResource(R.drawable.variety);
				break;
			}else {
				viewholder.imgViewLogo.setImageResource(R.drawable.originality);
				break;
			}
		}
		
		
		//设置当前标题控件的标题
		viewholder.txtViewTitle.setText(title[i]);
		viewholder.txtViewTitle.setTextColor(Color.BLACK);
		
		int color = Color.rgb(236, 239, 243);
		view.setBackgroundColor(color);

		System.out.println("i = "+i);
		System.out.println("selectItem = "+selectItem);
		
		return view;
	}

	//设置HashList
	public void setHashList(CDataStruct cdatastruct, String s, String s1, RelativeCallback relativecallback){
		//当前cd数据结构
		hashList = cdatastruct;
		//默认的url
		DefaultUrl = s;
		//请求页数
		PageIndex = s1;
		//回调函数
		typeAc = relativecallback;
	}

	//设置当前显示行
	public void setSelectItem(int i){
		selectItem = i;
	}
}
