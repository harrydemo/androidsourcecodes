package com.dream.myqiyi.home;

import java.util.Timer;
import java.util.TimerTask;

import com.dream.myqiyi.R;
import com.dream.myqiyi.widget.FlowIndicator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class HomeActivity extends Activity {
	static final int SCROLL_ACTION = 0;
	ExpandableListView mExpandableListView;
	int[] tags = new int[] { 0, 0, 0, 0, 0 };
	String[] groups = new String[] { "同步剧场", "奇艺出品", "热播电影", "3月片花速递", "动漫乐园" };
	String[][] childs = new String[5][10];
	Gallery mGallery;
	GalleryAdapter mGalleryAdapter;
	FlowIndicator mMyView;
	Timer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		prepareView();
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new MyTask(), 0, 5000);
	}

	private void prepareView() {
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		MyListAdapter adapter = new MyListAdapter();
		View header = LayoutInflater.from(this).inflate(R.layout.header_view,
				null);
		mGallery = (Gallery) header.findViewById(R.id.home_gallery);
		mMyView = (FlowIndicator) header.findViewById(R.id.myView);
		mGalleryAdapter = new GalleryAdapter(this);
		mMyView.setCount(mGalleryAdapter.getCount());
		mGallery.setAdapter(mGalleryAdapter);
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mMyView.setSeletion(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		mExpandableListView.addHeaderView(header);
		mExpandableListView.setAdapter(adapter);
		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int arg0) {
						// TODO Auto-generated method stub
						tags[arg0] = 1;
					}
				});
		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						tags[arg0] = 0;
					}
				});
	}

	private class MyTask extends TimerTask {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(SCROLL_ACTION);
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SCROLL_ACTION:
				// int curPos = mGallery.getSelectedItemPosition();
				// if (curPos < mGalleryAdapter.getCount() - 1) {
				// curPos++;
				// } else {
				// curPos = 0;
				// }
				// // mGallery.setLayoutAnimation(new LayoutAnimationController(
				// // AnimationUtils.loadAnimation(HomeActivity.this,
				// // R.anim.gallery_in)));
				// mGallery.setSelection(curPos, true);
				MotionEvent e1 = MotionEvent.obtain(SystemClock.uptimeMillis(),
						SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
						89.333336f, 265.33334f, 0);
				MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
						SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
						300.0f, 238.00003f, 0);

				mGallery.onFling(e1, e2, -1300, 0);
				break;

			default:
				break;
			}
		}
	};

	class MyListAdapter extends BaseExpandableListAdapter {

		public MyListAdapter() {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 10; j++) {
					childs[i][j] = "child" + i + "_" + j;
				}
			}
		}

		@Override
		public String getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return childs[arg0][arg1];
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO Auto-generated method stub
			if (arg3 == null) {
				arg3 = LayoutInflater.from(HomeActivity.this).inflate(
						R.layout.list_child_item, null);
			}
			return arg3;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return groups[arg0];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.length;
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		class GroupHolder {
			ImageView img;
			TextView title;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			GroupHolder groupHolder;
			if (arg2 == null) {
				arg2 = LayoutInflater.from(HomeActivity.this).inflate(
						R.layout.list_group_item, null);
				groupHolder = new GroupHolder();
				groupHolder.img = (ImageView) arg2.findViewById(R.id.tag_img);
				groupHolder.title = (TextView) arg2
						.findViewById(R.id.title_view);
				arg2.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) arg2.getTag();
			}
			if (tags[arg0] == 0) {
				groupHolder.img
						.setImageResource(R.drawable.list_indecator_button);
			} else {
				groupHolder.img
						.setImageResource(R.drawable.list_indecator_button_down);
			}
			groupHolder.title.setText(groups[arg0]);

			return arg2;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

	}

}
