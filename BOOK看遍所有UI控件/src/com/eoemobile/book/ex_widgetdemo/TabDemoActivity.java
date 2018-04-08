package com.eoemobile.book.ex_widgetdemo;

//Download by http://www.codefans.net
import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class TabDemoActivity extends TabActivity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("TabDemoActivity");
		TabHost tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.tab_demo, tabHost.getTabContentView(), true);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("tab1").setContent(R.id.view1));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("tab2").setContent(R.id.view2));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("tab3").setContent(R.id.view3));
	}
}
