package com.bus.shenyang.net;

import static com.bus.shenyang.common.Commons.*;

import com.bus.shenyang.R;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class BusNet extends TabActivity{
	private TabHost m_tabHost;
	private LayoutInflater mLayoutInflater;
	String ststop;
	String enstop;
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.busplan);
		Bundle bundle = this.getIntent().getExtras();
		ststop = bundle.getString("ststop");
		enstop = bundle.getString("enstop");
		init();

	}
	private void init() {
		m_tabHost = getTabHost();
		mLayoutInflater = LayoutInflater.from(this);
		int count = TabClassArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = m_tabHost.newTabSpec(TextviewArray[i])
					.setIndicator(getTabItemView(i))
					.setContent(getTabItemIntent(i));
			m_tabHost.addTab(tabSpec);
			m_tabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
		}

	}

	private Intent getTabItemIntent(int i) {
		Intent intent = new Intent();
		
		Bundle bundle = new Bundle();
	
    bundle.putString("ststop1", ststop);
	bundle.putString("enstop1", enstop);
	intent.setClass(BusNet.this, TabClassArray[i]);
	intent.putExtras(bundle);
		return intent;
	}

	private View getTabItemView(int i) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

		if (imageView != null) {
			imageView.setImageResource(ImageViewArray[i]);
		}

		TextView textView = (TextView) view.findViewById(R.id.textview);

		textView.setText(TextviewArray[i]);
		return view;
	}
}