package net.fiex.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class FlexListActivity extends ListActivity implements OnScrollListener,
		OnItemClickListener {
	private static String LAYOUT_INFLATER_SERVICE = Context.LAYOUT_INFLATER_SERVICE;

	public static final String CONTENT_DETAIL_TITLE = "content_detail_title";
	public static final String CONTENT_STATUS_IMG = "content_status_img";
	public static final String CONTENT_START_TIME = "content_start_time";
	public static final String CONTENT_END_TIME = "content_end_time";
	public static final String CONTENT_NAME = "content_name";
	public static final String CONTENT_DETAIL = "content_detail";
	public static final String CONTENT_HEADNAME = "content_headname";

	private LayoutInflater mInflater;

	private FlexListAdapter adapter;

	private List<Map<String, String>> contentDefileList;
	private boolean[] isCurrentItems;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		init();
		setListAdapter(adapter);
		getListView().setBackgroundColor(Color.WHITE);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setDivider(
				getResources().getDrawable(R.color.transparent));
		getListView().setSelector(R.drawable.work_detail_click_bg);
		getListView().setOnScrollListener(this);
		getListView().setOnItemClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		contentDefileList = new ArrayList<Map<String, String>>();

		Map<String, String> contentDefile = new HashMap<String, String>();
		contentDefile.put(CONTENT_DETAIL_TITLE, "我们都是幸运的人");
		contentDefile.put(CONTENT_STATUS_IMG, "true");
		contentDefile.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile.put(CONTENT_NAME, "张北，李西，王东，谭北，居中正");
		contentDefile.put(CONTENT_DETAIL, "      在这个世界里，我们都是一群幸运的人，因为上天怜惜，"
				+ "赐与我们一副完整的身体，和健全的头脑，让我们在这世上走一遭，当是幸运无比的人。");
		contentDefile.put(CONTENT_HEADNAME, "国政");

		Map<String, String> contentDefile_1 = new HashMap<String, String>();
		contentDefile_1.put(CONTENT_DETAIL_TITLE, "生活的大城市");
		contentDefile_1.put(CONTENT_STATUS_IMG, "false");
		contentDefile_1.put(CONTENT_START_TIME, "2011-03-11");
		contentDefile_1.put(CONTENT_END_TIME, "2011-05-11");
		contentDefile_1.put(CONTENT_NAME, "黑三，王二");
		contentDefile_1.put(CONTENT_DETAIL, "      不知道为什么，我在这个城市，"
				+ "找不到任何活着的意义，因为我不明白，我在这里的意义。");
		contentDefile_1.put(CONTENT_HEADNAME, "张零");

		Map<String, String> contentDefile_2 = new HashMap<String, String>();
		contentDefile_2.put(CONTENT_DETAIL_TITLE, "向往中的生活");
		contentDefile_2.put(CONTENT_STATUS_IMG, "false");
		contentDefile_2.put(CONTENT_START_TIME, "2010-11-02");
		contentDefile_2.put(CONTENT_END_TIME, "2010-12-01");
		contentDefile_2.put(CONTENT_NAME, "刘王，齐皇，塞帝");
		contentDefile_2.put(CONTENT_DETAIL, "      向往着田园的生活，却奋斗在喧嚣的都市。");
		contentDefile_2.put(CONTENT_HEADNAME, "王皇帝");

		Map<String, String> contentDefile_3 = new HashMap<String, String>();
		contentDefile_3.put(CONTENT_DETAIL_TITLE, "不知道为什么");
		contentDefile_3.put(CONTENT_STATUS_IMG, "true");
		contentDefile_3.put(CONTENT_START_TIME, "2011-01-21");
		contentDefile_3.put(CONTENT_END_TIME, "2011-06-27");
		contentDefile_3.put(CONTENT_NAME, "毛东");
		contentDefile_3.put(CONTENT_DETAIL, "     你说真的可以吗？");
		contentDefile_3.put(CONTENT_HEADNAME, "未知");

		Map<String, String> contentDefile_4 = new HashMap<String, String>();
		contentDefile_4.put(CONTENT_DETAIL_TITLE, "世界无限大");
		contentDefile_4.put(CONTENT_STATUS_IMG, "false");
		contentDefile_4.put(CONTENT_START_TIME, "2001-07-11");
		contentDefile_4.put(CONTENT_END_TIME, "2001-05-21");
		contentDefile_4.put(CONTENT_NAME, "中美日");
		contentDefile_4.put(CONTENT_DETAIL, "      这个世界真的很大，青山绿水，山川高原，有着无数的地方"
				+ "，等待我们去探索。\r\n这个世界真的很大，琴棋书画，衣食住行，每一样都着独特的魅力"
				+ "，让我们去学习。\r\n这个世界，真的很大。");
		contentDefile_4.put(CONTENT_HEADNAME, "乡");

		Map<String, String> contentDefile_5 = new HashMap<String, String>();
		contentDefile_5.put(CONTENT_DETAIL_TITLE, "信念");
		contentDefile_5.put(CONTENT_STATUS_IMG, "true");
		contentDefile_5.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_5.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_5.put(CONTENT_NAME, "信念");
		contentDefile_5.put(CONTENT_DETAIL, "      当你抱着一个信念，意志坚定的走下去，即便神魔，"
				+ "也不敢说什么，可惜如今太多的人面对残酷的现实，丢失了信仰，如此的你怎么去谈梦想。");
		contentDefile_5.put(CONTENT_HEADNAME, "信仰");

		Map<String, String> contentDefile_6 = new HashMap<String, String>();
		contentDefile_6.put(CONTENT_DETAIL_TITLE, "磨和难");
		contentDefile_6.put(CONTENT_STATUS_IMG, "true");
		contentDefile_6.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_6.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_6.put(CONTENT_NAME, "痛苦，挫折");
		contentDefile_6.put(CONTENT_DETAIL, "      曾常常听到这样的话语，幸福是建立在物质需求之上的，"
				+ "每每听这句话，便觉得很刺耳，终其原因，是因为幸福之所以幸福，是因为经历过风雨，"
				+ "这个世界因为有了峭壁，才觉得平原的广阔，因为有了干旱，才渴望雨水的滋润。");
		contentDefile_6.put(CONTENT_HEADNAME, "幸福");

		Map<String, String> contentDefile_7 = new HashMap<String, String>();
		contentDefile_7.put(CONTENT_DETAIL_TITLE, "我们都是幸运的人");
		contentDefile_7.put(CONTENT_STATUS_IMG, "true");
		contentDefile_7.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_7.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_7.put(CONTENT_NAME, "张北，李西，王东，谭北，居中正");
		contentDefile_7.put(CONTENT_DETAIL, "      在这个世界里，我们都是一群幸运的人，因为上天怜惜，"
				+ "赐与我们一副完整的身体，和健全的头脑，让我们在这世上走一遭，当是幸运无比的人。");
		contentDefile_7.put(CONTENT_HEADNAME, "国政");

		Map<String, String> contentDefile_8 = new HashMap<String, String>();
		contentDefile_8.put(CONTENT_DETAIL_TITLE, "我们都是幸运的人");
		contentDefile_8.put(CONTENT_STATUS_IMG, "true");
		contentDefile_8.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_8.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_8.put(CONTENT_NAME, "张北，李西，王东，谭北，居中正");
		contentDefile_8.put(CONTENT_DETAIL, "      在这个世界里，我们都是一群幸运的人，因为上天怜惜，"
				+ "赐与我们一副完整的身体，和健全的头脑，让我们在这世上走一遭，当是幸运无比的人。");
		contentDefile_8.put(CONTENT_HEADNAME, "国政");

		Map<String, String> contentDefile_9 = new HashMap<String, String>();
		contentDefile_9.put(CONTENT_DETAIL_TITLE, "我们都是幸运的人");
		contentDefile_9.put(CONTENT_STATUS_IMG, "true");
		contentDefile_9.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_9.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_9.put(CONTENT_NAME, "张北，李西，王东，谭北，居中正");
		contentDefile_9.put(CONTENT_DETAIL, "      在这个世界里，我们都是一群幸运的人，因为上天怜惜，"
				+ "赐与我们一副完整的身体，和健全的头脑，让我们在这世上走一遭，当是幸运无比的人。");
		contentDefile_9.put(CONTENT_HEADNAME, "国政");

		Map<String, String> contentDefile_0 = new HashMap<String, String>();
		contentDefile_0.put(CONTENT_DETAIL_TITLE, "我们都是幸运的人");
		contentDefile_0.put(CONTENT_STATUS_IMG, "true");
		contentDefile_0.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_0.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_0.put(CONTENT_NAME, "张北，李西，王东，谭北，居中正");
		contentDefile_0.put(CONTENT_DETAIL, "      在这个世界里，我们都是一群幸运的人，因为上天怜惜，"
				+ "赐与我们一副完整的身体，和健全的头脑，让我们在这世上走一遭，当是幸运无比的人。");
		contentDefile_0.put(CONTENT_HEADNAME, "国政");

		contentDefileList.add(contentDefile);
		contentDefileList.add(contentDefile_1);
		contentDefileList.add(contentDefile_2);
		contentDefileList.add(contentDefile_3);
		contentDefileList.add(contentDefile_4);
		contentDefileList.add(contentDefile_5);
		contentDefileList.add(contentDefile_6);
		contentDefileList.add(contentDefile_7);
		contentDefileList.add(contentDefile_8);
		contentDefileList.add(contentDefile_9);
		contentDefileList.add(contentDefile_0);

		isCurrentItems = new boolean[contentDefileList.size()];

		for (int i = 0; i < isCurrentItems.length; i++) {
			isCurrentItems[i] = false;
		}

		Log.w("TAG", "AutoLoadActivity init() =========>>>>>> come in.");
		adapter = new FlexListAdapter();
	}

	public void onScroll(AbsListView v, int i, int j, int k) {
	}

	public void onScrollStateChanged(AbsListView v, int state) {
		if (state == OnScrollListener.SCROLL_STATE_IDLE) {
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isCurrentItems[position]) {
			isCurrentItems[position] = false;
		} else {
			isCurrentItems[position] = true;
		}
		adapter.notifyDataSetChanged();
	}

	class FlexListAdapter extends BaseAdapter {
		int count = contentDefileList.size();

		public int getCount() {
			return count;
		}

		public Object getItem(int pos) {
			return pos;
		}

		public long getItemId(int pos) {
			return pos;
		}

		public View getView(int pos, View v, ViewGroup p) {
			FlexLinearLayout view = null;
			if (null == v) {
				view = new FlexLinearLayout(FlexListActivity.this,
						contentDefileList.get(pos), pos, false);
			} else {
				view = (FlexLinearLayout) v;
				view.setWorkTitleLayout(contentDefileList.get(pos), pos,
						isCurrentItems[pos]);
			}
			return view;
		}
	}

	public class FlexLinearLayout extends LinearLayout {
		public static final int BULE = 0xFF3D8CB8;

		private LinearLayout layout;
		private RelativeLayout contentTitleLayout;
		private LinearLayout contentDetailLayout;

		private TextView contentDetailTitleText;
		private ImageView contentStatusImgView;
		private TextView contentStartTimeText;
		private TextView contentEndTimeText;
		private TextView contentNameText;
		private TextView contentDefailText;
		private TextView contentHeadnameText;

		/**
		 * 创建一个带有伸缩效果的LinearLayout
		 * 
		 * @param context
		 * @param contextDefail
		 *            内容详细
		 * @param position
		 *            该列所在列表的位置
		 * @param isCurrentItem
		 *            是否为伸展
		 */
		public FlexLinearLayout(Context context,
				final Map<String, String> contextDefail, final int position,
				boolean isCurrentItem) {
			super(context);

			layout = (LinearLayout) mInflater.inflate(
					R.layout.work_detail_row_layout, null);
			contentTitleLayout = (RelativeLayout) layout
					.findViewById(R.id.workTitleLayout);
			contentDetailLayout = (LinearLayout) layout
					.findViewById(R.id.workDetailLayout);

			contentDetailTitleText = (TextView) layout
					.findViewById(R.id.workDetailTitle);
			contentStatusImgView = (ImageView) layout
					.findViewById(R.id.workStatusImg);
			contentStartTimeText = (TextView) layout
					.findViewById(R.id.workStartTime);
			contentEndTimeText = (TextView) layout
					.findViewById(R.id.workEndTime);
			contentNameText = (TextView) layout.findViewById(R.id.workName);
			contentDefailText = (TextView) layout.findViewById(R.id.workDetail);
			contentHeadnameText = (TextView) layout
					.findViewById(R.id.workHeadname);

			this.addView(layout);
			setWorkTitleLayout(contextDefail, position, isCurrentItem);
		}

		/**
		 * 设置该列的状态及样式
		 * 
		 * @param contentDefail
		 *            内容详细
		 * @param position
		 *            该列所在列表的位置
		 * @param isCurrentItem
		 *            是否为伸展
		 */
		public void setWorkTitleLayout(final Map<String, String> contentDefail,
				final int position, boolean isCurrentItem) {

			contentTitleLayout
					.setBackgroundResource((position % 2 == 1) ? R.drawable.title_1
							: R.drawable.title_2);

			contentDetailTitleText.setText(contentDefail
					.get(CONTENT_DETAIL_TITLE));

			contentDetailTitleText.setTextColor((position % 2 == 0) ? BULE
					: Color.WHITE);

			contentStatusImgView
					.setImageResource((contentDefail.get(CONTENT_STATUS_IMG)
							.equals("true")) ? R.drawable.onebit_34
							: R.drawable.onebit_33);

			contentStatusImgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (contentDefail.get(CONTENT_STATUS_IMG).equals("true")) {
						Random random = new Random();
						int stateID = random.nextInt(4);

						alterStateDialog(stateID);
					} else {
						contentDefail.put(CONTENT_STATUS_IMG, "true");
						contentDefileList.remove(position);
						contentDefileList.add(position, contentDefail);

						adapter.notifyDataSetChanged();
					}
				}
			});

			if (isCurrentItem) {
				Log
						.d("TAG", "isCurrentItem ============>>>>>>>>>  "
								+ position);

				contentStartTimeText.setText(contentDefail
						.get(CONTENT_START_TIME));
				contentEndTimeText.setText(contentDefail.get(CONTENT_END_TIME));
				contentNameText.setText(contentDefail.get(CONTENT_NAME));
				contentDefailText.setText(contentDefail.get(CONTENT_DETAIL));
				contentHeadnameText
						.setText(contentDefail.get(CONTENT_HEADNAME));
			}

			contentDetailLayout.setVisibility(isCurrentItem ? VISIBLE : GONE);
		}
	}

	/**
	 * 更改任务状态对话框
	 * 
	 * @param stateID
	 *            当前状态序列号
	 */
	private void alterStateDialog(int stateID) {
		/* 共四种状态，第五个为算法需要，具体算法请自己推算 */
		String[] stateAll = { "未读", "接收", "拒绝", "完成", "接收" };

		if (3 == stateID) {
			Toast.makeText(FlexListActivity.this, "该任务已完成", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String[] states = new String[2];
		for (int i = 0; i < 2; i++) {
			states[i] = stateAll[(stateID + i + 1)];
		}

		new AlertDialog.Builder(this).setTitle("当前状态：" + stateAll[stateID])
				.setItems(states, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setNegativeButton("取消", null).show();
	}
}