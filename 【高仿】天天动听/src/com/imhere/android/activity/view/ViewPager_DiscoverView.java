package com.imhere.android.activity.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.imhere.android.R;
import com.imhere.android.adapter.DiscoverInfiniteLoopViewPagerAdapter;
import com.imhere.android.adapter.DiscoverMPagerAdapter;
import com.imhere.android.adapter.DiscoverMyListViewAdapter;
import com.imhere.android.adapter.MyLoopViewPagerAdatper;
import com.imhere.android.entity.DiscoverBriefUnit;
import com.imhere.android.entity.DiscoverClassify;
import com.imhere.android.view.DiscoverInfiniteLoopViewPager;
import com.imhere.android.view.NoScrollListView;

public class ViewPager_DiscoverView extends LayoutViews {
	private View mDiscoverView;

	private DiscoverMPagerAdapter viewPager;

	private DiscoverInfiniteLoopViewPagerAdapter pagerAdapter;

	private DiscoverInfiniteLoopViewPager viewpager_Information;
	private int[] imageViewIds;
	private List<ImageView> imageViews;

	Activity mActivity;

	Handler mHandler;

	private NoScrollListView listview_classify;
	ViewPager frameViewPager;
	ScrollView mScrollView ;
	public ViewPager_DiscoverView(Handler handler,ViewPager frameViewPager) {
		mHandler = handler;
		this.frameViewPager=frameViewPager;
	}

	@Override
	public View createView(Activity activity) {
		mActivity = activity;
		// TODO Auto-generated method stub
		mDiscoverView = LayoutInflater.from(mActivity).inflate(
				R.layout.viewpager_discover, null);
		initViews();
		initEvents();
		return mDiscoverView;

	}

	@Override
	protected void initViews() {
		viewpager_Information = (DiscoverInfiniteLoopViewPager) mDiscoverView
				.findViewById(R.id.viewpager_Information);
		mScrollView=(ScrollView) mDiscoverView.findViewById(R.id.mScrollView);
		imageViewIds = new int[] { R.drawable.p1, R.drawable.p2, R.drawable.p3,
				R.drawable.p4 };
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageViewIds.length; i++) {
			ImageView mImageView = new ImageView(mActivity);
			mImageView.setImageResource(imageViewIds[i]);
			imageViews.add(mImageView);
		}
		pagerAdapter = new DiscoverInfiniteLoopViewPagerAdapter(
				new MyLoopViewPagerAdatper(imageViews));
		viewpager_Information.setInfinateAdapter(mHandler, pagerAdapter);

		listview_classify = (NoScrollListView) mDiscoverView
				.findViewById(R.id.listview_classify);
		List<DiscoverClassify> itmes = new ArrayList<DiscoverClassify>();
		for (int j = 0; j < 6; j++) {
			DiscoverClassify classify = new DiscoverClassify();
			classify.setItemName("j--->" + j);
			List<DiscoverBriefUnit> gridviewitmes = new ArrayList<DiscoverBriefUnit>();
			for (int i = 0; i < 6; i++) {
				DiscoverBriefUnit unit = new DiscoverBriefUnit();
				unit.setDrawable(mActivity.getResources().getDrawable(
						R.drawable.bg_title_off));
				unit.setName("i--->" + i);
				gridviewitmes.add(unit);
			}
			classify.setGridViewItems(gridviewitmes);
			itmes.add(classify);
		}

		listview_classify.setAdapter(new DiscoverMyListViewAdapter(mActivity,
				itmes));
	}

	@Override
	protected void initEvents() {

	}

	public DiscoverInfiniteLoopViewPager getViewpager_Information() {
		return viewpager_Information;
	}

}
