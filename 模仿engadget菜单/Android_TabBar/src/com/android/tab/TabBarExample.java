package com.android.tab;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabBarExample extends TabActivity {

	private int myMenuRes[] = {
			R.drawable.tab1,
			R.drawable.tab2,
			R.drawable.tab3,
			R.drawable.tab4,
			R.drawable.tab5
	};

	TabHost tabHost;
	TabSpec firstTabSpec;
	TabSpec secondTabSpec;
	TabSpec threeTabSpec;
	TabSpec fourTabSpec;
	TabSpec	fiveTabSpec;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);

		setTitle("Tab test");

		/* TabHost will have Tabs */
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setBackgroundResource(R.drawable.nav_background);

		/*
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/* tid1 is firstTabSpec Id. Its used to access outside. */
		firstTabSpec = tabHost.newTabSpec("tid1");
		secondTabSpec = tabHost.newTabSpec("tid2");
		threeTabSpec = tabHost.newTabSpec("tid3");
		fourTabSpec = tabHost.newTabSpec("tid4");
		fiveTabSpec = tabHost.newTabSpec("tid5");

		/* TabSpec setIndicator() is used to set name for the tab. */
		/* TabSpec setContent() is used to set content for a particular tab. */
		firstTabSpec.setIndicator("Latest", getResources().getDrawable(
				myMenuRes[0]));
		secondTabSpec.setIndicator("Topics", getResources().getDrawable(
				myMenuRes[1]));
		threeTabSpec.setIndicator("Video", getResources().getDrawable(
				myMenuRes[2]));
		fourTabSpec.setIndicator("Podcast", getResources().getDrawable(
				myMenuRes[3]));
		fiveTabSpec.setIndicator("Gallery", getResources().getDrawable(
				myMenuRes[4]));

		firstTabSpec.setContent(new Intent(this, FirstTab.class));
		secondTabSpec.setContent(new Intent(this, SecondTab.class));
		threeTabSpec.setContent(new Intent(this, FirstTab.class));
		fourTabSpec.setContent(new Intent(this, SecondTab.class));
		fiveTabSpec.setContent(new Intent(this, SecondTab.class));

		/* Add tabSpec to the TabHost to display. */
		tabHost.addTab(firstTabSpec);
		tabHost.addTab(secondTabSpec);
		tabHost.addTab(threeTabSpec);
		tabHost.addTab(fourTabSpec);
		tabHost.addTab(fiveTabSpec);
	}
}
