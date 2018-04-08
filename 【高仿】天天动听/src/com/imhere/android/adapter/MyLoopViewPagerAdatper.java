package com.imhere.android.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyLoopViewPagerAdatper extends DiscoverMPagerAdapter {
	List<ImageView> views;

	public MyLoopViewPagerAdatper(List<ImageView> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == (View) object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// return super.instantiateItem(container, position);
		views.get(position).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("========>>> 点击了viewpager的第 " + position
						+ " 项");
			}
		});
		container.addView(views.get(position));
		return views.get(position);
	}

}
