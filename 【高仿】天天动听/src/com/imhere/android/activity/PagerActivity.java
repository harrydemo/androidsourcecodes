package com.imhere.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imhere.android.BaseActivity;
import com.imhere.android.BaseApplication;
import com.imhere.android.R;
import com.imhere.android.activity.view.ViewPager_AboutMeView;
import com.imhere.android.activity.view.ViewPager_AppWallView;
import com.imhere.android.activity.view.ViewPager_DiscoverView;
import com.imhere.android.activity.view.ViewPager_GroupView;
import com.imhere.android.activity.view.ViewPager_SideView;
import com.imhere.android.adapter.MyFramePagerAdapter;

public class PagerActivity extends BaseActivity implements OnClickListener {
	Activity mPagerActivity;
	// 图标宽度
	private int pngW;
	// 偏移值
	private int offset = 0;

	private final int count = 5;

	private int currIndex = 1;// 当前页卡编

	ImageView mCursor;

	List<View> mTitleViews = new ArrayList<View>();

	List<View> mTitleLayoutViews = new ArrayList<View>();

	ImageView img_Group;

	TextView txt_AboutMe, txt_Side, txt_Discover, txt_AppWall;

	ViewPager mViewPager;

	LinearLayout lay_Group, lay_AboutMe, lay_Side, lay_Discover, lay_AppWall;

	
	private int sleepTime = 3000;

	ViewPager_DiscoverView DiscoverView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);

		mPagerActivity = this;
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		initTitleViews();
		initCursor();
		initPagerViews();

	}

	@Override
	protected void initEvents() {
		for (int i = 0; i < mTitleLayoutViews.size(); i++) {
			mTitleLayoutViews.get(i).setOnClickListener(this);
		}
		// 当前项取消点击事件
		mTitleLayoutViews.get(currIndex).setClickable(false);
	}

	private void initTitleViews() {
		img_Group = (ImageView) findViewById(R.id.img_Group);
		txt_AboutMe = (TextView) findViewById(R.id.txt_AboutMe);
		txt_Side = (TextView) findViewById(R.id.txt_Side);
		txt_Discover = (TextView) findViewById(R.id.txt_Discover);
		txt_AppWall = (TextView) findViewById(R.id.txt_AppWall);

		lay_Group = (LinearLayout) findViewById(R.id.lay_Group);
		lay_AboutMe = (LinearLayout) findViewById(R.id.lay_AboutMe);
		lay_Side = (LinearLayout) findViewById(R.id.lay_Side);
		lay_Discover = (LinearLayout) findViewById(R.id.lay_Discover);
		lay_AppWall = (LinearLayout) findViewById(R.id.lay_AppWall);

		// 设置Tag
		lay_Group.setTag(0);
		lay_AboutMe.setTag(1);
		lay_Side.setTag(2);
		lay_Discover.setTag(3);
		lay_AppWall.setTag(4);

		mTitleViews.add(img_Group);
		mTitleViews.add(txt_AboutMe);
		mTitleViews.add(txt_Side);
		mTitleViews.add(txt_Discover);
		mTitleViews.add(txt_AppWall);
		mTitleLayoutViews.add(lay_Group);
		mTitleLayoutViews.add(lay_AboutMe);
		mTitleLayoutViews.add(lay_Side);
		mTitleLayoutViews.add(lay_Discover);
		mTitleLayoutViews.add(lay_AppWall);
		// 被选中设置字体颜色
		txt_AboutMe.setTextColor(Color.BLACK);
	}

	private void initPagerViews() {
		List<View> mListViews = new ArrayList<View>();
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mListViews.add(new ViewPager_GroupView().createView(mPagerActivity));
		mListViews.add(new ViewPager_AboutMeView().createView(mPagerActivity));
		mListViews.add(new ViewPager_SideView().createView(mPagerActivity));
		DiscoverView = new ViewPager_DiscoverView(mHandler,mViewPager);
		mListViews.add(DiscoverView.createView(mPagerActivity));
		mListViews.add(new ViewPager_AppWallView().createView(mPagerActivity));
		mViewPager.setAdapter(new MyFramePagerAdapter(mListViews));
		mViewPager.setCurrentItem(1);
		mViewPager.setOnPageChangeListener(new myOnPageChengeListener());
	}

	private void initCursor() {
		mCursor = (ImageView) findViewById(R.id.cursor);
		pngW = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_mark).getWidth();
		offset = ((mScreenWidth / count) - pngW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		// 个人主页
		matrix.postTranslate(offset * 2 + pngW, 0);
		mCursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * Title点击事件的处理
	 */
	@Override
	public void onClick(View view) {
		mViewPager.setCurrentItem((Integer) view.getTag());
	}

	private class myOnPageChengeListener implements OnPageChangeListener {
		int one = offset * 2 + pngW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			Animation animation = null;
			switch (position) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(0, -one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(one, -one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(two, -one, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(three, -one, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(-one, offset, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(one, offset, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(two, offset, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(three, offset, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(-one, one, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(-one, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				break;
			case 4:
				if (currIndex == 0) {
					animation = new TranslateAnimation(-one, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				break;
			}

			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(200);
			mCursor.startAnimation(animation);
			onChengeTitleTextViewColor(position, currIndex);
			currIndex = position;

		}

	}

	private void onChengeTitleTextViewColor(int selectPosition, int currPosition) {
		if (selectPosition != 0)
			((TextView) mTitleViews.get(selectPosition))
					.setTextColor(Color.BLACK);
		if (currPosition != 0)
			((TextView) mTitleViews.get(currPosition)).setTextColor(Color
					.parseColor("#3280c1"));
		// 设置点击事件是否启动
		mTitleLayoutViews.get(currPosition).setClickable(true);
		mTitleLayoutViews.get(selectPosition).setClickable(false);

	}

	@Override
	protected void onPause() {
		super.onPause();
		mApplication.isRun = false;
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mApplication.isRun = true;
		mHandler.sendEmptyMessageDelayed(0, sleepTime);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				DiscoverView.getViewpager_Information().setCurrentItem(
						DiscoverView.getViewpager_Information()
								.getCurrentItem() + 1, true);
				if (mApplication.isRun && !mApplication.isDown) {
					this.sendEmptyMessageDelayed(0, sleepTime);
				}
				break;

			case 1:
				if (mApplication.isRun && !mApplication.isDown) {
					this.sendEmptyMessageDelayed(0, sleepTime);
				}
				break;
			}
		}
	};
}
