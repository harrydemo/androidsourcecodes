 /*
  * Copyright (C) 2012 The TimeSale Project
  * All right reserved.
  * Version 1.00 2012-11-25
  * Author veally@foxmail.com
  */
package com.veally.timesale;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 
 * @author veally@foxmail.com
 */
public class AdapterSaleItem extends BaseAdapter {

	private Context mContext;
	private List<ProductItem> mItems;
	private LayoutInflater inflater;
	private BufferedInputStream bis;
	private AssetManager assetManager;
	public AdapterSaleItem(Context context, List<ProductItem> items) {
		this.mContext = context;
		this.mItems = items;
		inflater = LayoutInflater.from(mContext);
		assetManager = mContext.getAssets();
	}
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item, null);
		}
		ImageView image = (ImageView) convertView.findViewById(R.timeSale.image);
		CustomDigitalClock remainTime = (CustomDigitalClock) convertView.findViewById(R.timeSale.remainTime);
		try {
			bis = new BufferedInputStream(assetManager.open(mItems.get(position).getImageUrl()));
			image.setImageBitmap(BitmapFactory.decodeStream(bis));
		} catch (IOException e) {
			e.printStackTrace();
		}
		remainTime.setEndTime(mItems.get(position).getRemainTime());
		remainTime.setClockListener(new CustomDigitalClock.ClockListener() { // register the clock's listener
			
			@Override
			public void timeEnd() {
				// The clock time is ended.
			}
			
			@Override
			public void remainFiveMinutes() {
				// The clock time is remain five minutes.
			}
		});
		return convertView;
	}

}
