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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.datastruct.ListItemBean;
import com.android.tools.AsyncImgLoader;
import com.android.tools.RemoveCallback;
import com.sph.player.R;

//收藏的数据适配器
public class ListViewCollectAdapter extends ArrayAdapter
	implements android.view.View.OnClickListener{
	
	//数据项对象
	public class ViewHolder extends View{

		public ImageView delBtn;
		public ImageView imageDelIcon;
		public ImageView imageView;
		public boolean isShowing;
		public TextView videoLen;
		public TextView videoTitle;

		public ViewHolder(Context context){
			super(context);
			isShowing = false;
		}
	}


	private int ClickItem;
	private int Pre_holder;
	private Context ct;
	private TranslateAnimation hideAction;
	private AsyncImgLoader imageLoader;
	private RemoveCallback rcb;
	private int selectItem;
	private boolean setEditModle;
	private boolean setEditModle_deleteButton;
	private TranslateAnimation showAction;
	private Map viewMap;

	//构造函数
	public ListViewCollectAdapter(Activity activity, List list, RemoveCallback removecallback){
		super(activity, 0, list);
		
		ct = null;
		showAction = null;
		hideAction = null;
		//图片加载的线程
		imageLoader = new AsyncImgLoader();
		//数据对象
		viewMap = new HashMap();
		setEditModle_deleteButton = false;
		setEditModle = false;
		selectItem = -1;
		ClickItem = -1;
		Pre_holder = -1;
		ct = activity;
		rcb = removecallback;
		//显示button
		ShowButton();
		HiddleButton();
	}

	public void HiddleButton(){
		int i = 1;
		int j = 1;
		float f = 0F;
		int k = 1;
		float f1 = 0F;
		hideAction = new TranslateAnimation(1, 0F, i, -1F, j, f, k, f1);
	}

	//恢复默认
	public void ResetDefault(ListItemBean listitembean){
		if (viewMap == null) {
			return;
		} else {
			int size =  viewMap.size();
			for(int i = 0; i < size; i++){
				
			}
		}
	}

	//恢复删除按钮
	public void ResetDeleteButton(){
		if (viewMap == null){
			return;
		} else {
			
			for(int i = 0; i < viewMap.size(); i++){
				Map map = viewMap;
				Integer integer = Integer.valueOf(i);
				RelativeLayout relativelayout = (RelativeLayout)((View)map.get(integer)).findViewById(R.id.collect_delete_btn);
				relativelayout.startAnimation(showAction);
				relativelayout.setVisibility(0);
				relativelayout.postInvalidate();
			}
		}
	}

	//显示button
	public void ShowButton(){
		int i = 1;
		int j = 1;
		float f = 0F;
		int k = 1;
		float f1 = 0F;
		showAction = new TranslateAnimation(1, 1F, i, 0F, j, f, k, f1);
		showAction.setDuration(500L);
	}

	public View getDropDownView(int i, View view, ViewGroup viewgroup)
	{
		int j = 10 + 1;
		return null;
	}

	public boolean getEditModle()
	{
		return setEditModle;
	}

	//获取当前显示的视图
	public View getView(int i, View view, ViewGroup viewgroup){
		
		int j = i;
		view = ((Activity)getContext()).getLayoutInflater().inflate
			(R.layout.type_activity_collect_list, null);
		final ListItemBean itemBean = (ListItemBean)getItem(i);
		final RelativeLayout myMenu = (RelativeLayout)view.findViewById(R.id.my_collect_menu);
		myMenu.setVisibility(View.GONE);
		myMenu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (myMenu.isShown()){
					rcb.RemoveCollectItemBean(itemBean);
				}
			}
		});
		
		
		final ImageView imageDelIcon = (ImageView)view.findViewById(R.id.item_del_icon);
		TextView textview;
		String s;
		TextView textview1;
		String s1;
		final ImageView imageView;
		String s2;
		AsyncImgLoader asyncimgloader;
		Bitmap bitmap;
		Integer integer;
		Object obj;
		
		if (!setEditModle){
			imageDelIcon.setVisibility(View.GONE);
		}else{
			imageDelIcon.setVisibility(View.VISIBLE);
		}
		
		imageDelIcon.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				if (!myMenu.isShown()){
					imageDelIcon.setBackgroundResource(R.drawable.collect_del);
					myMenu.startAnimation(showAction);
					myMenu.setVisibility(View.VISIBLE);
				} else{
					imageDelIcon.setBackgroundResource(R.drawable.collect_show);
					imageDelIcon.setVisibility(View.VISIBLE);
					myMenu.setVisibility(View.GONE);
				}
			}
		});
		
		textview = (TextView)view.findViewById(R.id.txt_video_caption);
		s = itemBean.getVideoTitle();
		textview.setText(s);
		
		textview1 = (TextView)view.findViewById(R.id.txt_video_length);
		s1 = itemBean.getVideoLength();
		textview1.setText(s1);
		
		imageView = (ImageView)view.findViewById(R.id.video_preview_img);
		s2 = itemBean.getVideoImgSrc();
		asyncimgloader = imageLoader;
		LoadImageCallback mLoadImageCallback  = new LoadImageCallback(imageView);
		bitmap = asyncimgloader.loadDrawable(s2, mLoadImageCallback);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		}
		integer = Integer.valueOf(i);
		obj = viewMap.put(integer, view);
		
		return view;
	}

	public void notifyDataSetChanged()
	{
	}

	public void onClick(View view)
	{
		
		
	}

	//设置编辑模式
	public void setEditModle(boolean flag){
		setEditModle = flag;
	}

	//设置选择的对象
	public void setSelectItem(int i){
		selectItem = i;
	}



	//加载图片的回调函数
	private class LoadImageCallback
		implements com.android.tools.AsyncImgLoader.ImageCallback{

		final ListViewCollectAdapter this$0;
		private final ImageView imageView;

		public void imageLoaded(Bitmap bitmap, String s){
			if (bitmap != null){
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(0);
			}
		}

		LoadImageCallback(ImageView imageView){
			super();
			this$0 = ListViewCollectAdapter.this;
			this.imageView = imageView;
		}
	}

}
