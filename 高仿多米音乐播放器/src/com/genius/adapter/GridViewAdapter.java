package com.genius.adapter;

import com.genius.demo.MenuItemData;
import com.genius.demo.R;
import com.genius.demo.R.id;
import com.genius.demo.R.layout;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
	
	Context mContext;
	
	LayoutInflater mLayoutInflater;
	
	MenuItemData 	mMenuItemData;
	
	public GridViewAdapter(Context context, MenuItemData menuItemData)
	{

		mContext = context;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		mMenuItemData = menuItemData;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMenuItemData.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.menu_item, null);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview);	
		imageView.setBackgroundDrawable(mMenuItemData.getDrawable(position));

		
		TextView  textView = (TextView) convertView.findViewById(R.id.textview);
		textView.setText(mMenuItemData.getTitle(position));
		
		
		return convertView;
		
	}

}
