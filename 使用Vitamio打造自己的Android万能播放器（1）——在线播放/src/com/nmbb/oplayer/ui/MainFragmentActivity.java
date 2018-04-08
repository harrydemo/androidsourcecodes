package com.nmbb.oplayer.ui;

import com.nmbb.oplayer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class MainFragmentActivity extends FragmentActivity implements
		OnClickListener {

	private ViewPager mPager;
	private RadioButton mRadioFile;
	private RadioButton mRadioOnline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);

		// ~~~~~~ 绑定控件
		mPager = (ViewPager) findViewById(R.id.pager);
		mRadioFile = (RadioButton) findViewById(R.id.radio_file);
		mRadioOnline = (RadioButton) findViewById(R.id.radio_online);

		// ~~~~~~ 绑定事件
		mRadioFile.setOnClickListener(this);
		mRadioOnline.setOnClickListener(this);
		mPager.setOnPageChangeListener(mPagerListener);

		// ~~~~~~ 绑定数据
		mPager.setAdapter(mAdapter);
	}

	@Override
	public void onBackPressed() {
		if (getFragmentByPosition(mPager.getCurrentItem()).onBackPressed())
			return;
		else
			super.onBackPressed();
	}
	
	/** 查找Fragment */
	private FragmentBase getFragmentByPosition(int position) {
		return (FragmentBase) getSupportFragmentManager().findFragmentByTag("android:switcher:" + mPager.getId() + ":" + position);
	}

	private FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {

		/** 仅执行一次 */
		@Override
		public Fragment getItem(int position) {
			Fragment result = null;
			switch (position) {
			case 1:
				result = new FragmentOnline();// 在线视频
				break;
			case 0:
			default:
				result = new FragmentFile();// 本地视频
				break;
			}
			return result;
		}

		@Override
		public int getCount() {
			return 2;
		}

	};

	private ViewPager.SimpleOnPageChangeListener mPagerListener = new ViewPager.SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:// 本地视频
				mRadioFile.setChecked(true);
				break;
			case 1:// 在线视频
				mRadioOnline.setChecked(true);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio_file:
			mPager.setCurrentItem(0);
			break;
		case R.id.radio_online:
			mPager.setCurrentItem(1);
			break;
		}
	}
}
