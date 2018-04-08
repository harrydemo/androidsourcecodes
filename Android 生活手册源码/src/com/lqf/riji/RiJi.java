package com.lqf.riji;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.lqf.gerenriji.R;
import com.lqf.gerenriji.ZhuJieMian;
import com.lqf.rili.RiLi;
import com.lqf.sqlite.DBhelper;

public class RiJi extends Activity {
	private ListView listView;
	// ����һ���α�
	private Cursor cursor;
	// ����DBHelper��
	private DBhelper helper;

	@SuppressWarnings("static-access")
	protected void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// ���ò��ֱ���
		super.onCreate(savedInstanceState);
		setContentView(R.layout.riji);
		// ��ȡ�Զ�����Ⲽ��
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.biaoti1);

		// ��ȡ���ݿ�ʵ�л�����
		helper = new DBhelper(this);
		// ��ȡ����Ҫ�Ŀؼ�
		listView = (ListView) findViewById(R.id.listview);
		// ���ò�ѯ����
		cursor = helper.rijiselect();
		// �������ݿ�������
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.riji, cursor, new String[] { helper.TITLE,
						helper.MARCH, helper.NEIRONG }, new int[] {
						R.id.biaoti, R.id.riqi, R.id.neirong });
		//��������
		listView.setAdapter(adapter);
		//�������Ĳ˵�
		listView.setOnCreateContextMenuListener(contextMenuListener);
	}

	// ����Menu�˵�
	@SuppressWarnings("unused")
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu menua = menu.addSubMenu(Menu.NONE, 0, 1, "д�ռ�");
		SubMenu menub = menu.addSubMenu(Menu.NONE, 1, 1, "������");
		SubMenu menuc = menu.addSubMenu(Menu.NONE, 2, 1, "�����˱�");
		SubMenu menud = menu.addSubMenu(Menu.NONE, 3, 1, "��ĬЦ��");
		SubMenu menue = menu.addSubMenu(Menu.NONE, 4, 1, "���Ŵ�ȫ");

		return super.onCreateOptionsMenu(menu);
	}

	// ���Menu�˵��¼�
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(RiJi.this, XieRiJi.class);
			startActivity(intent);
			break;
		case 1:
			Intent intent2 = new Intent(RiJi.this, JiNianRi.class);
			startActivity(intent2);
			break;
		case 2:
			
			break;
		case 3:
			Intent intent4 = new Intent(RiJi.this, XiaoHua.class);
			startActivity(intent4);
			break;
		case 4:
			Intent intent5 = new Intent(RiJi.this, DuanXin.class);
			startActivity(intent5);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	 //�÷���������ʾ�����Ĳ˵�
	OnCreateContextMenuListener contextMenuListener = new OnCreateContextMenuListener() {
		
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			//��������Ĳ˵�
			menu.add(Menu.NONE, 0, 2, "����");
			menu.add(Menu.NONE, 1, 2, "�޸�");
			menu.add(Menu.NONE, 2, 2, "ɾ��");
			menu.add(Menu.NONE, 3, 2, "ȫ��ɾ��");
			//��������Ĳ˵�����
			menu.setHeaderTitle("�˵�ѡ��");
			//��������Ĳ˵�����ͼƬ
			menu.setHeaderIcon(android.R.drawable.ic_popup_sync);
			
		}
	};
	//��������Ĳ˵����������¼�
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(RiJi.this, XieRiJi.class);
			startActivity(intent);
			break;
		case 1:
			break;
		case 2:
			
			break;
		case 3:
			break;
		}
		return super.onContextItemSelected(item);
	}
	//���ؼ�����Ч��
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				Intent intent = new Intent(RiJi.this, ZhuJieMian.class);
				setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(R.anim.my_up, R.anim.my_down);
				return false;
			}
			return false;
		}
}
