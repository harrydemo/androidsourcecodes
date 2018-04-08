package com.imhere.android.activity.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.imhere.android.R;
import com.imhere.android.adapter.AboutMeSpaceListAdapter;
import com.imhere.android.entity.Thoughts;

public class ViewPager_AboutMeView extends LayoutViews {
	private View mAboutMeView;
	ListView mListView_Am;
	Activity mActivity;

	@Override
	public View createView(Activity activity) {
		mActivity = activity;
		mAboutMeView = LayoutInflater.from(mActivity).inflate(
				R.layout.viewpager_about_me, null);
		initViews();
		initEvents();
		return mAboutMeView;

	}

	@Override
	protected void initViews() {
		mListView_Am = (ListView) mAboutMeView
				.findViewById(R.id.listview_aboutMe);
		View mHeaderView = initListViewHeader();
		mListView_Am.addHeaderView(mHeaderView);
		mListView_Am.setDivider(null);
		mListView_Am.setCacheColorHint(Color.TRANSPARENT);
		mListView_Am.setSelector(mActivity.getResources().getDrawable(
				R.drawable.bg_space_listview_select));
		List<Thoughts> mThoughts = new ArrayList<Thoughts>();
		Thoughts thoughts = new Thoughts();
		thoughts.setTime("2012.12.25");
		thoughts.setAddress("浙江省台州市黄岩区江口街道埭头村");
		thoughts.setDrawable(new BitmapDrawable(BitmapFactory.decodeResource(
				mActivity.getResources(), R.drawable.pic01)));
		thoughts.setMsg("习惯被拒绝的人会先拒绝这一次至少是我先说离别有一些痛楚看不见泪水有一种防卫叫做我无所谓要让你快乐原是我的心愿可是你从不在意我的伤悲丢给我一些喜悦的碎屑却带走我一切");

		Thoughts thoughts1 = new Thoughts();
		thoughts1.setTime("2013.11.05");
		thoughts1.setAddress("浙江省温州市鹿城区划龙桥路");
		thoughts1.setDrawable(new BitmapDrawable(BitmapFactory.decodeResource(
				mActivity.getResources(), R.drawable.pic02)));
		thoughts1
				.setMsg("你不爱我　是我舍不得是我不配　为你在狼狈你不爱我　你真的不爱我尽力而为我拼命给也是浪费你不爱我　是我舍不得是我不配　和命运作对你不爱我　你真的不爱我一直以为　我是后卫原来只是　那后备");
		mThoughts.add(thoughts);
		mThoughts.add(thoughts1);
		AboutMeSpaceListAdapter mAdapter = new AboutMeSpaceListAdapter(
				mThoughts, mActivity);
		mListView_Am.setAdapter(mAdapter);

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	private View initListViewHeader() {
		View ListViewHeaderView = LayoutInflater.from(mActivity).inflate(
				R.layout.listview_aboutme_header, null);
		LinearLayout lay_UserSpace_BG = (LinearLayout) ListViewHeaderView
				.findViewById(R.id.lay_UserSpace_BG);
		lay_UserSpace_BG.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("BG");
			}
		});
		LinearLayout lay_UserHeader = (LinearLayout) ListViewHeaderView
				.findViewById(R.id.lay_UserHeader);
		lay_UserHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("HD");
			}
		});
		return ListViewHeaderView;
	}

}
