package com.text.mytab;




import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;

public class IndexActicity extends ActivityGroup  {
	private int index_tab = 0;
	private TabWidget tabWidget;
	View tab1;
	View tab2;
	View tab4;
	View tab3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_layout);
		DisplayMetrics dm = new DisplayMetrics();
		Log.i("adfa","dfadf");
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.WIDTH = dm.widthPixels;
		Constant.HEIGHT= dm.heightPixels;
		//TabHost t =getTabHost();
		TabHost t = (TabHost)findViewById(R.id.t1);
		t.setBackgroundDrawable(getResources().getDrawable(R.drawable.inde_bg));
		t.setup(this.getLocalActivityManager());
		t.setPadding(0, 0, 0, 0);
		tabWidget = t.getTabWidget();

		LayoutInflater fi = LayoutInflater.from(IndexActicity.this);

		View view = fi.inflate(R.layout.tab_layout, null);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.tablayout);
		tab1 = view.findViewById(R.id.tab1);
		tab2 = view.findViewById(R.id.tab2);
		tab3 = view.findViewById(R.id.tab3);
	    tab4 = view.findViewById(R.id.tab4);
		ll.removeAllViews();
		t.addTab(t.newTabSpec("1").setIndicator(tab1
						).setContent(
								new Intent(IndexActicity.this,
										TabActivity1.class)));
		t.addTab(t.newTabSpec("2").setIndicator(tab2
						).setContent(
								new Intent(IndexActicity.this,
										TabActivity2.class)));
		t.addTab(t.newTabSpec("3").setIndicator(tab3
						).setContent(
								new Intent(IndexActicity.this,
										TabActivity3.class)));
		t.addTab(t.newTabSpec("4").setIndicator(tab4).setContent(
				new Intent(IndexActicity.this, TabActivity4.class)));
		tabWidget.setBackgroundColor(getResources().getColor(R.color.alpha_00));
		tabWidget.setBaselineAligned(true);
		tab1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.menu_bg));
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			
			tabWidget.getChildAt(i).getLayoutParams().width = Constant.WIDTH / 4;
			tabWidget.getChildAt(i).getLayoutParams().height=50;
		}
		t.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				tabChanged(tabId);
			}
		});

	}
//捕获tab变化事件
	public void tabChanged(String tabId) {
		if (index_tab != (Integer.valueOf(tabId) - 1)) {
			tabWidget.getChildAt(Integer.valueOf(tabId) - 1)
					.setBackgroundDrawable(
							getResources().getDrawable(R.drawable.menu_bg));
			tabWidget.getChildAt(index_tab).setBackgroundDrawable(null);
			index_tab = Integer.valueOf(tabId) - 1;
			
		}
	}
	
}
