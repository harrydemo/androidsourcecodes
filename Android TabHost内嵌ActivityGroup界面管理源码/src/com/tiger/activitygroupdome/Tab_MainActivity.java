package com.tiger.activitygroupdome;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * �ײ�ѡ�ѡ�����
 * 
 * @author HuYang
 * 
 */
public class Tab_MainActivity extends TabActivity {
	/** �����¶��� */
	private TabHost tabHost;
	private TabHost.TabSpec spec;
	private RadioGroup mainbtGroup;
	private RadioButton tab_radio_Home;
	private RadioButton tab_radio_Category;
	private RadioButton tab_radio_Cart;
	private RadioButton tab_radio_Personal;
	private RadioButton tab_radio_More;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);
		init_tab();
		initView();
	}

	/**
	 * ��ʼ��
	 */
	public void initView() {
		this.mainbtGroup = (RadioGroup) this.findViewById(R.id.main_radio);
		// ����ײ���ť,�л�����,!
		mainbtGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.menu_tab_home:
					tabHost.setCurrentTabByTag("home");
					break;
				case R.id.menu_tab_category:
					tabHost.setCurrentTabByTag("category");
					break;
				case R.id.menu_tab_cart:
					tabHost.setCurrentTabByTag("cart");
					break;
				case R.id.menu_tab_personal:
					tabHost.setCurrentTabByTag("personal");
				case R.id.menu_tab_more:
					tabHost.setCurrentTabByTag("more");
					break;
				}
			}
		});

	}

	/**
	 * ���õײ�ÿ����ť�������ת�Ľ���.
	 */
	private void init_tab() {
		// ���tabHost����,�̳е�TabActivity,ֱ��ȡ����.!
		tabHost = getTabHost();
		TabSpec spec_home = tabHost
				.newTabSpec("home")
				.setIndicator("home")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								GotoMianActicity.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_home);
		TabSpec spec_category = tabHost
				.newTabSpec("category")
				.setIndicator("category")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_category);

		TabSpec spec_cart = tabHost
				.newTabSpec("cart")
				.setIndicator("cart")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_cart);
		TabSpec spec_personal = tabHost
				.newTabSpec("personal")
				.setIndicator("personal")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_personal);
		TabSpec spec_more = tabHost
				.newTabSpec("more")
				.setIndicator("more")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_more);
		// �õ��ײ�ÿ����ť�Ķ���,! ʵ����.
		tab_radio_Home = (RadioButton) findViewById(R.id.menu_tab_more);
		tab_radio_Category = (RadioButton) findViewById(R.id.menu_tab_category);
		tab_radio_Cart = (RadioButton) findViewById(R.id.menu_tab_cart);
		tab_radio_Personal = (RadioButton) findViewById(R.id.menu_tab_personal);
		tab_radio_More = (RadioButton) findViewById(R.id.menu_tab_more);
	}

	public void promptExit(final Context con) {
		// �����Ի���
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("��ʾ");
		ab.setMessage("ȷ��Ҫ�˳���");
		ab.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Tab_MainActivity.this.finish();
			}
		});
		ab.setNegativeButton("ȡ��", null);
		// ��ʾ�Ի���
		ab.show();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			promptExit(Tab_MainActivity.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
