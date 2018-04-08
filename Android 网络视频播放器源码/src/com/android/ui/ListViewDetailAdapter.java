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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.datastruct.ListItemBean;
import com.android.tools.AsyncImgLoader;
import com.sph.player.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/*
 * 分类导航详情的数据适配器
 * */
public class ListViewDetailAdapter extends ArrayAdapter
{

	//下载图片线程
	private AsyncImgLoader imageLoader;
	//listview的数据
	private Map viewMap;

	//构造函数
	public ListViewDetailAdapter(Activity activity, List list){
		super(activity, 0, list);
		
		//创建下载图片的线程
		imageLoader = new AsyncImgLoader();

		viewMap = new HashMap();
	}

	//获取显示当前的view
	public View getView(int i, View view, ViewGroup viewgroup){
		Integer integer = Integer.valueOf(i);
		View view1 = (View)viewMap.get(integer);
		
		if (view1 == null){
			//加载布局文件
			view1 = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.type_activity_detail_list, null);
			
			//获取当前数据项的数据
			ListItemBean listitembean = (ListItemBean)getItem(i);
			
			//显示标题控件
			TextView textview = (TextView)view1.findViewById(R.id.txt_video_caption);
			String s = listitembean.getVideoTitle();
			textview.setText(s);
			
			//显示视频长度控件
			TextView textview1 = (TextView)view1.findViewById(R.id.txt_video_length);
			String s1 = listitembean.getVideoLength();
			textview1.setText(s1);
			
			//显示视频的图片控件
			final ImageView imageView = (ImageView)view1.findViewById(R.id.video_preview_img);

			String imageUrl = listitembean.getVideoImgSrc();
			AsyncImg  mAsyncImg = new AsyncImg(imageView);
			Bitmap bitmap = imageLoader.loadDrawable(imageUrl, mAsyncImg);
			
			Integer integer1 = Integer.valueOf(i);
			Object obj = viewMap.put(integer1, view1);
		}
		return view1;
	}

	//加载图片的回调class
	private class AsyncImg
		implements com.android.tools.AsyncImgLoader.ImageCallback
	{

		private final ImageView imageView;

		public void imageLoaded(Bitmap bitmap, String s){
			if (bitmap != null){
				imageView.setImageBitmap(bitmap);
			}
		}

		AsyncImg(ImageView imageview){
			super();
			this.imageView = imageview;
		}
	}

}
