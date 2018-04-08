package com.lqf.rili;

import com.lqf.gerenriji.R;
import com.lqf.gerenriji.ZhuJieMian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RiLi extends Activity {
	// ����CalendarView����
	private CalendarView calendarView;

	protected void onCreate(Bundle savedInstanceState) {
		// ��ȡ�������Ʋ���
		LinearLayout mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.rili, null);
		super.onCreate(savedInstanceState);
		setContentView(mainLayout);
		// ��ȡʵ��������
		calendarView = new CalendarView(this);
		// ��ʾ����
		mainLayout.addView(calendarView);
	}

	// ����Menu���ܲ˵�
	public boolean onCreateOptionsMenu(Menu menu) {
		// ����Menu���˵�
		SubMenu menua = menu.addSubMenu(Menu.NONE, 0, 1, "");
		menua.setIcon(R.drawable.rilimenua);// ���ñ���
		SubMenu menub = menu.addSubMenu(Menu.NONE, 1, 1, "");
		menub.setIcon(R.drawable.rilimenub);// ���ñ���
		SubMenu menuc = menu.addSubMenu(Menu.NONE, 2, 1, "");
		menuc.setIcon(R.drawable.rilimenuc);// ���ñ���
		SubMenu menud = menu.addSubMenu(Menu.NONE, 3, 1, "");
		menud.setIcon(R.drawable.rilimenud);// ���ñ���
		SubMenu menue = menu.addSubMenu(Menu.NONE, 4, 1, "");
		menue.setIcon(R.drawable.rilimenue);// ���ñ���
		SubMenu menuf = menu.addSubMenu(Menu.NONE, 5, 1, "");
		menuf.setIcon(R.drawable.rilimenuf);// ���ñ���
		return super.onCreateOptionsMenu(menu);
	}

	// ���Menu�˵����������¼�
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Toast.makeText(RiLi.this, "������˽���", 100).show();
			break;
		case 1:
			Toast.makeText(RiLi.this, "�����������", 100).show();
			break;
		case 2:
			Toast.makeText(RiLi.this, "�����������", 100).show();
			break;
		case 3:
			Toast.makeText(RiLi.this, "�����������", 100).show();
			break;
		case 4:
			Toast.makeText(RiLi.this, "���������Ϣ", 100).show();
			break;
		case 5:
			Toast.makeText(RiLi.this, "������˹���", 100).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//���ؼ�����Ч��
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(RiLi.this, ZhuJieMian.class);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.my_up, R.anim.my_down);
			return false;
		}
		return false;
	}
}
