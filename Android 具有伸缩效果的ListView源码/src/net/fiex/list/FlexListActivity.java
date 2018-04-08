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
	 * ��ʼ������
	 */
	private void init() {
		contentDefileList = new ArrayList<Map<String, String>>();

		Map<String, String> contentDefile = new HashMap<String, String>();
		contentDefile.put(CONTENT_DETAIL_TITLE, "���Ƕ������˵���");
		contentDefile.put(CONTENT_STATUS_IMG, "true");
		contentDefile.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile.put(CONTENT_NAME, "�ű���������������̷����������");
		contentDefile.put(CONTENT_DETAIL, "      �������������Ƕ���һȺ���˵��ˣ���Ϊ������ϧ��"
				+ "��������һ�����������壬�ͽ�ȫ��ͷ�ԣ�����������������һ�⣬���������ޱȵ��ˡ�");
		contentDefile.put(CONTENT_HEADNAME, "����");

		Map<String, String> contentDefile_1 = new HashMap<String, String>();
		contentDefile_1.put(CONTENT_DETAIL_TITLE, "����Ĵ����");
		contentDefile_1.put(CONTENT_STATUS_IMG, "false");
		contentDefile_1.put(CONTENT_START_TIME, "2011-03-11");
		contentDefile_1.put(CONTENT_END_TIME, "2011-05-11");
		contentDefile_1.put(CONTENT_NAME, "����������");
		contentDefile_1.put(CONTENT_DETAIL, "      ��֪��Ϊʲô������������У�"
				+ "�Ҳ����κλ��ŵ����壬��Ϊ�Ҳ����ף�������������塣");
		contentDefile_1.put(CONTENT_HEADNAME, "����");

		Map<String, String> contentDefile_2 = new HashMap<String, String>();
		contentDefile_2.put(CONTENT_DETAIL_TITLE, "�����е�����");
		contentDefile_2.put(CONTENT_STATUS_IMG, "false");
		contentDefile_2.put(CONTENT_START_TIME, "2010-11-02");
		contentDefile_2.put(CONTENT_END_TIME, "2010-12-01");
		contentDefile_2.put(CONTENT_NAME, "��������ʣ�����");
		contentDefile_2.put(CONTENT_DETAIL, "      ��������԰�����ȴ�ܶ��������Ķ��С�");
		contentDefile_2.put(CONTENT_HEADNAME, "���ʵ�");

		Map<String, String> contentDefile_3 = new HashMap<String, String>();
		contentDefile_3.put(CONTENT_DETAIL_TITLE, "��֪��Ϊʲô");
		contentDefile_3.put(CONTENT_STATUS_IMG, "true");
		contentDefile_3.put(CONTENT_START_TIME, "2011-01-21");
		contentDefile_3.put(CONTENT_END_TIME, "2011-06-27");
		contentDefile_3.put(CONTENT_NAME, "ë��");
		contentDefile_3.put(CONTENT_DETAIL, "     ��˵��Ŀ�����");
		contentDefile_3.put(CONTENT_HEADNAME, "δ֪");

		Map<String, String> contentDefile_4 = new HashMap<String, String>();
		contentDefile_4.put(CONTENT_DETAIL_TITLE, "�������޴�");
		contentDefile_4.put(CONTENT_STATUS_IMG, "false");
		contentDefile_4.put(CONTENT_START_TIME, "2001-07-11");
		contentDefile_4.put(CONTENT_END_TIME, "2001-05-21");
		contentDefile_4.put(CONTENT_NAME, "������");
		contentDefile_4.put(CONTENT_DETAIL, "      ���������ĺܴ���ɽ��ˮ��ɽ����ԭ�����������ĵط�"
				+ "���ȴ�����ȥ̽����\r\n���������ĺܴ������黭����ʳס�У�ÿһ�����Ŷ��ص�����"
				+ "��������ȥѧϰ��\r\n������磬��ĺܴ�");
		contentDefile_4.put(CONTENT_HEADNAME, "��");

		Map<String, String> contentDefile_5 = new HashMap<String, String>();
		contentDefile_5.put(CONTENT_DETAIL_TITLE, "����");
		contentDefile_5.put(CONTENT_STATUS_IMG, "true");
		contentDefile_5.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_5.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_5.put(CONTENT_NAME, "����");
		contentDefile_5.put(CONTENT_DETAIL, "      ���㱧��һ�������־�ᶨ������ȥ��������ħ��"
				+ "Ҳ����˵ʲô����ϧ���̫�������Բп����ʵ����ʧ����������˵�����ôȥ̸���롣");
		contentDefile_5.put(CONTENT_HEADNAME, "����");

		Map<String, String> contentDefile_6 = new HashMap<String, String>();
		contentDefile_6.put(CONTENT_DETAIL_TITLE, "ĥ����");
		contentDefile_6.put(CONTENT_STATUS_IMG, "true");
		contentDefile_6.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_6.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_6.put(CONTENT_NAME, "ʹ�࣬����");
		contentDefile_6.put(CONTENT_DETAIL, "      ���������������Ļ���Ҹ��ǽ�������������֮�ϵģ�"
				+ "ÿÿ����仰������ú̶ܴ�������ԭ������Ϊ�Ҹ�֮�����Ҹ�������Ϊ���������꣬"
				+ "���������Ϊ�����ͱڣ��ž���ƽԭ�Ĺ�������Ϊ���˸ɺ����ſ�����ˮ������");
		contentDefile_6.put(CONTENT_HEADNAME, "�Ҹ�");

		Map<String, String> contentDefile_7 = new HashMap<String, String>();
		contentDefile_7.put(CONTENT_DETAIL_TITLE, "���Ƕ������˵���");
		contentDefile_7.put(CONTENT_STATUS_IMG, "true");
		contentDefile_7.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_7.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_7.put(CONTENT_NAME, "�ű���������������̷����������");
		contentDefile_7.put(CONTENT_DETAIL, "      �������������Ƕ���һȺ���˵��ˣ���Ϊ������ϧ��"
				+ "��������һ�����������壬�ͽ�ȫ��ͷ�ԣ�����������������һ�⣬���������ޱȵ��ˡ�");
		contentDefile_7.put(CONTENT_HEADNAME, "����");

		Map<String, String> contentDefile_8 = new HashMap<String, String>();
		contentDefile_8.put(CONTENT_DETAIL_TITLE, "���Ƕ������˵���");
		contentDefile_8.put(CONTENT_STATUS_IMG, "true");
		contentDefile_8.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_8.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_8.put(CONTENT_NAME, "�ű���������������̷����������");
		contentDefile_8.put(CONTENT_DETAIL, "      �������������Ƕ���һȺ���˵��ˣ���Ϊ������ϧ��"
				+ "��������һ�����������壬�ͽ�ȫ��ͷ�ԣ�����������������һ�⣬���������ޱȵ��ˡ�");
		contentDefile_8.put(CONTENT_HEADNAME, "����");

		Map<String, String> contentDefile_9 = new HashMap<String, String>();
		contentDefile_9.put(CONTENT_DETAIL_TITLE, "���Ƕ������˵���");
		contentDefile_9.put(CONTENT_STATUS_IMG, "true");
		contentDefile_9.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_9.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_9.put(CONTENT_NAME, "�ű���������������̷����������");
		contentDefile_9.put(CONTENT_DETAIL, "      �������������Ƕ���һȺ���˵��ˣ���Ϊ������ϧ��"
				+ "��������һ�����������壬�ͽ�ȫ��ͷ�ԣ�����������������һ�⣬���������ޱȵ��ˡ�");
		contentDefile_9.put(CONTENT_HEADNAME, "����");

		Map<String, String> contentDefile_0 = new HashMap<String, String>();
		contentDefile_0.put(CONTENT_DETAIL_TITLE, "���Ƕ������˵���");
		contentDefile_0.put(CONTENT_STATUS_IMG, "true");
		contentDefile_0.put(CONTENT_START_TIME, "2011-04-11");
		contentDefile_0.put(CONTENT_END_TIME, "2011-05-21");
		contentDefile_0.put(CONTENT_NAME, "�ű���������������̷����������");
		contentDefile_0.put(CONTENT_DETAIL, "      �������������Ƕ���һȺ���˵��ˣ���Ϊ������ϧ��"
				+ "��������һ�����������壬�ͽ�ȫ��ͷ�ԣ�����������������һ�⣬���������ޱȵ��ˡ�");
		contentDefile_0.put(CONTENT_HEADNAME, "����");

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
		 * ����һ����������Ч����LinearLayout
		 * 
		 * @param context
		 * @param contextDefail
		 *            ������ϸ
		 * @param position
		 *            ���������б��λ��
		 * @param isCurrentItem
		 *            �Ƿ�Ϊ��չ
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
		 * ���ø��е�״̬����ʽ
		 * 
		 * @param contentDefail
		 *            ������ϸ
		 * @param position
		 *            ���������б��λ��
		 * @param isCurrentItem
		 *            �Ƿ�Ϊ��չ
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
	 * ��������״̬�Ի���
	 * 
	 * @param stateID
	 *            ��ǰ״̬���к�
	 */
	private void alterStateDialog(int stateID) {
		/* ������״̬�������Ϊ�㷨��Ҫ�������㷨���Լ����� */
		String[] stateAll = { "δ��", "����", "�ܾ�", "���", "����" };

		if (3 == stateID) {
			Toast.makeText(FlexListActivity.this, "�����������", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String[] states = new String[2];
		for (int i = 0; i < 2; i++) {
			states[i] = stateAll[(stateID + i + 1)];
		}

		new AlertDialog.Builder(this).setTitle("��ǰ״̬��" + stateAll[stateID])
				.setItems(states, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setNegativeButton("ȡ��", null).show();
	}
}