package com.shinylife.smalltools;

import java.util.ArrayList;
import java.util.List;

import com.shinylife.smalltools.BaseActivity;
import com.shinylife.smalltools.ItemAdapter;
import com.shinylife.smalltools.QueryAddress;
import com.shinylife.smalltools.QueryIDCard;
import com.shinylife.smalltools.QueryPhone;
import com.shinylife.smalltools.R;
import com.shinylife.smalltools.UpdateApp;
import com.shinylife.smalltools.entity.NumberItem;
import com.shinylife.smalltools.helper.InternetHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SmallToolsActivity extends BaseActivity {

	private List<NumberItem> list;
	private ListView number_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.main);
		SetTitle(getString(R.string.app_name));
		setTitleBar(0, "", 0, "");
		InitList();
		findViews();
		ItemAdapter adapter = new ItemAdapter(this, list);
		number_list.setAdapter(adapter);
		setListener();

		if (!new InternetHelper(this).getNetworkIsAvailable()) {
			showToast(getString(R.string.no_internet));
		}

	}

	private void findViews() {
		number_list = (ListView) findViewById(R.id.number_list);
	}

	private void setListener() {
		number_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = list.get(arg2).getIntent();
				if (it != null) {
					startActivity(it);
				}
			}
		});
	}

	private void InitList() {
		list = new ArrayList<NumberItem>();
		list.add(new NumberItem(R.drawable.number_query,
				getString(R.string.number_mobile_info), new Intent(this,
						QueryPhone.class)));
		list.add(new NumberItem(R.drawable.number_operator,
				getString(R.string.number_id_info), new Intent(this,
						QueryIDCard.class)));
		list.add(new NumberItem(R.drawable.number_ticket,
				getString(R.string.number_add_info), new Intent(this,
						QueryAddress.class)));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.loginout_title)
					.setMessage(R.string.loginout_msg)
					.setPositiveButton(
							R.string.confirm,
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent startMain = new Intent(
											Intent.ACTION_MAIN);
									startMain.addCategory(Intent.CATEGORY_HOME);
									startMain
											.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(startMain);
									System.exit(0);
								}
							}).setNegativeButton(R.string.cancel, null)
					.create().show();
			return false;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(1, 0, 0, getString(R.string.menu_update)).setIcon(
				R.drawable.menu_update);
		menu.add(1, 1, 1, getString(R.string.menu_help)).setIcon(
				R.drawable.menu_help);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			UpdateApp update = new UpdateApp(SmallToolsActivity.this);
			update.checkVersion();
			break;
		case 1: {
			LayoutInflater factory = LayoutInflater.from(this);
			View view = factory.inflate(R.layout.about, null);
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info).setView(view)
					.setNegativeButton(R.string.cancel, null).create().show();
		}
			break;
		}
		return false;
	}
}