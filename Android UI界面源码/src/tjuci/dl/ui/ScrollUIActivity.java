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
	// page2�ı���
	LinearLayout images_layout;// page2��images�����Բ���
	ImageView[] images_point;// ��Ų����е�����image��
	int[] images;// �������ͼƬ
	int mCurImage = 1;// ��ǰ��image
	ImageView page_image;// ������image

	// page3�ı���
	ExpandableListView expandable;
	// ����ExpandableListView��һ��Ŀ¼
	String[] oneLayer = new String[] { "�������", "���ʹ���", "��Ŀ����", "�����������ϵͳ",
			"�豸����", "������Դ" };
	// ����ExpandableListView�Ķ���Ŀ¼
	String[][] twoLayer = new String[][] {
			{ "�������1", "�������2", "�������3", "�������4", "�������5", "�������6", "�������7" },
			{ "���ʹ���1", "���ʹ���2", "���ʹ���3", "���ʹ���4", "���ʹ���5", "���ʹ���6", "���ʹ���7",
					"���ʹ���8", "���ʹ���9", "���ʹ���10", "���ʹ���11", "���ʹ���12", }, {}, {},
			{}, {} };
	int[] GroupImageId = { R.drawable.page3_image1, R.drawable.page3_image2,
			R.drawable.page3_image3, R.drawable.page3_image4,
			R.drawable.page3_image5, R.drawable.page3_image6 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		page2Event();// ����page2���¼�
		page3Event();// ����page3���¼�
	}

	/**
	 * ����page3ҳ��������¼�
	 */
	public void page3Event() {
		// ��ʼ������
		expandable = (ExpandableListView) findViewById(R.id.page3_expandable);
		expandable.setGroupIndicator(null);// ȡ��һ��ͼ��ļ�ͷ
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
	 * ����page2ҳ��������¼�
	 */
	public void page2Event() {
		// page2�б����Ķ��� �� ��ֵ
		images_layout = (LinearLayout) findViewById(R.id.page2_images_layout);
		images_point = new ImageView[images_layout.getChildCount()];
		page_image = (ImageView) findViewById(R.id.page2_image);
		images = new int[] { R.drawable.page2_image1, R.drawable.page2_image2,
				R.drawable.page2_image3, R.drawable.page2_image4 };

		setScrollerImagePoint();// ��ʼ�� page2�е� ������

		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					if (mCurImage > images.length - 1) {// �������ͼƬ����Ŀ ��ô���¿�ʼ����
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
	 * Ϊpage2���ù������Ч��
	 */
	public void setScrollerImagePoint() {
		for (int i = 0; i < images_layout.getChildCount(); i++) {
			images_point[i] = (ImageView) images_layout.getChildAt(i);
			images_point[i].setEnabled(false);
		}
		images_point[mCurImage].setEnabled(true);// ���ÿ��õĵ� ͼƬ
	}
}