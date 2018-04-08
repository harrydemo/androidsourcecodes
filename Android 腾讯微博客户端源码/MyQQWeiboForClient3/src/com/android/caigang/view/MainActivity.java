package com.android.caigang.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.caigang.R;

public class MainActivity extends TabActivity {
	private TabHost tabHost;
	private RadioGroup mainbtGroup;
	private static final String HOME = "主页";
	private static final String REFER = "提及";
	private static final String SECRET = "私信";
	private static final String SEARCH = "搜索";
	private static final String ATTENTIION = "关注";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		
		tabHost = this.getTabHost();

		View view1 = View.inflate(MainActivity.this, R.layout.tab, null);
		((ImageView) view1.findViewById(R.id.tab_imageview_icon)).setImageResource(R.drawable.tab_timeline_selector);
		((TextView) view1.findViewById(R.id.tab_textview_title)).setText(HOME);

		TabHost.TabSpec spec1 = tabHost.newTabSpec(HOME)
				.setIndicator(view1)
				.setContent(new Intent(this, HomeTimeLineActivity.class));
		tabHost.addTab(spec1);
		
		View view2 = View.inflate(MainActivity.this, R.layout.tab, null);
		((ImageView) view2.findViewById(R.id.tab_imageview_icon)).setImageResource(R.drawable.tab_atme_selector);
		((TextView) view2.findViewById(R.id.tab_textview_title)).setText(REFER);

		TabHost.TabSpec spec2 = tabHost.newTabSpec(REFER)
				.setIndicator(view2)
				.setContent(new Intent(this, ReferActivity.class));
		tabHost.addTab(spec2);
		
		View view3 = View.inflate(MainActivity.this, R.layout.tab, null);
		((ImageView) view3.findViewById(R.id.tab_imageview_icon)).setImageResource(R.drawable.tab_message_selector);
		((TextView) view3.findViewById(R.id.tab_textview_title)).setText(SECRET);

		TabHost.TabSpec spec3 = tabHost.newTabSpec(SECRET)
				.setIndicator(view3)
				.setContent(new Intent(this, MessageActivity.class));
		tabHost.addTab(spec3);
		
		View view4 = View.inflate(MainActivity.this, R.layout.tab, null);
		((ImageView) view4.findViewById(R.id.tab_imageview_icon)).setImageResource(R.drawable.tab_explore_selector);
		((TextView) view4.findViewById(R.id.tab_textview_title)).setText(SEARCH);

		TabHost.TabSpec spec4 = tabHost.newTabSpec(SEARCH)
				.setIndicator(view4)
				.setContent(new Intent(this, SearchActivity.class));
		tabHost.addTab(spec4);
		
		View view5 = View.inflate(MainActivity.this, R.layout.tab, null);
		((ImageView) view5.findViewById(R.id.tab_imageview_icon)).setImageResource(R.drawable.tab_focus_selector);
		((TextView) view5.findViewById(R.id.tab_textview_title)).setText(ATTENTIION);

		TabHost.TabSpec spec5 = tabHost.newTabSpec(ATTENTIION)
				.setIndicator(view5)
				.setContent(new Intent(this, AttentionActivity.class));
		tabHost.addTab(spec5);
	}
}