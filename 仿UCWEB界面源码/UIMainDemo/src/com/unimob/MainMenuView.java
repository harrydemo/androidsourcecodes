/*
 *Author:CoffeeCole
 *Email:longkefan@foxmail.com
 *Date:2010-8-6
 */
package com.unimob;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainMenuView extends Activity {
	private PopupWindow popup;
	private ListView listView;
	private GridView toolbarGrid, mGridView, mTitleGridView;
	private LinearLayout mLayout;
	private TextView title1, title2, title3;
	private int titleIndex;
	private ViewFlipper mViewFlipper;

	/*-- Toolbar�ײ��˵�ѡ���±�--*/
	private final int TOOLBAR_ITEM_PAGEHOME = 0;// ��ҳ
	private final int TOOLBAR_ITEM_BACK = 1;// �˺�
	private final int TOOLBAR_ITEM_FORWARD = 2;// ǰ��
	private final int TOOLBAR_ITEM_NEW = 3;// ����
	private final int TOOLBAR_ITEM_MENU = 4;// �˵�

	/** �ײ��˵�ͼƬ **/
	int[] menu_toolbar_image_array = { R.drawable.controlbar_homepage,
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable, R.drawable.controlbar_window,
			R.drawable.controlbar_menu };
	/** �ײ��˵����� **/
	String[] menu_toolbar_name_array = { "��ҳ", "����", "ǰ��", "����", "�˵�" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initPopupMenu();// ��ʼ��Popup Menu�˵�
		// �����ײ��˵� Toolbar
		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		toolbarGrid.setSelector(R.drawable.toolbar_menu_item);
		toolbarGrid.setBackgroundResource(R.drawable.menu_bg2);// ���ñ���
		toolbarGrid.setNumColumns(5);// ����ÿ������
		toolbarGrid.setGravity(Gravity.CENTER);// λ�þ���
		toolbarGrid.setVerticalSpacing(10);// ��ֱ���
		toolbarGrid.setHorizontalSpacing(10);// ˮƽ���
		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array,
				menu_toolbar_image_array));// ���ò˵�Adapter
		/** �����ײ��˵�ѡ�� **/
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case TOOLBAR_ITEM_PAGEHOME:
					break;
				case TOOLBAR_ITEM_BACK:

					break;
				case TOOLBAR_ITEM_FORWARD:

					break;
				case TOOLBAR_ITEM_NEW:

					break;
				case TOOLBAR_ITEM_MENU:
					if (popup != null) {
						if (popup.isShowing()) {
							popup.dismiss();
						} else {
							popup.showAtLocation(
									findViewById(R.id.ListView_catalog),
									Gravity.BOTTOM, 0, 70);
							mViewFlipper.startFlipping();// ���Ŷ���
						}
					}
					break;
				}
			}
		});

		final int[] menu_image_array = { R.drawable.menu_search,
				R.drawable.menu_filemanager, R.drawable.menu_downmanager,
				R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
				R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
				R.drawable.menu_sharepage, R.drawable.menu_quit,
				R.drawable.menu_nightmode, R.drawable.menu_refresh,
				R.drawable.menu_more };
		listView = (ListView) findViewById(R.id.ListView_catalog);
		listView.setAdapter(getMenuAdapter(new String[] { "����1", "����2", "����3",
				"����4", "����5", "����6", "����7", "����8", "����9", "����10", "����11",
				"����12" }, menu_image_array));

	}

	/**
	 * ����Popup Menu�˵�
	 */
	private void initPopupMenu() {
		// ��������
		mViewFlipper = new ViewFlipper(this);
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.menu_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.menu_out));
		mLayout = new LinearLayout(MainMenuView.this);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		// ����ѡ����
		mTitleGridView = new GridView(MainMenuView.this);
		mTitleGridView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mTitleGridView.setSelector(R.color.alpha_00);
		mTitleGridView.setNumColumns(3);
		mTitleGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mTitleGridView.setVerticalSpacing(1);
		mTitleGridView.setHorizontalSpacing(1);
		mTitleGridView.setGravity(Gravity.CENTER);
		MenuTitleAdapter mta = new MenuTitleAdapter(this, new String[] { "����",
				"����", "����" }, 16, 0xFFFFFFFF);
		mTitleGridView.setAdapter(mta);
		mTitleGridView.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				onChangeItem(arg1, arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		mTitleGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onChangeItem(arg1, arg2);
			}
		});

		// ��ѡ����
		mGridView = new GridView(MainMenuView.this);
		mGridView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		mGridView.setSelector(R.drawable.toolbar_menu_item);
		mGridView.setNumColumns(4);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(10);
		mGridView.setHorizontalSpacing(10);
		mGridView.setPadding(10, 10, 10, 10);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setAdapter(getMenuAdapter(new String[] { "����1", "����2", "����3",
				"����4" }, new int[] { R.drawable.menu_test,
				R.drawable.menu_bookmark, R.drawable.menu_about,
				R.drawable.menu_checknet }));
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (titleIndex) {
				case 0:// ����
					// ����1
					if (arg2 == 0) {

					}
					// ����2
					if (arg2 == 1) {

					}
					// ����3
					if (arg2 == 2) {

					}
					// ����4
					if (arg2 == 3) {

					}
					break;
				case 1:// ����
					break;
				case 2:// ����
					// ����4
					if (arg2 == 3)
						popup.dismiss();
					break;
				}
			}
		});
		mLayout.addView(mTitleGridView);
		mLayout.addView(mGridView);
		mViewFlipper.addView(mLayout);
		mViewFlipper.setFlipInterval(60000);
		// ����Popup
		popup = new PopupWindow(mViewFlipper, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popup.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.menu_bg));// ����menu�˵�����
		popup.setFocusable(true);// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ
		popup.update();
		// ����Ĭ����
		title1 = (TextView) mTitleGridView.getItemAtPosition(0);
		title1.setBackgroundColor(0x00);
	}

	@Override
	/**
	 * ����MENU
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// ���봴��һ��
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * ����MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (popup != null) {
			if (popup.isShowing())
				popup.dismiss();
			else {
				popup.showAtLocation(findViewById(R.id.ListView_catalog),
						Gravity.BOTTOM, 0, 70);
				mViewFlipper.startFlipping();// ���Ŷ���
			}
		}
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}

	/**
	 * ����˵�Adapter
	 * 
	 * @param menuNameArray
	 *            ����
	 * @param imageResourceArray
	 *            ͼƬ
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

	/**
	 * �ı�ѡ�к�Ч��
	 * 
	 * @param arg1
	 *            item����
	 * @param arg2
	 *            item�±�
	 */
	private void onChangeItem(View arg1, int arg2) {
		titleIndex = arg2;
		switch (titleIndex) {
		case 0:
			title1 = (TextView) arg1;
			title1.setBackgroundColor(0x00);
			if (title2 != null)
				title2.setBackgroundResource(R.drawable.toolbar_menu_release);
			if (title3 != null)
				title3.setBackgroundResource(R.drawable.toolbar_menu_release);
			mGridView.setAdapter(getMenuAdapter(new String[] { "����1", "����2",
					"����3", "����4" }, new int[] { R.drawable.menu_test,
					R.drawable.menu_bookmark, R.drawable.menu_about,
					R.drawable.menu_checknet }));
			break;
		case 1:
			title2 = (TextView) arg1;
			title2.setBackgroundColor(0x00);
			if (title1 != null)
				title1.setBackgroundResource(R.drawable.toolbar_menu_release);
			if (title3 != null)
				title3.setBackgroundResource(R.drawable.toolbar_menu_release);
			mGridView.setAdapter(getMenuAdapter(new String[] { "����1", "����2",
					"����3", "����4" }, new int[] { R.drawable.menu_edit,
					R.drawable.menu_delete, R.drawable.menu_fullscreen,
					R.drawable.menu_help }));
			break;
		case 2:
			title3 = (TextView) arg1;
			title3.setBackgroundColor(0x00);
			if (title2 != null)
				title2.setBackgroundResource(R.drawable.toolbar_menu_release);
			if (title1 != null)
				title1.setBackgroundResource(R.drawable.toolbar_menu_release);
			mGridView.setAdapter(getMenuAdapter(new String[] { "����1", "����2",
					"����3", "����4" }, new int[] { R.drawable.menu_copy,
					R.drawable.menu_cut, R.drawable.menu_normalmode,
					R.drawable.menu_quit }));
			break;
		}
	}

	/**
	 * �Զ���Adapter
	 * 
	 * @author coffee
	 * 
	 */
	public class MenuTitleAdapter extends BaseAdapter {

		private Context mContext;
		private int fontColor;
		private TextView[] title;

		/**
		 * �����˵���
		 * 
		 * @param context
		 *            ������
		 * @param titles
		 *            ����
		 * @param fontSize
		 *            �����С
		 * @param color
		 *            ������ɫ
		 */
		public MenuTitleAdapter(Context context, String[] titles, int fontSize,
				int color) {
			this.mContext = context;
			this.fontColor = color;
			this.title = new TextView[titles.length];
			for (int i = 0; i < titles.length; i++) {
				title[i] = new TextView(mContext);
				title[i].setText(titles[i]);
				title[i].setTextSize(fontSize);
				title[i].setTextColor(fontColor);
				title[i].setGravity(Gravity.CENTER);
				title[i].setPadding(10, 10, 10, 10);
				title[i].setBackgroundResource(R.drawable.toolbar_menu_release);
			}
		}

		public int getCount() {

			return title.length;
		}

		public Object getItem(int position) {

			return title[position];
		}

		public long getItemId(int position) {

			return title[position].getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				v = title[position];
			} else {
				v = convertView;
			}
			return v;
		}

	}
}
