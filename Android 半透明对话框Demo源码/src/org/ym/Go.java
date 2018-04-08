package org.ym;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author ţ��
 * 
 */
public class Go extends Activity {
	private Button dialog;
	private Button popupwin;
	private String[] menu_name_array = { "����", "�ļ�����", "���ع���", "ȫ��", "��ַ",
			"��ǩ", "������ǩ", "����ҳ��", "�˳�", "ҹ��ģʽ", "ˢ��", "�ر�" };
	int[] menu_image_array = { R.drawable.menu_search,
			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
			R.drawable.menu_sharepage, R.drawable.menu_quit,
			R.drawable.menu_nightmode, R.drawable.menu_refresh,
			R.drawable.menu_more };
	private GridView menuGrid;
	private PopupWindow popupWindow;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dialog = (Button) findViewById(R.id.dialog);
		popupwin = (Button) findViewById(R.id.popup);
		dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialog();
			}
		});
		popupwin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openPopupwin();
			}
		});
	}

	private void openDialog() {
		View menuView = View.inflate(this, R.layout.gridview_menu, null);
		// ����AlertDialog
		final AlertDialog menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 11) {
					menuDialog.cancel();
				}
			}
		});
		menuDialog.show();
	}

	private ListAdapter getMenuAdapter(String[] menuNameArray,
			int[] menuImageArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", menuImageArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;

	}

	private void openPopupwin() {
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.gridview_pop, null, true);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		menuGrid.requestFocus();
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 11) {
					popupWindow.dismiss();
				}
			}
		});
		menuGrid.setOnKeyListener(new OnKeyListener() {// ���㵽��gridview�ϣ�������Ҫ�����˴��ļ����¼����������ֲ���Ӧ�����¼������
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						switch (keyCode) {
						case KeyEvent.KEYCODE_MENU:
							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							}
							break;
						}
						System.out.println("menuGridfdsfdsfdfd");
						return true;
					}
				});
		popupWindow = new PopupWindow(menuView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.showAtLocation(findViewById(R.id.parent), Gravity.CENTER
				| Gravity.CENTER, 0, 0);
		popupWindow.update();
	}

	/**
	 * ColorDrawable dw = new ColorDrawable(-00000);
	 * popupWindow.setBackgroundDrawable(dw);
	 * �������˸�ʾ�����������������оͲ��õ���dismiss���������֮��Ĳ�λ�����߰�back�����ܹرմ��ڡ� ����������д�ˣ����ǲ��С�
	 * ���Ҿ�Ȼ���񲻵������¼������ߣ�ϣ���ĸ�����������������ң�лл�� ytdcr@tom.com �������ˣ�����Ϊû���ñ�����ԭ��
	 * popupWindow.setBackgroundDrawable(new BitmapDrawable());
	 * //����һ�з���showAtLocationǰ������ˣ���ǰ�Ƿ��ں���ģ������ˡ�
	 * popupWindow.showAtLocation(findViewById(R.id.parent), Gravity.CENTER |
	 * Gravity.CENTER, 0, 0); ����Ҳ�кܶ���˵������pop֮�󣬲���Ӧ�����¼��ˣ������ʵ�ǽ�����pop�����viewȥ�ˡ�
	 * �����Ϊ�����������gridview����ȥ�ˡ�28¥���ֵ���ʾ�ģ�л�ˡ� ��gridview����setOnKeyListener�����ܽ����
	 */

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { switch
	 * (keyCode) { case KeyEvent.KEYCODE_MENU: if (popupWindow != null &&
	 * popupWindow.isShowing()) { popupWindow.dismiss();
	 * 
	 * } else { openPopupwin(); }
	 * 
	 * Toast.makeText(this, "fd", 1000).show(); break;
	 * 
	 * } return false; }
	 */

}