package com.bus.shenyang;

import static com.bus.shenyang.common.Commons.*;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class BusActivity extends TabActivity {
	private TabHost m_tabHost;
	private LayoutInflater mLayoutInflater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		m_tabHost = getTabHost();
		mLayoutInflater = LayoutInflater.from(this);
		int count = mTabClassArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = m_tabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i))
					.setContent(getTabItemIntent(i));
			m_tabHost.addTab(tabSpec);
			m_tabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
		}
	}

	private Intent getTabItemIntent(int i) {
		Intent intent = new Intent(this, mTabClassArray[i]);
		return intent;
	}

	private View getTabItemView(int i) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

		if (imageView != null) {
			imageView.setImageResource(mImageViewArray[i]);
		}

		TextView textView = (TextView) view.findViewById(R.id.textview);

		textView.setText(mTextviewArray[i]);
		return view;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(BusActivity.this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("沈阳离线公交")
					.setMessage("你确定退出了哦?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {

									BusActivity.this.finish();
								}

							}).setNegativeButton("取消", null).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	protected void onResume() {
		super.onResume();
		
		
		
		
	}
}