package tjuci.dl.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollUIActivity extends Activity {
	// page2的变量
	LinearLayout images_layout;// page2中images的线性布局
	ImageView[] images_point;// 存放布局中的所有image点
	int[] images;// 存放所有图片
	int mCurImage = 1;// 当前的image
	ImageView page_image;// 被换的image

	// page3的变量
	ExpandableListView expandable;
	// 对于ExpandableListView的一级目录
	String[] oneLayer = new String[] { "财务管理", "物资管理", "项目管理", "法律事务管理系统",
			"设备管理", "人力资源" };
	// 对于ExpandableListView的二级目录
	String[][] twoLayer = new String[][] {
			{ "财务管理1", "财务管理2", "财务管理3", "财务管理4", "财务管理5", "财务管理6", "财务管理7" },
			{ "物资管理1", "物资管理2", "物资管理3", "物资管理4", "物资管理5", "物资管理6", "物资管理7",
					"物资管理8", "物资管理9", "物资管理10", "物资管理11", "物资管理12", }, {}, {},
			{}, {} };
	int[] GroupImageId = { R.drawable.page3_image1, R.drawable.page3_image2,
			R.drawable.page3_image3, R.drawable.page3_image4,
			R.drawable.page3_image5, R.drawable.page3_image6 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		page2Event();// 处理page2的事件
		page3Event();// 处理page3的事件
	}

	/**
	 * 处理page3页面的所有事件
	 */
	public void page3Event() {
		// 初始化变量
		expandable = (ExpandableListView) findViewById(R.id.page3_expandable);
		expandable.setGroupIndicator(null);// 取消一级图标的箭头
		ExpandableListAdapter ea = new BaseExpandableListAdapter() {
			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				LinearLayout ll = new LinearLayout(ScrollUIActivity.this);
				ll.setLayoutParams(new AbsListView.LayoutParams(600,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				ll.setPadding(20, 5, 5, 5);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ImageView iv = new ImageView(ScrollUIActivity.this);
				iv.setImageResource(GroupImageId[groupPosition]);
				ll.addView(iv);
				TextView tv = new TextView(ScrollUIActivity.this);
				tv.setLayoutParams(new AbsListView.LayoutParams(500,
						AbsListView.LayoutParams.WRAP_CONTENT));
				tv.setText(oneLayer[groupPosition]);
				ll.addView(tv);
				return ll;
			}

			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return oneLayer.length;
			}

			@Override
			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return oneLayer[groupPosition];
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return twoLayer[groupPosition].length;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				TextView tv = new TextView(ScrollUIActivity.this);
				tv.setText(twoLayer[groupPosition][childPosition]);
				return tv;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return twoLayer[groupPosition][childPosition];
			}
		};
		expandable.setAdapter(ea);
	}

	/**
	 * 处理page2页面的所有事件
	 */
	public void page2Event() {
		// page2中变量的定义 与 赋值
		images_layout = (LinearLayout) findViewById(R.id.page2_images_layout);
		images_point = new ImageView[images_layout.getChildCount()];
		page_image = (ImageView) findViewById(R.id.page2_image);
		images = new int[] { R.drawable.page2_image1, R.drawable.page2_image2,
				R.drawable.page2_image3, R.drawable.page2_image4 };

		setScrollerImagePoint();// 初始化 page2中的 滚动点

		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					if (mCurImage > images.length - 1) {// 如果大于图片的数目 那么重新开始加载
						mCurImage = 0;
					}
					setScrollerImagePoint();
					page_image.setAnimation(AnimationUtils.loadAnimation(
							ScrollUIActivity.this, android.R.anim.fade_out));
					page_image.setImageResource(images[mCurImage]);
					page_image.setAnimation(AnimationUtils.loadAnimation(
							ScrollUIActivity.this, android.R.anim.fade_in));
					mCurImage++;
				}
			}
		};

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					h.sendEmptyMessage(0x123);
				}
			}
		}, 0, 500);
	}

	/*
	 * 为page2设置滚动点的效果
	 */
	public void setScrollerImagePoint() {
		for (int i = 0; i < images_layout.getChildCount(); i++) {
			images_point[i] = (ImageView) images_layout.getChildAt(i);
			images_point[i].setEnabled(false);
		}
		images_point[mCurImage].setEnabled(true);// 设置可用的点 图片
	}
}