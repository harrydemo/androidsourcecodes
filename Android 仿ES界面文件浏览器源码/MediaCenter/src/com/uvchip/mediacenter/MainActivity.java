package com.uvchip.mediacenter;

import java.util.ArrayList;
import java.util.List;
import com.hc.module.animtab.AnimTabLayout;
import com.hc.module.animtab.AnimTabLayout.OnTabChangeListener;
import com.hc.modules.aboutus.AboutUs;
import com.uvchip.mediacenter.filebrowser.Browser;
import com.uvchip.mediacenter.filebrowser.FileBrowser;
import com.uvchip.mediacenter.filebrowser.ImageFileBrowser;
import com.uvchip.mediacenter.filebrowser.MusicFileBrowser;
import com.uvchip.mediacenter.filebrowser.VideoFileBrowser;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnPageChangeListener,
		OnTabChangeListener {
	private String TAG = "MainActivity";
	private ViewPager mViewPager;
	// private PagerTitleStrip mPagerTitleStrip;
	private FileBrowser mFileBrowser;
	private MusicFileBrowser mMusicFileBrowser;
	private VideoFileBrowser mVideoFileBrowser;
	private ImageFileBrowser mImageFileBrowser;
	private List<View> mViews;
	private List<String> mTitles;
	public static int mScreenWidth;
	private AnimTabLayout mAnimTab;
	private SDCardChangeReceiver mReceiver;
	
	private final int MENU_ABOUT_US     = Menu.FIRST;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mFileBrowser = new FileBrowser(this);
		mMusicFileBrowser = new MusicFileBrowser(this);
		mVideoFileBrowser = new VideoFileBrowser(this);
		mImageFileBrowser = new ImageFileBrowser(this);
		initView();
		mReceiver = new SDCardChangeReceiver();
	}
	@Override
	protected void onResume() {
		super.onResume();
		registerSDCardChangeReceiver();
	}
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Browser browser = getCurrBrowser();
		if (browser != null) {
			if (!browser.onBackPressed()) {
				super.onBackPressed();
			}
		}
	}

    private void registerSDCardChangeReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mReceiver, filter);
    }
	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return 400;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
//			 ((ViewPager)container).removeView(mViews.get(position % mViews.size()));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles.get(position);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(
						mViews.get(position % mViews.size()), 0);
			} catch (Exception e) {
			}
			return mViews.get(position % mViews.size());
		}
	};
	BaseAdapter tabAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(MainActivity.this);
			}
			((TextView) convertView).setText(mTitles.get(position));
			((TextView) convertView).setTextAppearance(MainActivity.this,R.style.tvTitle);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return mTitles.size();
		}
	};

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(4 * 50);
		mViewPager.setOnPageChangeListener(this);

		mViews = new ArrayList<View>();
		mViews.add(mFileBrowser.getView());
		mViews.add(mMusicFileBrowser.getView());
		mViews.add(mVideoFileBrowser.getView());
		mViews.add(mImageFileBrowser.getView());

		mTitles = new ArrayList<String>();
		mTitles.add(getString(R.string.file_browser));
		mTitles.add(getString(R.string.music_browser));
		mTitles.add(getString(R.string.video_browser));
		mTitles.add(getString(R.string.image_browser));

		mAnimTab = (AnimTabLayout) findViewById(R.id.animTab);
//		mAnimTab.setBackgroundResource(R.drawable.topbar_bg);
		mAnimTab.setAdapter(tabAdapter);
		mAnimTab.setOnTabChangeListener(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "newConfig========>" + newConfig);
		mFileBrowser.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	private Browser getCurrBrowser(){
		int index = mViewPager.getCurrentItem() % mViews.size();
		switch (index) {
		case 0:
			return mFileBrowser;
		case 1:
			return mMusicFileBrowser;
		case 2:
			return mVideoFileBrowser;
		case 3:
			return mImageFileBrowser;
		default:
			return null;
		}
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		Browser browser = getCurrBrowser();
		if (browser != null) {
			browser.onPrepareOptionsMenu(menu);
		}
		menu.add(1, MENU_ABOUT_US, Menu.NONE, R.string.menu_about_us).setIcon(android.R.drawable.ic_menu_info_details);
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Browser browser = getCurrBrowser();
		if (browser != null) {
			if (browser.onOptionsItemSelected(item)){
				return true;
			}
		}
		switch (item.getItemId()) {
        case MENU_ABOUT_US:
        	AboutUs.getAboutUsDialog(this).show();
        	return true;
        default:
            break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		mFileBrowser.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		mFileBrowser.onContextMenuClosed(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return mFileBrowser.onContextItemSelected(item);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int index) {
		index = index % mViews.size();
		mAnimTab.moveTo(index);
		switch (index) {
		case 0:
			mFileBrowser.onResume();
			break;
		case 1:
			mMusicFileBrowser.onResume();
			break;
		case 2:
			mVideoFileBrowser.onResume();
			break;
		case 3:
			mImageFileBrowser.onResume();
			break;
		default:
			break;
		}
	}

	@Override
	public void tabChange(int index) {
		int curr = mViewPager.getCurrentItem();
		int realIndex = curr % mViews.size();
		int toIndex = curr + (index - realIndex);
		Log.i(TAG, "index:" + index + " curr:" + curr + " realIndex:" + realIndex + " toIndex:" + toIndex);
		mViewPager.setCurrentItem(toIndex, false);
	}
}
