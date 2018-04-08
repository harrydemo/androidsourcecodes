package com.galleryTab;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;
 public class TabAdapter extends BaseAdapter {
	 private Context mContext;
	 private List<String> mList;
	 private int mSelectedTab;
	
	 public TabAdapter(Context context, List<String> list) {
	 mContext = context;
	 /*使用attrs里的 <declare-styleable>属性*/
	 TypedArray a = context.obtainStyledAttributes(R.styleable.Gallery);
	 a.recycle();//重复使用对象的styleable属性
	   if (list == null)
	 list = Collections.emptyList();
	 mList = list;
	 }
	 /*
	 * 设置选中的Tab，并且刷新界面
	 */
	 public void setSelectedTab(int tab) {
	 if (tab != mSelectedTab) {
	 mSelectedTab = tab;
	 notifyDataSetChanged();
	 }
	 }
	
	 public int getSelectedTab() {
	 return mSelectedTab;
	 }
	
	 public int getCount() {
	 return Integer.MAX_VALUE;//返回最大值
	   }
	
	 public Object getItem(int position) {
	 return mList.get(position);
	 }
	
	 public long getItemId(int position) {
	 return position;
	 }
	
	 public View getView(int position, View convertView, ViewGroup parent) {
	 TextView text = null;//这里只放一个TextView，可以根据需要来定制
	   if (convertView == null ) {
	 text = new TextView(mContext);
	 } else {
	 text = (TextView) convertView;
	 }
	
	 text.setTextColor(Color.WHITE);
	 text.setText(mList.get(position % mList.size()));//循环取余设置显示内容
	  
	 text.setLayoutParams(new Gallery.LayoutParams(102, 40));
	 text.setGravity(Gravity.CENTER);
	
	 /*
	 * 对于选中的Tab，给他一个选中的背景
	 */
	 if (position == mSelectedTab)
	 text.setBackgroundResource(R.drawable.tab_button_select);
	 else 
	 text.setBackgroundResource(R.drawable.tab_button_unselect);
	
	 return text;
	 }
	 }