package com.eoemobile.book.ex_widgetdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.BaseAdapter;

import android.widget.GridView;

import android.widget.ImageView;

public class GridViewActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);
		setTitle("GridViewActivity");
		GridView gridview = (GridView) findViewById(R.id.grid_view);
		gridview.setAdapter(new ImageAdapter(this));
	}

	public class ImageAdapter extends BaseAdapter
	{
		private Context mContext;

		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		public int getCount()
		{
			return mThumbIds.length;
		}

		public Object getItem(int position)
		{
			return null;
		}

		public long getItemId(int position)
		{
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;
			if (convertView == null)
			{ // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else
			{
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

		// references to our images
		private Integer[] mThumbIds =
		{ R.drawable.grid_view_01, R.drawable.grid_view_02, R.drawable.grid_view_03, R.drawable.grid_view_04, R.drawable.grid_view_05, R.drawable.grid_view_06, R.drawable.grid_view_07, R.drawable.grid_view_08, R.drawable.grid_view_09, R.drawable.grid_view_10, R.drawable.grid_view_11, R.drawable.grid_view_12, R.drawable.grid_view_13, R.drawable.grid_view_14, R.drawable.grid_view_15, R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7 };
	}

}