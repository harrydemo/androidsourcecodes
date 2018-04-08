package com.milifan.portal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TabHost;

import com.milifan.R;
import com.milifan.contact.IncomingContactList;
import com.milifan.contact.MissedContactList;
import com.milifan.contact.OutgoingContactList;
import com.milifan.contact.TotalDisplay;
import com.milifan.util.Common;
import com.milifan.util.SharedPreferencesHelper;

public class CalllogTabs extends TabActivity {
	SharedPreferencesHelper sp;
	public final static String MONTH_TOTAL = "month_total";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		final TabHost host = getTabHost();

		host.addTab(host.newTabSpec("incoming").setIndicator("����",
				getResources().getDrawable(R.drawable.incoming)).setContent(
				new Intent(this, IncomingContactList.class)));

		host.addTab(host.newTabSpec("goouting").setIndicator("����",
				getResources().getDrawable(R.drawable.outgoing)).setContent(
				new Intent(this, OutgoingContactList.class)));
		host.addTab(host.newTabSpec("missed").setIndicator("δ��",
				getResources().getDrawable(R.drawable.missed)).setContent(
				new Intent(this, MissedContactList.class)));
		host.addTab(host.newTabSpec("total").setIndicator("�ܼ�",
				getResources().getDrawable(R.drawable.total)).setContent(
				new Intent(this, TotalDisplay.class)));
		// Common.month_total=this.getProferences();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "����").setIcon(R.drawable.set);
		menu.add(0, 2, 2, "�˳�").setIcon(R.drawable.exit);
		menu.add(0, 1, 1, "����").setIcon(R.drawable.about);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		Dialog dialog = null;
		switch (itemId) {
		case 0:
			LayoutInflater factory = LayoutInflater.from(this);
			final View DialogView = factory.inflate(R.layout.setting, null);
			// ���µ�Activity
			Intent intent = new Intent(CalllogTabs.this, Settings.class);
			startActivity(intent);

			break;
		case 1:
			dialog = new AlertDialog.Builder(this).setTitle("����").setMessage(
					R.string.eula).setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).create();
			dialog.show();
			break;
		case 2:
			this.onDestroy();
			break;
		}
		return true;
	}

}
