package com.dream.myqiyi.home;

import com.dream.myqiyi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
	Context mContext;
	int[] res = new int[] { R.drawable.t1, R.drawable.t2,
			R.drawable.t3, R.drawable.t1, R.drawable.t2,
			R.drawable.t3, R.drawable.t1, R.drawable.t2,
			R.drawable.t3 };

	public GalleryAdapter(Context cnt) {
		this.mContext = cnt;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return res.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.gallery_item,
					null);
		}
		ImageView img = (ImageView) arg1.findViewById(R.id.home_img);
		img.setImageResource(res[arg0]);
		return arg1;
	}
}
